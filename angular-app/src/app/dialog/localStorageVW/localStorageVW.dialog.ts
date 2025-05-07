import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { KeyValEntry } from 'src/app/datatypes';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-localStorageVW',
  templateUrl: './localStorageVW.dialog.html',
  styleUrls: ['./localStorageVW.dialog.less']
})
export class LocalStorageDialogComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<LocalStorageDialogComponent>, public dialog: MatDialog, public serviceWipf: ServiceWipf) { }


  public dataSource;
  public displayedColumns: string[] = ['key', 'value', 'button'];
  public inData: KeyValEntry = { key: "", value: "" };

  ngOnInit(): void {
    this.loadLocalStorage();
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public loadLocalStorage(): void {
    let output = [];
    for (let i = 0; i < localStorage.length; i++) {
      let key = localStorage.key(i);
      if (key) {
        let value = localStorage.getItem(key);
        output.push({ key: key, value: value });
      }
    }
    this.dataSource = new MatTableDataSource(output);
  }

  public openDialog(item: KeyValEntry): void {
    const edititem: KeyValEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(LocalStorageVWAddDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: KeyValEntry) => {
      if (result) {
        this.setLocalStorageItem(result.key, result.value);
      }
    });
  }
  public newItem(): void {
    let e: KeyValEntry = {};
    this.openDialog(e);
  }

  public editItem(e: KeyValEntry): void {
    this.openDialog(e);
  }

  public setLocalStorageItem(cname: string, value: string): void {
    localStorage.setItem(cname, value);
    this.loadLocalStorage();
  }

  public deleteItem(cname: string): void {
    localStorage.removeItem(cname);
    this.loadLocalStorage();
  }

}

@Component({
  selector: 'app-localStorageVW-add-dialog',
  templateUrl: './localStorageVW.add.dialog.html',
})
export class LocalStorageVWAddDialogComponent {
  constructor(public dialogRef: MatDialogRef<LocalStorageVWAddDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: KeyValEntry) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}