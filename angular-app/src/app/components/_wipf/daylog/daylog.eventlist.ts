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

    @Input("filterTextEvent") public sFilterTextEvent: string;
    @Input("showAllTableColumns") public bShowAllTableColumns: Boolean;
    @Input("dateForLoad") public dateForLoad: DaylogDay;
    @Input("daylogTypes") public daylogTypes: DaylogType[];
    @Input("filterEventType") public filterEventType: DaylogType;

    @ViewChild(MatSort, { static: true }) sortEvent: MatSort;
    public bShowWarning: boolean = false;
    public eventlistDataSource;
    public eventlistDisplayedColumns: string[] = [];
    public eventlist: DaylogEvent[] = [];
    public isLoadAllEvents: boolean = false;

    // Auf Änderungen achten und aktionen starten
    ngOnChanges(changes: SimpleChanges) {
        if (changes?.dateForLoad) {
            this.doLoadEventList();
            this.applyFilter();
        }

        if (changes?.bShowAllTableColumns) {
            this.showAllTableColumns();
        }

        if (changes?.sFilterTextEvent) {
            this.applyFilter();
        }

        if (changes?.filterEventType) {
            this.applyFilterByType();
        } else {
            // Filter leeren
            this.filterEventType = undefined;
        }
    }

    ngOnInit(): void {
        this.eventlistDataSource = new MatTableDataSource(this.eventlist);
        this.eventlistDataSource.sort = this.sortEvent;
        this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
        this.showAllTableColumns;
    }

    private saveEvent(item: DaylogEvent): void {
        this.bShowWarning = true;
        this.rest.post('daylog/event/save', item).then(() => {
            this.doLoadEventList();
            this.bShowWarning = false;
        });
    }

    private doLoadEventList(): void {
        // Je nachdem die gesamte Liste oder nur den Tag laden bzw. leeren
        if (this.dateForLoad.date === "") {
            this.eventlistDataSource = new MatTableDataSource();;
        }
        else if (this.dateForLoad.date === "Alle-Events") {
            this.loadAllEvents();
            this.applyFilter();
            this.applyFilterByType();
        } else {
            this.loadEventsByDay(this.dateForLoad);
            this.applyFilter();
            this.applyFilterByType();
        }
    }

    public newEventBySelectedDay(): void {
        this.newEvent(this.dateForLoad);
    }

    public openDialogEvent(item: DaylogEvent): void {
        const edititem: DaylogEvent = this.serviceWipf.deepCopy(item);

        const dialogRef = this.dialog.open(DaylogComponentDialogEventComponent, {
            data: edititem,
            autoFocus: true,
            minWidth: '200px',
            minHeight: '150px',
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
            this.eventlistDataSource.sort = this.sortEvent;
        });
    }

    public applyFilterByType() {
        if (this.filterEventType != undefined) {
            let eventlistToShow: DaylogEvent[] = [];

            this.eventlist.forEach((event: DaylogEvent) => {
                if (event.typid == this.filterEventType.id.toString()) {
                    eventlistToShow.push(event);
                }
            });
            this.eventlistDataSource = new MatTableDataSource(eventlistToShow);
            this.eventlistDataSource.sort = this.sortEvent;
            this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
        } else {
            // Wie Normal -> alles anzeigen
            this.eventlistDataSource = new MatTableDataSource(this.eventlist);
            this.eventlistDataSource.sort = this.sortEvent;
            this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
        }

    }

    public showAllTableColumns(): void {
        this.bShowAllTableColumns = !this.bShowAllTableColumns;
        if (this.bShowAllTableColumns) {
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
        this.isLoadAllEvents = true;

        // keine userid möglich -> unsicher!
        this.rest.get('daylog/event/getAll').then((resdata: DaylogEvent[]) => {
            this.eventlist = resdata;

            this.eventlistDataSource = new MatTableDataSource(this.eventlist);
            this.eventlistDataSource.sort = this.sortEvent;
            this.eventlistDataSource.filter = this.sFilterTextEvent.trim();
            warten.close();
            this.applyFilterByType();
        });
    }

    /**
    * Alle Events eines Tages Laden 
    */
    private loadEventsByDay(d: DaylogDay): void {
        if (d.id != undefined) {
            this.isLoadAllEvents = false;
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
        let e: DaylogEvent = {};
        e.dateid = dayitem.id;
        e.text = "";
        e.typid = "";
        this.openDialogEvent(e);
    }

    public editEvent(e: DaylogEvent): void {
        this.openDialogEvent(e);
    }

    public deleteEvent(item: any): void {
        item.infotext = "Wirklich löschen?";
        item.infotext2 = item.id;
        const dialogRef = this.dialog.open(DialogJaNeinComponent, {
            minWidth: '200px',
            minHeight: '150px',
            data: item,
            autoFocus: true
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.rest.delete('daylog/event/delete/' + item.id).then((resdata: any) => {
                    if (this.isLoadAllEvents) {
                        // Komplette liste
                        this.loadAllEvents();
                    } else {
                        // Einzelansicht
                        this.loadEventsByDay(this.dateForLoad);
                    }
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
    styleUrls: ['./daylog.component.less'],
})
export class DaylogComponentDialogEventComponent implements OnInit {
    constructor(public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<DaylogComponentDialogEventComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogEvent, private rest: ServiceRest) {
        dialogRef.disableClose = true;
        dialogRef.updateSize("70%", "70%");
    }

    public daylogTypes: DaylogType[] = [];
    public sListVorschlag: string[] = [];
    private bSucheAktiv: boolean = false;
    private mehrladenCounter: number = 0;

    ngOnInit(): void {
        this.loadDaylogTypes();
        // Beim bearbeiten die Vorschläge bereits initial vorladen
        if (this.data.text.length > 2) {
            this.getTextVorschlag();
        }
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
        var suchetext = this.data.text.trim().replace('/', "%2F").replace("  ", " ");
        if (this.data.text.length > 1) {
            this.rest.getNoWartenDialog('daylog/event/getTextBySearchAndType/' + suchetext + '/' + this.data.typid).then((resdata: string[]) => {
                this.sListVorschlag = resdata;
            });
        } else {
            this.loadLastVorschlaege(this.daylogTypes.find(item => item.id === Number(this.data.typid)).preview + this.mehrladenCounter);
        }
    }

    public vorschlagMehrLaden(): void {
        this.mehrladenCounter = this.mehrladenCounter + 10;
        this.loadTextVorschlag();
    }

    private loadLastVorschlaege(nAnzahl: number): void {
        this.rest.getNoWartenDialog('daylog/event/getLastByType/' + this.data.typid + '/' + nAnzahl).then((resdata: string[]) => {
            this.sListVorschlag = resdata;
        });
    }

    public vorschlagToData(sItem: string): void {
        this.data.text = sItem;
        this.loadTextVorschlag();
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }

    public convertBool(b: boolean): string {
        if (b) {
            return "Y^";
        } else {
            return "N^";
        }
    }

}
