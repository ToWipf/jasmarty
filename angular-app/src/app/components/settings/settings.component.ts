import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { SettingEntry } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.less'],
})
export class SettingsComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['key', 'val', 'button'];
  public settingsearry: SettingEntry[] = [];
  public bShowWarning: boolean = false;
  public sFilter: String = "";


  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.settingsearry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.http.get(this.rest.gethost() + 'basesettings/getAll').subscribe((resdata: SettingEntry[]) => {
      this.settingsearry = resdata;

      this.dataSource = new MatTableDataSource(this.settingsearry);
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
    let e: SettingEntry = {};
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
        this.http.delete(this.rest.gethost() + 'basesettings/delete/' + item.key).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: SettingEntry): void {
    this.http.post(this.rest.gethost() + 'basesettings/save', item).subscribe((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: SettingEntry): void {
    const edititem: SettingEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(SettingsComponentDialogComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: SettingEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

}

@Component({
  selector: 'app-settings-dialog',
  templateUrl: './settings.dialog.html',
})
export class SettingsComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<SettingsComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: SettingEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
