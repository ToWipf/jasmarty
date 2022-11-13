import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ListeEntry } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-liste',
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.less']
})
export class ListeComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public bShowWarning: boolean = false;
  public displayedColumns: string[] = [];
  public sFilter: string = "";
  public bShowAllTableColumns: boolean = true;


  ngOnInit() {
    this.load();
    this.showAllTableColumns();
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['id', 'type', 'data', 'date', 'button'];
    } else {
      this.displayedColumns = ['type', 'data', 'button'];
    }
  }
  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('liste/getAll').then((resdata: ListeEntry[]) => {
      

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
    let td: ListeEntry = {};
    this.openDialog(td);
  }

  private save(item: ListeEntry): void {
    this.rest.post('liste/save', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
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
        this.rest.delete('liste/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  public openDialog(item: ListeEntry): void {
    const edititem: ListeEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(ListeComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '300px',
      minHeight: '250px',
    });

    dialogRef.afterClosed().subscribe((result: ListeEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

}

@Component({
  selector: 'app-liste-dialog',
  templateUrl: './liste.dialog.html',
})
export class ListeComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<ListeComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ListeEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

