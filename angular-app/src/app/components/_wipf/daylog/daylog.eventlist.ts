import { Component, Inject, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
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
    constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

    @Input("filterTextEvent") public sFilterTextEvent: String;
    @Input("showAllTable") public bShowAllTable: Boolean;
    @Input("dateForLoad") public dateForLoad: DaylogDay;
    @Input("daylogTypes") public daylogTypes: DaylogType;
    @Input("filterEventType") public filterEventType: DaylogType;

    @ViewChild(MatSort, { static: true }) sortEvent: MatSort;
    public bShowWarning: boolean = false;
    public eventlistDataSource;
    // public eventlistDataSource: MatTableDataSource<Component>;
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

        if (changes?.sFilterTextEvent) {
            this.applyFilter();
        }

        if (changes?.filterEventType) {
            this.applyFilterByType();
        } else {
            // Filter immer leeren
            this.filterEventType = undefined;
        }
    }

    ngOnInit(): void {
        this.eventlistDataSource = new MatTableDataSource(this.eventlist);
        this.eventlistDataSource.sort = this.sortEvent;
        this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
        this.showAllTable;
    }

    private saveEvent(item: DaylogEvent): void {
        this.bShowWarning = true;
        this.rest.post('daylog/event/save', item).then((resdata: any) => {
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
            this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
        });
    }

    public applyFilterByType() {
        let eventlistToShow: DaylogEvent[] = [];
        if (this.filterEventType != undefined) {

            this.eventlist.forEach((event: DaylogEvent) => {
                if (event.typ == this.filterEventType.id.toString()) {
                    eventlistToShow.push(event);
                }
            });
            this.eventlistDataSource = new MatTableDataSource(eventlistToShow);
        } else {
            // Wie Normal -> alles anzeigen
            this.eventlistDataSource = new MatTableDataSource(this.eventlist);
        }

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
        this.rest.get('daylog/event/getAll').then((resdata: DaylogEvent[]) => {
            this.eventlist = resdata;

            this.eventlistDataSource = new MatTableDataSource(this.eventlist);
            this.eventlistDataSource.sort = this.sortEvent;
            this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
            warten.close();
        });
    }

    public loadEventsByDay(d: DaylogDay): void {
        if (d.id != undefined) {
            const warten = this.dialog.open(DialogWartenComponent, {});
            this.eventlist = [];

            this.rest.get('daylog/event/get/' + d.id).then((resdata: DaylogEvent[]) => {
                this.eventlist = resdata;

                this.eventlistDataSource = new MatTableDataSource(this.eventlist);
                this.eventlistDataSource.sort = this.sortEvent;
                this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
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
                this.rest.delete('daylog/event/delete/' + item.id).then((resdata: any) => {
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
    constructor(public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<DaylogComponentDialogEventComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogEvent, private rest: ServiceRest) { }

    public daylogTypes: DaylogType[] = [];
    public sTextVorschlag: String = "";
    private bSucheAktiv: boolean = false;

    ngOnInit(): void {
        this.loadDaylogTypes();
    }

    private loadDaylogTypes(): void {
        this.rest.get('daylog/type/getAll').then((resdata: DaylogDay[]) => {
            this.daylogTypes = resdata;
        });
    }

    public getTextVorschlag(): void {
        if (!this.bSucheAktiv) {
            this.bSucheAktiv = true;
            this.serviceWipf.delay(300).then(() => {
                if (this.bSucheAktiv) {
                    this.loadTextVorschlag();
                    this.bSucheAktiv = false;
                }
            });
        }
    }

    private loadTextVorschlag(): void {
        if (this.data.text.length > 0) {
            this.rest.getNoWartenDialog('daylog/event/getTextBySearchAndType/' + this.data.text + '/' + this.data.typ).then((resdata: String[]) => {
                resdata.forEach((str: String) => {
                    if (this.sTextVorschlag.length > 0) {
                        this.sTextVorschlag = this.sTextVorschlag + ", ";
                    }
                    this.sTextVorschlag = this.sTextVorschlag + str.toString();
                });
            });
        } else {
            this.sTextVorschlag = "-";
        }
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
