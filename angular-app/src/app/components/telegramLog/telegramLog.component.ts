import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { Telegram } from 'src/app/datatypes';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { JaNeinDialogComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-telegram-log',
  templateUrl: './telegramLog.component.html',
  styleUrls: ['./telegramLog.component.less'],
})
export class TelegramLogComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['mid', 'chatid', 'type', 'from', 'message', 'antwort', 'date', 'edit'];

  ngOnInit() {
    // Kein init laden
  }

  public loadAll(): void {
    this.dataSource = null;
    this.http.get(this.rest.gethost() + 'telegram/log').subscribe((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
    });
  }

  public loadExt(): void {
    this.dataSource = null;
    this.http.get(this.rest.gethost() + 'telegram/logext').subscribe((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
    });
  }

  public openDelItemDialog(e: any): void {
    e.infotext = "Wirklich löschen der id " + e.mid + " ?";
    const dialogRef = this.dialog.open(JaNeinDialogComponent, {
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
    this.http.delete(this.rest.gethost() + 'telegram/delLog/' + e.mid).subscribe((resdata: any) => {

      e.infotext = "Logs neu laden ?";
      const dialogRef = this.dialog.open(JaNeinDialogComponent, {
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

