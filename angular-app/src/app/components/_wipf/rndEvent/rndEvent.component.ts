import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { RndEvent } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-rndEvent',
  templateUrl: './rndEvent.component.html',
  styleUrls: ['./rndEvent.component.less'],
})
export class RndEventComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'eventtext', 'active', 'button'];
  public bShowWarning: boolean = false;
  public sFilter: string = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('rndevent/getAll').then((resdata: RndEvent[]) => {
      this.dataSource = new MatTableDataSource(resdata);
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

  public newItem(): void {
    let td: RndEvent = {};

    td.eventtext = '';
    td.active = true;

    this.openDialog(td);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.eventtext;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('rndevent/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: RndEvent): void {
    this.rest.post('rndevent/save', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: RndEvent): void {
    const edititem: RndEvent = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(RndEventComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '300px',
      minHeight: '250px',
    });

    dialogRef.afterClosed().subscribe((result: RndEvent) => {
      if (result) {
        this.save(result);
      }
    });
  }
}

@Component({
  selector: 'app-rndEvent-dialog',
  templateUrl: './rndEvent.dialog.html',
})
export class RndEventComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<RndEventComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: RndEvent) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
