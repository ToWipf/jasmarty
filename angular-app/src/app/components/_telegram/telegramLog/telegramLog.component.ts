import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { Telegram } from 'src/app/datatypes';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-telegram-log',
  templateUrl: './telegramLog.component.html',
  styleUrls: ['./telegramLog.component.less'],
})
export class TelegramLogComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['mid', 'chatid', 'type', 'from', 'message', 'antwort', 'date', 'edit'];
  public sFilter: string = "";
  public bShowAllTable: Boolean = true;

  ngOnInit() {
    // Kein init laden
    this.showAllTable();
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.displayedColumns = ['mid', 'chatid', 'type', 'from', 'message', 'antwort', 'date', 'edit'];
    } else {
      this.displayedColumns = ['type', 'chatid', 'message', 'antwort', 'date', 'edit'];
    }
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public loadAll(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.dataSource = null;

    this.rest.get('telegram/log').then((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      warten.close();
    });
  }

  public loadExt(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.dataSource = null;
    this.rest.get('telegram/logext').then((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      warten.close();
    });
  }

  public openCleanLogDialog(): void {
    var e: any = {};
    e.infotext = 'Soll das Log aufgeräumt werden? Dabei werden viele Einträge gelöscht!'
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: e,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.dataSource = null;
        this.cleanLog();
      }
    });
  }

  private cleanLog(): void {
    this.rest.delete('telegram/cleanLog').then((resdata: any) => {
      var e: any = {};
      e.infotext = 'Löschen ist fertig. Liste neu laden?'

      const dialogRef = this.dialog.open(DialogJaNeinComponent, {
        width: '250px',
        height: '250px',
        data: e,
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.loadAll();
        }
      });

    });
  }

  public openDelItemDialog(e: any): void {
    e.infotext = "Wirklich löschen der id " + e.mid + "?";
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: e,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.delItem(result);
      }
    });
  }

  private delItem(e: any): void {
    this.rest.delete('telegram/delLog/' + e.mid).then((resdata: any) => {

      e.infotext = "Logs neu laden?";
      const dialogRef = this.dialog.open(DialogJaNeinComponent, {
        width: '250px',
        height: '250px',
        data: e,
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.dataSource = null;
        }
      });

    });
  }
}
