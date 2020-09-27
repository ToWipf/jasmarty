import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { Telegram } from 'src/app/datatypes';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-telegram-log',
  templateUrl: './telegramLog.component.html',
  styleUrls: ['./telegramLog.component.less'],
})
export class TelegramLogComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['mid', 'chatid', 'type', 'from', 'message', 'antwort', 'date'];

  ngOnInit() {
    // Kein init laden
  }

  public loadAll(): void {
    this.http.get(this.rest.gethost() + 'telegram/log').subscribe((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
    });
  }

  public loadExt(): void {
    this.http.get(this.rest.gethost() + 'telegram/logext').subscribe((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
    });
  }

}
