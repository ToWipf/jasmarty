import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'eventtext', 'active', 'button'];
  public rndarry: RndEvent[] = [];
  private nextId: number = 0;
  public bShowWarning: boolean = false;
  public sFilter: String = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.rndarry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.http.get(this.rest.gethost() + 'rndevent/getAll').subscribe((resdata: RndEvent[]) => {
      resdata.forEach((element) => {
        this.rndarry.push(element);
      });

      this.dataSource = new MatTableDataSource(this.rndarry);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.getNextId();
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

    td.id = this.nextId;
    td.eventtext = '';
    td.active = true;

    this.openDialog(td);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.data;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'rndevent/delete/' + item.id).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private getNextId(): void {
    let nextIdTmp: number = 0;
    // Auch falls jetzt weginger in der Liste ist, die bisher höchste id nehmen
    if (this.nextId != 0) {
      nextIdTmp = this.nextId;
    }
    this.rndarry.forEach((item: RndEvent) => {
      if (item.id > nextIdTmp) {
        nextIdTmp = item.id;
      }
    });
    this.nextId = nextIdTmp * 1 + 1;
  }

  private save(item: RndEvent): void {
    this.http.post(this.rest.gethost() + 'rndevent/save', item).subscribe((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: RndEvent): void {
    const edititem: RndEvent = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(RndEventComponentDialogComponent, {
      width: '350px',
      height: '360px',
      data: edititem,
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
