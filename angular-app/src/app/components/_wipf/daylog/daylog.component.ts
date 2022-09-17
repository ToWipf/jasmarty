import { Component, Inject, OnInit, ViewChild } from '@angular/core';
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
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sortDay: MatSort;

  public daylistDataSource;
  public userid = 0;
  public daylistDisplayedColumns: string[] = [];
  public sFilterDay: string = "";
  public sFilterTextEvent: string = "";
  public bShowWarning: boolean = false;
  public daylogTypes: DaylogType[] = [];
  public dateForLoad: DaylogDay = {};
  public bShowAllTable: boolean = true;
  public bShowDayTable: boolean = true;
  public typelistForEventFilter: DaylogType[] = [];
  public selectedEventTypeFilter: any;
  private filterActiveCache: boolean = false;

  ngOnInit() {
    this.sFilterDay = new Date(Date.now()).getFullYear().toString() + "-" + this.serviceWipf.pad((new Date(Date.now()).getMonth() + 1), 2);
    this.dateForLoad = { date: "" };
    this.loadDays();
    this.loadDaylogTypes();
    this.showAllTable();
    this.loadTypeListForEventFilter();
    this.bShowDayTable = true;
  }

  public loadTypeListForEventFilter(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.typelistForEventFilter = [];

    this.rest.get('daylog/type/getAll').then((resdata: DaylogType[]) => {
      this.typelistForEventFilter = resdata;
      warten.close();
    });
  }

  public loadEventsByDaySetVar(ddl: DaylogDay): void {
    this.dateForLoad = ddl;
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.daylistDisplayedColumns = ['id', 'date', 'day', 'tagestext', 'userid', 'button'];
    } else {
      this.daylistDisplayedColumns = ['day', 'date', 'tagestext', 'button'];
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
    this.rest.get('daylog/type/getAll').then((resdata: DaylogDay[]) => {
      this.daylogTypes = resdata;
    });
  }

  public loadDays(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    if (this.sFilterDay.length == 0) {
      this.sFilterDay = "-";
    }

    this.rest.get('daylog/day/getAllByDateQuery/' + this.sFilterDay + "/" + this.userid).then((resdata: DaylogDay[]) => {
      resdata.forEach((d: DaylogDay) => {
        d.extrafeld_wochentag = new Date(d.date).toLocaleDateString('de-de', { weekday: 'short' });
      });

      this.daylistDataSource = new MatTableDataSource(resdata);
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
        this.rest.delete('daylog/day/delete/' + item.id).then((resdata: any) => {
          this.loadDays();
        });
      }
    });
  }

  public dayFilter() {
    if (!this.filterActiveCache) {
      this.filterActiveCache = true;
      this.serviceWipf.delay(500).then(() => {
        if (this.filterActiveCache) {
          this.loadDays();
          this.filterActiveCache = false;
        }
      });
    }
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
    this.bShowDayTable = false;
    this.dateForLoad = d;
  }

  private saveDay(item: DaylogDay): void {
    this.bShowWarning = true;
    this.rest.post('daylog/day/getDateAndCrateIfDateNotExistsByJSON', item).then((resdata: DaylogDay) => {
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

  public setText(sIn: string): void {
    this.data.tagestext = sIn;
  }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
  selector: 'app-daylog-dialogtypelist',
  templateUrl: './daylog.dialogTypeList.html',
  styleUrls: ['./daylog.component.less']
})

export class DaylogComponentDialogTypeListComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogTypeListComponent>, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public eventlistDisplayedColumns: string[] = ['id', 'type', 'art', 'button'];
  public sFilter: string = "";
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
    this.rest.post('daylog/type/save', item).then((resdata: any) => {
      if (resdata.save == "true") {
        this.bShowWarning = false;
        this.loadType();
      }
    });
  }

  public loadType(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.typelist = [];

    this.rest.get('daylog/type/getAll').then((resdata: DaylogType[]) => {
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
        this.rest.delete('daylog/type/delete/' + item.id).then((resdata: any) => {
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
