import { Component, Inject, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DaylogDay, DaylogEvent, DaylogType } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
    selector: 'app-daylog-eventlist',
    templateUrl: './daylog.eventlist.html',
    styleUrls: ['./daylog.component.less']
})
export class DaylogComponentEventlist implements OnChanges, OnInit {
    constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

    @Input("filterEvent") public sFilterEvent: String;
    @Input("showAllTable") public bShowAllTable: Boolean;
    @Input("dateForLoad") public dateForLoad: DaylogDay;
    @Input("daylogTypes") public daylogTypes: DaylogType;

    @ViewChild(MatSort, { static: true }) sortEvent: MatSort;
    public bShowWarning: boolean = false;
    public eventlistDataSource;
    public eventlistDisplayedColumns: string[] = [];
    public eventlist: DaylogEvent[] = [];

    // Auf Änderungen achten und aktionen starten
    ngOnChanges(changes: SimpleChanges) {
        if (changes?.dateForLoad?.currentValue?.date === "LOADALL") {
            this.loadAllEvents();
            this.applyFilter();
        } else if (changes?.dateForLoad) {
            this.loadEventsByDay(this.dateForLoad);
            this.applyFilter();
        }

        if (changes?.bShowAllTable) {
            this.showAllTable();
        }

        if (changes?.sFilterEvent) {
            this.applyFilter();
        }
    }

    ngOnInit(): void {
        this.eventlistDataSource = new MatTableDataSource(this.eventlist);
        this.eventlistDataSource.sort = this.sortEvent;
        this.eventlistDataSource.filter = this.sFilterEvent.trim();
        this.showAllTable;
    }

    private saveEvent(item: DaylogEvent): void {
        this.bShowWarning = true;
        this.http.post(this.rest.gethost() + 'daylog/event/save', item).subscribe((resdata: any) => {
            this.loadEventsByDay(this.dateForLoad);
            if (resdata.save == "true") {
                this.bShowWarning = false;
            }
        });
    }

    public newEventBySelectedDay(): void {
        this.newEvent(this.dateForLoad);
    }

    public openDialogEvent(item: DaylogEvent): void {
        const edititem: DaylogEvent = this.serviceWipf.deepCopy(item);

        const dialogRef = this.dialog.open(DaylogComponentDialogEventComponent, {
            width: '350px',
            height: '350px',
            data: edititem,
        });

        dialogRef.afterClosed().subscribe((result: DaylogEvent) => {
            if (result) {
                this.saveEvent(result);
            }
        });
    }

    public applyFilter() {
        this.serviceWipf.delay(200).then(() => {
            this.eventlistDataSource.filter = this.sFilterEvent.trim();
        });
    }

    public showAllTable(): void {
        this.bShowAllTable = !this.bShowAllTable;
        if (this.bShowAllTable) {
            this.eventlistDisplayedColumns = ['typ', 'data', 'button'];
        } else {
            this.eventlistDisplayedColumns = ['id', 'dateid', 'typ', 'data', 'button'];
        }
    }

    /**
    * Alle Events Laden 
    */
    public loadAllEvents(): void {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.eventlist = [];

        // keine userid möglich -> unsicher!
        this.http.get(this.rest.gethost() + 'daylog/event/getAll').subscribe((resdata: DaylogEvent[]) => {
            this.eventlist = resdata;

            this.eventlistDataSource = new MatTableDataSource(this.eventlist);
            this.eventlistDataSource.sort = this.sortEvent;
            this.eventlistDataSource.filter = this.sFilterEvent.trim();
            warten.close();
        });
    }

    public loadEventsByDay(d: DaylogDay): void {
        if (d.id != undefined) {
            const warten = this.dialog.open(DialogWartenComponent, {});
            this.eventlist = [];

            this.http.get(this.rest.gethost() + 'daylog/event/get/' + d.id).subscribe((resdata: DaylogEvent[]) => {
                this.eventlist = resdata;

                this.eventlistDataSource = new MatTableDataSource(this.eventlist);
                this.eventlistDataSource.sort = this.sortEvent;
                this.eventlistDataSource.filter = this.sFilterEvent.trim();
                warten.close();
            });
        }
    }

    public newEvent(dayitem: DaylogDay): void {
        // Tag speicher um später die Liste neu laden zu können
        //this.dateCacheForLoad = dayitem; TODO nötig?
        let e: DaylogEvent = {};
        e.dateid = dayitem.id;
        e.text = "";
        e.typ = "";
        this.openDialogEvent(e);
    }

    public editEvent(e: DaylogEvent): void {
        this.openDialogEvent(e);
    }

    public deleteEvent(item: any): void {
        item.infotext = "Wirklich löschen? " + item.id;
        const dialogRef = this.dialog.open(DialogJaNeinComponent, {
            width: '250px',
            height: '250px',
            data: item,
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.http.delete(this.rest.gethost() + 'daylog/event/delete/' + item.id).subscribe((resdata: any) => {
                    //this.loadDays(); TODO warum war das hier?
                    this.loadEventsByDay(this.dateForLoad);
                });
            }
        });
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
    selector: 'app-daylog-dialogevent',
    templateUrl: './daylog.dialogEvent.html',
})
export class DaylogComponentDialogEventComponent implements OnInit {
    constructor(public dialogRef: MatDialogRef<DaylogComponentDialogEventComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogEvent, private http: HttpClient, private rest: ServiceRest) { }

    public daylogTypes: DaylogType[] = [];

    ngOnInit(): void {
        this.loadDaylogTypes();
    }

    private loadDaylogTypes(): void {
        this.http.get(this.rest.gethost() + 'daylog/type/getAll').subscribe((resdata: DaylogDay[]) => {
            this.daylogTypes = resdata;
        });
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
