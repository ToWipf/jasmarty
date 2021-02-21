import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { WipfUser } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-wipfuservw',
  templateUrl: './wipfUserVw.component.html',
  styleUrls: ['./wipfUserVw.component.less'],
})
export class WipfUserVwComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['username', 'role', 'telegramid', 'button'];
  public wuArray: WipfUser[] = [];

  ngOnInit() {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.wuArray = [];

    this.http.get(this.rest.gethost() + 'wipfuservw/getAll').subscribe((resdata: WipfUser[]) => {
      resdata.forEach((element) => {

        this.wuArray.push(element);

      });
      this.dataSource = new MatTableDataSource(this.wuArray);
      this.dataSource.sort = this.sort;
      warten.close();
    });
  }

  public newItem(): void {
    let td: WipfUser = {};
    td.role = 'user';
    this.openDialog(td);
  }

  private saveWipfUser(item: WipfUser): void {
    this.http.post(this.rest.gethost() + 'wipfuservw/createOrUpdate', item).subscribe((resdata: any) => {
      this.load();
    });
  }

  public openDialog(item: WipfUser): void {
    const edititem: WipfUser = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(WipfUserVWComponentDialogComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: WipfUser) => {
      if (result) {
        this.saveWipfUser(result);
      }
    });
  }
}

@Component({
  selector: 'app-wipfuservw-dialog',
  templateUrl: './wipfUserVw.dialog.html',
})
export class WipfUserVWComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<WipfUserVWComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: WipfUser) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
