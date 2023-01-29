import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogInfoContent, Telegram } from 'src/app/datatypes';
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
  public bShowAllTableColumns: Boolean = true;

  ngOnInit() {
    // Kein init laden
    this.showAllTableColumns();
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
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
    var dic: DialogInfoContent = {
      infotext: 'Soll das Log aufgeräumt werden?',
      infotext2: ' Dabei werden alle Admin Einträge gelöscht!'
    };
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: dic,
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
      var dic: DialogInfoContent = {
        infotext: 'Löschen abgeschlossen',
        infotext2: ' Liste neu laden?'
      };

      const dialogRef = this.dialog.open(DialogJaNeinComponent, {
        minWidth: '200px',
        minHeight: '150px',
        data: dic,
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
    e.infotext2 = e.message + ' | ' + e.antwort;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
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
        minWidth: '200px',
        minHeight: '150px',
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
