import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DaylogDay, DaylogEvent, DaylogType, DialogInputOneThingContent } from 'src/app/datatypes';
import { DialogInputOneThingComponent, DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
    selector: 'app-daylog',
    templateUrl: './daylog.component.html',
    styleUrls: ['./daylog.component.less'],
    standalone: false
})
export class DayLogComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sortDay: MatSort;

  public daylistDataSource;
  public daylistDisplayedColumns: string[] = [];
  public sFilterYYYY: number = undefined;
  public sFilterMON: number = undefined;
  public sFilterDay: string = "";
  public sFilterTextEvent: string = "";
  public bShowWarning: boolean = false;
  public daylogTypes: DaylogType[] = [];
  public dateForLoad: DaylogDay = {};
  public bShowAllTableColumns: boolean = true;
  public bShowDayTable: boolean = true;
  public typelistForEventFilter: DaylogType[] = [];
  public selectedEventTypeFilter: any;
  public heuteDatum: string = "";
  private filterActiveCache: boolean = false;

  ngOnInit() {
    this.initFilter();
    this.dateForLoad = { date: "" };
    this.loadDays();
    this.loadDaylogTypes();
    this.showAllTableColumns();
    this.loadTypeListForEventFilter();
    this.bShowDayTable = true;
    this.heuteDatum = new Date(Date.now()).toISOString().split('T')[0];
  }

  private initFilter(): void {
    this.sFilterYYYY = (new Date(Date.now()).getFullYear());
    this.sFilterMON = (new Date(Date.now()).getMonth() + 1);

    this.reloadDayFilter();
  }

  private reloadDayFilter(): void {
    this.sFilterDay = this.sFilterYYYY + "-" + this.serviceWipf.pad(this.sFilterMON, 2);
    this.dayFilter();
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
    this.daylistDisplayedColumns = ['date', 'tagestext', 'button'];
    this.dateForLoad = ddl;
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.daylistDisplayedColumns = ['id', 'date', 'day', 'tagestext', 'button'];
    } else {
      this.daylistDisplayedColumns = ['day', 'date', 'tagestext', 'button'];
    }
  }

  public openDialogTypeVW(): void {
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

    this.rest.get('daylog/day/getAllByDateQuery/' + this.sFilterDay).then((resdata: DaylogDay[]) => {
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
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.date;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '100px',
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

  public dayFilter(): void {
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

  public changeMonat(vorRueck: boolean): void {
    if (vorRueck) {
      if (this.sFilterMON == 1) {
        this.sFilterMON = 12;
        this.changeYYYY(true);
      } else if (this.sFilterMON > 1) {
        this.sFilterMON--;
      }
    } else {
      if (this.sFilterMON == 12) {
        this.sFilterMON = 1;
        this.changeYYYY(false);
      } else if (this.sFilterMON < 12) {
        this.sFilterMON++;
      }
    }
    this.dateForLoad = {};
    this.reloadDayFilter();
  }

  public changeYYYY(vorRueck: boolean): void {
    if (vorRueck) {
      this.sFilterYYYY--;
    }
    else {
      this.sFilterYYYY++;
    }
    this.dateForLoad = {};
    this.reloadDayFilter();
  }

  public setMonat(): void {
    let datenfeld: DialogInputOneThingContent = {
      type: "number",
      infotext: "Monat einstellen",
      infotext2: "Monat",
      data: this.sFilterMON
    };

    const dialogRef = this.dialog.open(DialogInputOneThingComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: datenfeld,
    });

    dialogRef.afterClosed().subscribe((result: DialogInputOneThingContent) => {
      if (result) {
        if (result.data > 0 && result.data < 13){
          this.sFilterMON = result.data;
          this.reloadDayFilter();
        }
      }
    });
  }

  public setYYYY(): void {
    let datenfeld: DialogInputOneThingContent = {
      type: "number",
      infotext: "Jahr einstellen",
      infotext2: "Jahr",
      data: this.sFilterYYYY
    };

    const dialogRef = this.dialog.open(DialogInputOneThingComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: datenfeld,
    });

    dialogRef.afterClosed().subscribe((result: DialogInputOneThingContent) => {
      if (result) {
        this.sFilterYYYY = result.data;
        this.reloadDayFilter();
      }
    });
  }

  public applyFilter(): void {
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
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '100px',
    });

    dialogRef.afterClosed().subscribe((result: DaylogDay) => {
      if (result) {
        this.saveDay(result);
      }
    });
  }

  public loadAllEventsViaVar(): void {
    let d: DaylogDay = { date: "Alle-Events" };
    this.bShowDayTable = false;
    this.dateForLoad = d;
  }

  private saveDay(item: DaylogDay): void {
    this.bShowWarning = true;
    this.rest.post('daylog/day/save', item).then((resdata: DaylogDay) => {
      this.loadDays();
      if (resdata) {
        this.bShowWarning = false;
        // Nach speichern, den Tag laden
        this.loadEventsByDaySetVar(resdata);
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
    standalone: false
})
export class DaylogComponentDialogDayComponent {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogDayComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogDay) {
    dialogRef.disableClose = true;
  }

  public onNoClick(): void {
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
    styleUrls: ['./daylog.component.less'],
    standalone: false
})

export class DaylogComponentDialogTypeListComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogTypeListComponent>, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public eventlistDisplayedColumns: string[] = ['id', 'prio', 'type', 'art', 'color', 'preview', 'button'];
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
    e.prio = 10;
    e.preview = 5;
    e.color = "#ffffff";
    this.openDialogType(e);
  }

  public editType(e: DaylogType): void {
    this.openDialogType(e);
  }

  public openDialogType(item: DaylogEvent): void {
    const edititem: DaylogEvent = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(DaylogComponentDialogTypeComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '100px',
    });

    dialogRef.afterClosed().subscribe((result: DaylogEvent) => {
      if (result) {
        this.saveType(result);
      }
    });
  }

  private saveType(item: DaylogType): void {
    this.bShowWarning = true;
    this.rest.post('daylog/type/save', item).then(() => {
      this.bShowWarning = false;
      this.loadType();
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
    item.infotext = "Wirklich löschen?"; + item.type;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '100px',
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

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Component({
    selector: 'app-daylog-dialogtype',
    templateUrl: './daylog.dialogType.html',
    standalone: false
})
export class DaylogComponentDialogTypeComponent {
  constructor(public dialogRef: MatDialogRef<DaylogComponentDialogTypeComponent>, @Inject(MAT_DIALOG_DATA) public data: DaylogType) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
