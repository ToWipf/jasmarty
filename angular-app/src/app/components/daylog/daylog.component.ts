import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DaylogDay, DaylogEvent } from 'src/app/datatypes';
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

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public daylistDataSource;
  public eventlistDataSource;
  public daylist: DaylogDay[] = [];
  public eventlist: DaylogEvent[] = [];
  public userid = 0;
  public daylistDisplayedColumns: string[] = ['id', 'date', 'tagestext', 'userid', 'button'];
  public eventlistDisplayedColumns: string[] = ['id', 'dateid', 'typ', 'data', 'button'];
  public sFilter: String = "";
  public bShowWarning: boolean = false;
  private dateCacheForLoad: DaylogDay = {};

  ngOnInit() {
    this.loadDays();
    // TODO: laden detail -> nur von ausgewälten tag ->
    // this.loadEvents();
  }

  public loadDays(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.daylist = [];

    this.http.get(this.rest.gethost() + 'daylog/day/getAll/' + this.userid).subscribe((resdata: DaylogDay[]) => {
      this.daylist = resdata;

      this.daylistDataSource = new MatTableDataSource(this.daylist);
      this.daylistDataSource.sort = this.sort;
      warten.close();
    });
  }

  public loadEvents(): void {
    // NUR DEBUG TODO: da keine userid
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.eventlist = [];

    this.http.get(this.rest.gethost() + 'daylog/event/getAll/').subscribe((resdata: DaylogEvent[]) => {
      this.eventlist = resdata;

      this.eventlistDataSource = new MatTableDataSource(this.eventlist);
      this.eventlistDataSource.sort = this.sort;
      warten.close();
    });
  }

  public loadEventsByDay(d: DaylogDay): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.eventlist = [];

    this.http.get(this.rest.gethost() + 'daylog/event/get/' + d.id).subscribe((resdata: DaylogEvent[]) => {
      this.eventlist = resdata;

      this.eventlistDataSource = new MatTableDataSource(this.eventlist);
      this.eventlistDataSource.sort = this.sort;
      warten.close();
    });
  }

  public deleteDay(item: any): void {
    item.infotext = "Wirklich löschen? " + item.date;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item.date,
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
      data: item.id,
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
    this.daylistDataSource.filter = this.sFilter.trim();
    this.eventlistDataSource.filter = this.sFilter.trim();
  }

  public newDay(): void {
    let e: DaylogDay = {};
    e.tagestext = "";
    this.openDialogDay(e);
  }

  public editDay(e: DaylogDay): void {
    this.openDialogDay(e);
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
    this.http.post(this.rest.gethost() + 'daylog/day/save', item).subscribe((resdata: any) => {
      this.loadDays();
      if (resdata.save == "true") {
        this.bShowWarning = false;
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
export class DaylogComponentDialogEventComponent {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogEventComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogEvent) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
