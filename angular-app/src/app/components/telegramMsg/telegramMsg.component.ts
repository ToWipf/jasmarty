import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { Telegram } from 'src/app/datatypes';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-telegramMsg',
  templateUrl: './telegramMsg.component.html',
  styleUrls: ['./telegramMsg.component.less'],
})
export class TelegramMsgComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['mid', 'message', 'antwort', 'from', 'date', 'edit'];

  ngOnInit() {
    this.loadAllItems();
  }

  public editItem(t: Telegram): void {
    this.openDialog(t);
  }

  public newItem(): void {
    let t: Telegram = {};
    this.openDialog(t);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.message;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'telegram/delMsg/' + item.mid).subscribe((resdata: any) => {
          this.loadAllItems();
        });
      }
    });

  }

  private saveItem(t: Telegram): void {
    this.http.post(this.rest.gethost() + 'telegram/saveMsg', t).subscribe((resdata: any) => {
      this.loadAllItems();
    });
  }

  private loadAllItems(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.dataSource = null;
    this.http.get(this.rest.gethost() + 'telegram/msgall').subscribe((resdata: Telegram[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      warten.close();
    });
  }

  private openDialog(item: Telegram): void {
    const edititem: Telegram = this.serviceWipf.deepCopy(item);
    edititem.from = "web";

    const dialogRef = this.dialog.open(TelegramMsgComponentDialogComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: Telegram) => {
      if (result) {
        result.editby = 'web';
        result.date = Math.round(Date.now() / 1000);
        this.saveItem(result);
      }
    });
  }

}

@Component({
  selector: 'app-telegramMsg-dialog',
  templateUrl: './telegramMsg.dialog.html',
})
export class TelegramMsgComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<TelegramMsgComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: Telegram) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
