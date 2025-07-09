import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { TeleMsg } from 'src/app/datatypes';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
    selector: 'app-telegramMsg',
    templateUrl: './telegramMsg.component.html',
    styleUrls: ['./telegramMsg.component.less'],
    standalone: false
})
export class TelegramMsgComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[];
  public sFilter: string = "";
  public bShowAllTableColumns: Boolean = true;

  ngOnInit() {
    this.showAllTableColumns();
    this.loadAllItems();
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['id', 'frage', 'antwort', 'button'];
    } else {
      this.displayedColumns = ['frage', 'antwort', 'button'];
    }
  }

  public editItem(t: TeleMsg): void {
    this.openDialog(t);
  }

  public newItem(): void {
    let t: TeleMsg = {};
    this.openDialog(t);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.message;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('telemsg/delete/' + item.mid).then((resdata: any) => {
          this.loadAllItems();
        });
      }
    });

  }

  private saveItem(t: TeleMsg): void {
    t.frage = t.frage.toLowerCase();
    this.rest.post('telemsg/save', t).then((resdata: any) => {
      this.loadAllItems();
    });
  }

  private loadAllItems(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.dataSource = null;
    this.rest.get('telemsg/getall').then((resdata: TeleMsg[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      warten.close();
      this.applyFilter();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }
  private openDialog(item: TeleMsg): void {
    const edititem: TeleMsg = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(TelegramMsgComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: TeleMsg) => {
      if (result) {
        this.saveItem(result);
      }
    });
  }

}

@Component({
    selector: 'app-telegramMsg-dialog',
    templateUrl: './telegramMsg.dialog.html',
    standalone: false
})
export class TelegramMsgComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<TelegramMsgComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: TeleMsg) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
