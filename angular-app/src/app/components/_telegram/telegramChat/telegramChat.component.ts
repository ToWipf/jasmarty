import { Component, OnInit, ViewChild } from '@angular/core';
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
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) {

  }
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = [];
  public tMsg: Telegram = { chatid: 0, message: '', type: 'website', editby: 'website' };
  public textOut: string = '-';
  public sFilter: string = "";
  public sTextOnlineTelegram: string;
  public sStatus: string;
  public bShowAllTableColumns: boolean = true;

  ngOnInit(): void {
    this.load();
    this.showAllTableColumns();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('teleusercache/getAll').then((resdata: TelegramUserCache[]) => {

      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['chatid', 'msg', 'usercache', 'counter', 'button'];
    } else {
      this.displayedColumns = ['chatid', 'msg', 'counter'];
    }
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.chatid;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('teleusercache/delete/' + item.chatid).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  // TestChat
  public sendLocal(): void {
    this.rest.post('telegram/chat', this.tMsg).then((resdata: any) => {
      this.tMsg.message = "";
      this.textOut = resdata.msg;
      this.load();
    });
  }

  public sendMsgToGroup(): void {
    if (this.sTextOnlineTelegram) {
      //TODO: escape input string
      this.rest.post('telegram/sendMsgToAdmin/' + this.sTextOnlineTelegram, null).then((resdata) => {
        this.sStatus = resdata.toString();
        this.sTextOnlineTelegram = null;
      });
    }
  }
}
