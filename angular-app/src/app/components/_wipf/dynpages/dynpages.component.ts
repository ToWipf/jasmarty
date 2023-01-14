import { Component, OnInit, ViewChild, Inject } from '@angular/core';
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
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'html', 'script', 'style', 'rechte', 'live', 'button'];
  public bShowWarning: boolean = false;
  public sFilter: string = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('dynpages/getAll').then((resdata: DynpageEntry[]) => {
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
    let e: DynpageEntry = {};
    e.html = "";
    e.script = "";
    e.style = "";
    e.rechte = "NONE";
    e.live = false;
    this.openDialog(e);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.id;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('dynpages/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: DynpageEntry): void {
    this.rest.post('dynpages/save', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: DynpageEntry): void {
    const edititem: DynpageEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(DynpagesComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
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

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
