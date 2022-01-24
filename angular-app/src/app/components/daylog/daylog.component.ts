import { Component, Inject, OnInit, ViewChild } from '@angular/core';
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
  @ViewChild(MatSort, { static: true }) sortEvent: MatSort;

  public daylistDataSource;
  public eventlistDataSource;
  public daylist: DaylogDay[] = [];
  public eventlist: DaylogEvent[] = [];
  public userid = 0;
  public daylistDisplayedColumns: string[] = [];
  public eventlistDisplayedColumns: string[] = [];
  public sFilter: String = "";
  public bShowWarning: boolean = false;
  public daylogTypes: DaylogType[] = [];
  public dateCacheForLoad: DaylogDay = {};
  public bShowAllTable: boolean = true;

  ngOnInit() {
    this.loadDays();
    this.loadDaylogTypes();
    this.showAllTable();
    // TODO: laden detail -> nur von ausgewälten tag ->
    // this.loadEvents();
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.daylistDisplayedColumns = ['id', 'date', 'tagestext', 'userid', 'button'];
      this.eventlistDisplayedColumns = ['id', 'dateid', 'typ', 'data', 'button'];
    } else {
      this.daylistDisplayedColumns = ['date', 'tagestext', 'button'];
      this.eventlistDisplayedColumns = ['typ', 'data', 'button'];
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
      warten.close();
    });
  }

  public loadEvents(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.eventlist = [];

    // keine userid möglich -> unsicher!
    this.http.get(this.rest.gethost() + 'daylog/event/getAll').subscribe((resdata: DaylogEvent[]) => {
      this.eventlist = resdata;

      this.eventlistDataSource = new MatTableDataSource(this.eventlist);
      this.eventlistDataSource.sort = this.sortEvent;
      warten.close();
    });
  }

  public loadEventsByDay(d: DaylogDay): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.eventlist = [];
    this.dateCacheForLoad = d;

    this.http.get(this.rest.gethost() + 'daylog/event/get/' + d.id).subscribe((resdata: DaylogEvent[]) => {
      this.eventlist = resdata;

      this.eventlistDataSource = new MatTableDataSource(this.eventlist);
      this.eventlistDataSource.sort = this.sortEvent;
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
          this.loadDays();
        });
      }
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.daylistDataSource.filter = this.sFilter.trim();
      this.eventlistDataSource.filter = this.sFilter.trim();

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

  public newEventBySelectedDay(): void {
    let tmpDay = this.dateCacheForLoad; // TODO: hier
    this.newEvent(tmpDay);
  }

  public newEvent(dayitem: DaylogDay): void {
    // Tag speicher um später die Liste neu laden zu können
    this.dateCacheForLoad = dayitem;
    let e: DaylogEvent = {};
    e.dateid = dayitem.id;
    e.text = "";
    e.typ = "";
    this.openDialogEvent(e);
  }

  public editEvent(e: DaylogEvent): void {
    this.openDialogEvent(e);
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

  private saveDay(item: DaylogDay): void {
    this.bShowWarning = true;
    this.http.post(this.rest.gethost() + 'daylog/day/getDateAndCrateIfDateNotExistsByJSON', item).subscribe((resdata: DaylogDay) => {
      this.loadDays();
      if (resdata.id != null) {
        this.bShowWarning = false;
        // Nach speichern, den Tag laden
        this.loadEventsByDay(resdata);
      }
    });
  }

  private saveEvent(item: DaylogEvent): void {
    this.bShowWarning = true;
    this.http.post(this.rest.gethost() + 'daylog/event/save', item).subscribe((resdata: any) => {
      this.loadEventsByDay(this.dateCacheForLoad);
      if (resdata.save == "true") {
        this.bShowWarning = false;
      }
    });
  }

}

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
    this.daylogTypeDataSource.filter = this.sFilter.trim();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
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
