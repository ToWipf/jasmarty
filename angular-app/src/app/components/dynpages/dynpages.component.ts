import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { DynpageEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-dynpages',
  templateUrl: './dynpages.component.html',
  styleUrls: ['./dynpages.component.less'],
})
export class DynpagesComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'html', 'script', 'style', 'rechte', 'live', 'button'];
  public dynpagearry: DynpageEntry[] = [];
  public bLive: boolean = true;
  public bShowWarning: boolean = false;
  public sFilter: String = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.dynpagearry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.http.get(this.rest.gethost() + 'dynpages/getAll').subscribe((resdata: DynpageEntry[]) => {
      resdata.forEach((element) => {

        if (element.live && this.bLive) {
          this.dynpagearry.push(element);
        }
      });

      this.dataSource = new MatTableDataSource(this.dynpagearry);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });
  }

  public applyFilter() {
    this.dataSource.filter = this.sFilter.trim();
  }

  public newItem(): void {
    let td: DynpageEntry = {};
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
        this.http.delete(this.rest.gethost() + 'dynpages/delete/' + item.id).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: DynpageEntry): void {
    this.http.post(this.rest.gethost() + 'dynpages/save', item).subscribe((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: DynpageEntry): void {
    const edititem: DynpageEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(DynpagesComponentDialogComponent, {
      width: '350px',
      height: '350px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: DynpageEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }
}

@Component({
  selector: 'app-dynpages-dialog',
  templateUrl: './dynpages.dialog.html',
})
export class DynpagesComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<DynpagesComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DynpageEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
