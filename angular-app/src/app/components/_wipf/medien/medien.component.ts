import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MedienEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-medien',
  templateUrl: './medien.component.html',
  styleUrls: ['./medien.component.less'],
})
export class MedienComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public bShowAllTableColumns: boolean = true;
  public displayedColumns: string[] = [];
  public sFilter: string = "";
  public listTypFilter: string[] = ["Serie", "Film", "Buch", "Brettspiel", "Band"];

  ngOnInit() {
    this.load();
    this.showAllTableColumns();
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['id', 'typ', 'titel', 'art', 'gesehen', 'bewertung', 'infotext', 'date', 'editby', 'button'];
    } else {
      this.displayedColumns = ['typ', 'titel', 'art', 'gesehen', 'infotext', 'button'];
    }
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('medien/getAll').then((resdata: MedienEntry[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      warten.close();
    });
  }

  public newItem(): void {
    var td: MedienEntry = {};
    td.art = "";
    td.bewertung = 0;
    td.gesehenDate = Math.round(Date.now() / 1000);
    td.infotext = "";
    td.titel = "";
    td.date = Math.round(Date.now() / 1000);
    td.editby = 'web';
    td.typ = "";
    this.openDialog(td);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.titel;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('medien/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: MedienEntry): void {
    this.rest.post('medien/save', item).then((resdata: any) => {
      this.load();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      if (this.sFilter) {
        this.dataSource.filter = this.sFilter.trim();
      } else {
        this.dataSource.filter = "";
      }
    });
  }

  public openDialog(item: MedienEntry): void {
    const edititem: MedienEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(MedienComponentDialog, {
      data: edititem,
      autoFocus: true,
      minWidth: '300px',
      minHeight: '250px',
    });

    dialogRef.afterClosed().subscribe((result: MedienEntry) => {
      if (result) {
        result.editby = 'web';
        result.date = Math.round(Date.now() / 1000);
        this.save(result);
      }
    });
  }
}

@Component({
  selector: 'app-medien-dialog',
  templateUrl: './medien.dialog.html',
})
export class MedienComponentDialog {
  constructor(public dialogRef: MatDialogRef<MedienComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: MedienEntry) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
