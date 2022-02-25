import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Telegram, TelegramUserCache } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-telegram-chat',
  templateUrl: './telegramChat.component.html',
  styleUrls: ['./telegramChat.component.less'],
})
export class TelegramChatComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) {

  }
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['chatid', 'msg', 'usercache', 'counter', 'button'];
  public tMsg: Telegram = { chatid: 0, message: '', type: 'website', editby: 'website' };
  public textOut: string = '-';
  public usercachearry: TelegramUserCache[] = [];
  public sFilter: string = "";

  ngOnInit(): void {
    this.load();
  }

  public load(): void {
    this.usercachearry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.http.get(this.rest.gethost() + 'telegram/usercache/getAll').subscribe((resdata: TelegramUserCache[]) => {
      this.usercachearry = resdata

      this.dataSource = new MatTableDataSource(this.usercachearry);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.chatid;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'telegram/usercache/delete/' + item.chatid).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

  // TestChat
  public send(): void {
    this.http.post(this.rest.gethost() + 'telegram/chat', this.tMsg).subscribe((resdata: any) => {
      this.tMsg.message = "";
      this.textOut = resdata.msg;
      this.load();
    });
  }

}
