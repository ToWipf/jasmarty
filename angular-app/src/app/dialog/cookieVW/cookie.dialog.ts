import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { KeyValEntry } from 'src/app/datatypes';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { SharedMaterialModule } from '../../shared/shared-material.module';

@Component({
  selector: 'app-cookie',
  templateUrl: './cookie.dialog.html',
  styleUrls: ['./cookie.dialog.less'],
  standalone: true,
  imports: [CommonModule, SharedMaterialModule]
})
export class CookieDialogComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<CookieDialogComponent>, public dialog: MatDialog, public serviceWipf: ServiceWipf) { }


  public dataSource: MatTableDataSource<KeyValEntry> = new MatTableDataSource();
  public pageurl: string = "";
  public displayedColumns: string[] = ['key', 'value', 'button'];
  public inData: KeyValEntry = { key: "", value: "" };

  ngOnInit(): void {
    this.loadCookies();
    this.pageurl = window.location.href;
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public loadCookies(): void {

    const output: KeyValEntry[] = [];
    document.cookie.split(/\s*;\s*/).forEach((pair) => {
      if (!pair.includes('=')) {
        return;
      }
      const name = decodeURIComponent(pair.substring(0, pair.indexOf('=')));
      const value = decodeURIComponent(pair.substring(pair.indexOf('=') + 1));
      output.push({ key: name, value: value });
    });

    this.dataSource = new MatTableDataSource(output);
  }

  public newItem(): void {
    let e: KeyValEntry = {};
    this.openDialog(e);
  }

  public openDialog(item: KeyValEntry): void {
    const edititem: KeyValEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(CookieAddDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: KeyValEntry) => {
      if (result) {
        this.setCookie(result.key ?? '', result.value ?? '');
      }
    });
  }

  public setCookie(cname: string, value: string): void {
    var xnow = new Date();
    var time = xnow.getTime();
    var expireTime = time + 1000 * 36000 * 9999;
    xnow.setTime(expireTime);
    document.cookie = cname + '=' + value + ';expires=' + xnow.toUTCString() + ';path=/;SameSite=Strict';

    this.loadCookies();
  }

  public deleteItem(cname: string): void {
    document.cookie = cname + '=DEL; expires=' + new Date() + ';path=/;SameSite=Strict';

    this.loadCookies();
  }

}

@Component({
  selector: 'app-cookie-add-dialog',
  templateUrl: './cookie.add.dialog.html',
  standalone: true,
  imports: [CommonModule, SharedMaterialModule]
})
export class CookieAddDialogComponent {
  constructor(public dialogRef: MatDialogRef<CookieAddDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: KeyValEntry) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}