import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { WipfUser } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
    selector: 'app-wipfuservw',
    templateUrl: './wipfUserVw.component.html',
    styleUrls: ['./wipfUserVw.component.less'],
    standalone: false
})
export class WipfUserVwComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'username', 'role', 'button'];
  public sFilter: string = "";

  ngOnInit() {
    this.load();
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('wipfuservw/getAll').then((resdata: WipfUser[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      warten.close();
    });
  }

  public newItem(): void {
    let td: WipfUser = {};
    td.role = 'user';
    this.openDialogEdit(td);
  }

  private saveWipfUser(item: WipfUser): void {
    this.rest.post('wipfuservw/createOrUpdate', item).then((resdata: any) => {
      this.load();
    });
  }

  public openDialogEdit(item: WipfUser): void {
    const edititem: WipfUser = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(WipfUserVWComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: WipfUser) => {
      if (result) {
        this.saveWipfUser(result);
      }
    });
  }

  public deleteUser(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.username;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('wipfuservw/delete/' + item.username).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

}

@Component({
    selector: 'app-wipfuservw-dialog',
    templateUrl: './wipfUserVw.dialog.html',
    standalone: false
})
export class WipfUserVWComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<WipfUserVWComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: WipfUser) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
