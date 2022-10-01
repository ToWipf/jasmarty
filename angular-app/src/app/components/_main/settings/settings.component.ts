import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { KeyValEntry } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatSort } from '@angular/material/sort';
import { ServiceVersion } from 'src/app/service/serviceVersion';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.less'],
})
export class SettingsComponent implements OnInit {
  constructor(public dialog: MatDialog, public rest: ServiceRest, public serviceWipf: ServiceWipf, public serviceVersion: ServiceVersion) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['key', 'val', 'button'];
  public bShowWarning: boolean = false;
  public sFilter: string = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('basesettings/getAll').then((resdata: KeyValEntry[]) => {
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
    let e: KeyValEntry = {};
    this.openDialog(e);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.key;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('basesettings/delete/' + item.key).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: KeyValEntry): void {
    this.rest.post('basesettings/save', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: KeyValEntry): void {
    const edititem: KeyValEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(SettingsComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '300px',
      minHeight: '250px',
    });

    dialogRef.afterClosed().subscribe((result: KeyValEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

  public stopApp(): void {
    this.rest.post('wipf/stop', '').then((resdata: any) => {
    });
  }

}

@Component({
  selector: 'app-settings-dialog',
  templateUrl: './settings.dialog.html',
})
export class SettingsComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<SettingsComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: KeyValEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
