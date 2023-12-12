import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AuthKey } from 'src/app/datatypes';
import { DialogWartenComponent, DialogJaNeinComponent } from 'src/app/dialog/main.dialog';
import { ServicAuthKey } from 'src/app/service/serviceAuthKey';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-authKey',
  templateUrl: './authKey.component.html',
  styleUrls: ['./authKey.component.less']
})
export class AuthKeyComponent implements OnInit {
  
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public serviceAuthKey: ServicAuthKey) { }
  
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  
  public dataSource;
  public displayedColumns: string[] = ['id', 'key', 'info', 'access', 'button'];
  public sFilter: string = "";
  public mykey: string = "";

  ngOnInit(): void {
    this.load();
    this.mykey = this.serviceAuthKey.getAuthKey();
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('authkey/getAll').then((resdata: AuthKey[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      warten.close();
    });
  }

  public newItem(): void {
    let td: AuthKey = {};
    td.access = false;
    this.openDialogEdit(td);
  }

  private save(item: AuthKey): void {
    this.rest.post('authkey/createOrUpdate', item).then((resdata: any) => {
      this.load();
    });
  }

  public openDialogEdit(item: AuthKey): void {
    const edititem: AuthKey = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(AuthKeyComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: AuthKey) => {
      if (result) {
        this.save(result);
      }
    });
  }

  public delete(item: any): void {
    item.infotext = "Wirklich löschen?"; 
    item.infotext2 = item.info;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('authkey/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }
}

@Component({
  selector: 'app-authkey-dialog',
  templateUrl: './authKey.dialog.html',
})
export class AuthKeyComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<AuthKeyComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: AuthKey) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
