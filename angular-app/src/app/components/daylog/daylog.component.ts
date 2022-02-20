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
  selector: 'app-daylog',
  templateUrl: './daylog.component.html',
  styleUrls: ['./daylog.component.less'],
})
export class DayLogComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sortDay: MatSort;

  public daylistDataSource;
  public daylist: DaylogDay[] = [];
  public userid = 0;
  public daylistDisplayedColumns: string[] = [];
  public sFilterDay: String = "";
  public sFilterEvent: String = "";
  public bShowWarning: boolean = false;
  public daylogTypes: DaylogType[] = [];
  public dateForLoad: DaylogDay = {};
  public bShowAllTable: boolean = true;

  ngOnInit() {
    this.sFilterDay = new Date(Date.now()).getFullYear().toString() + "-" + this.serviceWipf.pad((new Date(Date.now()).getMonth() + 1), 2);
    this.loadDays();
    this.loadDaylogTypes();
    this.showAllTable();
  }

  public loadEventsByDaySetVar(ddl: DaylogDay): void {
    this.dateForLoad = ddl;
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.daylistDisplayedColumns = ['id', 'date', 'tagestext', 'userid', 'button'];
    } else {
      this.daylistDisplayedColumns = ['date', 'tagestext', 'button'];
    }
  }

  public openDialogTypeVW() {
    const dialogRef = this.dialog.open(DaylogComponentDialogTypeListComponent, {
      width: '550px',
      height: '550px',
    });

    // Typen nach den Edit neu laden
    dialogRef.afterClosed().subscribe((result: DaylogDay) => {
      this.loadDaylogTypes();
    });
  }

  private loadDaylogTypes(): void {
    this.http.get(this.rest.gethost() + 'daylog/type/getAll').subscribe((resdata: DaylogDay[]) => {
      this.daylogTypes = resdata;
    });
  }

  public loadDays(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.daylist = [];

    this.http.get(this.rest.gethost() + 'daylog/day/getAll/' + this.userid).subscribe((resdata: DaylogDay[]) => {
      this.daylist = resdata;

      this.daylistDataSource = new MatTableDataSource(this.daylist);
      this.daylistDataSource.sort = this.sortDay;
      this.daylistDataSource.filter = this.sFilterDay.trim();
      warten.close();
    });
  }

  public deleteDay(item: any): void {
    item.infotext = "Wirklich löschen? " + item.date;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'daylog/day/delete/' + item.id).subscribe((resdata: any) => {
          this.loadDays();
        });
      }
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.daylistDataSource.filter = this.sFilterDay.trim();
    });
  }

  public newDay(): void {
    let e: DaylogDay = {};
    e.tagestext = "";
    e.date = new Date(Date.now()).toISOString().split('T')[0]; // heuteigen Tag als vorauswahl
    this.openDialogDay(e);
  }

  public editDay(e: DaylogDay): void {
    this.openDialogDay(e);
  }

  public openDialogDay(item: DaylogDay): void {
    const edititem: DaylogDay = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(DaylogComponentDialogDayComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: DaylogDay) => {
      if (result) {
        this.saveDay(result);
      }
    });
  }

  public loadAllEventsViaVar(): void {
    let d: DaylogDay = { date: "LOADALL" };
    this.dateForLoad = d;
  }

  private saveDay(item: DaylogDay): void {
    this.bShowWarning = true;
    this.http.post(this.rest.gethost() + 'daylog/day/getDateAndCrateIfDateNotExistsByJSON', item).subscribe((resdata: DaylogDay) => {
      this.loadDays();
      if (resdata.id != null) {
        this.bShowWarning = false;
        // Nach speichern, den Tag laden
        //this.loadEventsByDay(resdata);
        this.dateForLoad = resdata;
      }
    });
  }

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
  selector: 'app-daylog-dialogday',
  templateUrl: './daylog.dialogDay.html',
})
export class DaylogComponentDialogDayComponent {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogDayComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogDay) { }

  onNoClick(): void {
    this.dialogRef.close();
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
  selector: 'app-daylog-dialogtypelist',
  templateUrl: './daylog.dialogTypeList.html',
})
export class DaylogComponentDialogTypeListComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogTypeListComponent>, private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public eventlistDisplayedColumns: string[] = ['id', 'type', 'art', 'button'];
  public sFilter: String = "";
  public bShowWarning: boolean = false;
  public daylogTypeDataSource;
  public typelist: DaylogType[] = [];
  @ViewChild(MatSort, { static: true }) sortType: MatSort;

  ngOnInit() {
    this.loadType();
  }

  public newType(): void {
    let e: DaylogType = {};
    e.type = "";
    this.openDialogType(e);
  }

  public editType(e: DaylogType): void {
    this.openDialogType(e);
  }

  public openDialogType(item: DaylogEvent): void {
    const edititem: DaylogEvent = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(DaylogComponentDialogTypeComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: DaylogEvent) => {
      if (result) {
        this.saveType(result);
      }
    });
  }

  private saveType(item: DaylogType): void {
    this.bShowWarning = true;
    this.http.post(this.rest.gethost() + 'daylog/type/save', item).subscribe((resdata: any) => {
      if (resdata.save == "true") {
        this.bShowWarning = false;
        this.loadType();
      }
    });
  }

  public loadType(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.typelist = [];

    this.http.get(this.rest.gethost() + 'daylog/type/getAll').subscribe((resdata: DaylogType[]) => {
      this.typelist = resdata;

      this.daylogTypeDataSource = new MatTableDataSource(this.typelist);
      this.daylogTypeDataSource.sort = this.sortType;
      warten.close();
    });
  }

  public deleteType(item: any): void {
    item.infotext = "Wirklich löschen? " + item.type;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'daylog/type/delete/' + item.id).subscribe((resdata: any) => {
          this.loadType();
        });
      }
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.daylogTypeDataSource.filter = this.sFilter.trim();
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
  selector: 'app-daylog-dialogtype',
  templateUrl: './daylog.dialogType.html',
})
export class DaylogComponentDialogTypeComponent {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogTypeComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogType) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
  selector: 'app-daylog-eventlist',
  templateUrl: './daylog.eventlist.html',
})
export class DaylogComponentEventlist implements OnChanges, OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @Input("filterEvent") public sFilterEvent: String;
  @Input("showAllTable") public bShowAllTable: Boolean;
  @Input("dateForLoad") public dateForLoad: DaylogDay;
  @Input("daylogTypes") public daylogTypes: DaylogType;

  @ViewChild(MatSort, { static: true }) sortEvent: MatSort;
  public bShowWarning: boolean = false; // TODO OUTPUT
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
    if (d != undefined) {
      const warten = this.dialog.open(DialogWartenComponent, {});
      this.eventlist = [];
      //this.dateCacheForLoad = d; TODO nötig?

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
