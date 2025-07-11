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
    standalone: false
})
export class SettingsComponent implements OnInit {
  constructor(public dialog: MatDialog, public rest: ServiceRest, public serviceWipf: ServiceWipf, public serviceVersion: ServiceVersion) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['key', 'value', 'button'];
  public bShowWarning: boolean = false;
  public sFilter: string = "";
  public bSicherheitNochmalKlicken: boolean = false;

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

  public loadPwaManifest(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('app/manifest.webmanifest').then((res: any) => {
      var x = { infotext: "PWA Manifest", infotext2: JSON.stringify(res) };
      warten.close();
      this.dialog.open(DialogJaNeinComponent, {
        minWidth: '200px',
        minHeight: '150px',
        data: x,
      });
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
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.key;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
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
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: KeyValEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

  public stopApp(): void {
    if (this.bSicherheitNochmalKlicken) {
      this.rest.post('wipf/stop', '').then((resdata: any) => {
      });
    } else {
      this.bSicherheitNochmalKlicken = true;
    }
  }

}

@Component({
    selector: 'app-settings-dialog',
    templateUrl: './settings.dialog.html',
    standalone: false
})
export class SettingsComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<SettingsComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: KeyValEntry) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
