import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { FilmEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-filme',
  templateUrl: './filme.component.html',
  styleUrls: ['./filme.component.less'],
})
export class FilmeComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'titel', 'art', 'gesehen', 'bewertung', 'infotext', 'date', 'editby', 'button'];
  //public displayedColumns: string[] = ['titel', 'art', 'gesehen', 'bewertung', 'infotext', 'button'];
  public sFilter: string = "";

  ngOnInit() {
    this.load();
  }

  private load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('filme/getAll').then((resdata: FilmEntry[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      warten.close();
    });
  }

  public newItem(): void {
    var td: FilmEntry = {};
    td.art = "";
    td.bewertung = 0;
    td.gesehenDate = Math.round(Date.now() / 1000);
    td.infotext = "";
    td.titel = "";
    td.date = Math.round(Date.now() / 1000);
    td.editby = 'web';
    this.openDialog(td);
  }

  public deleteItem(item: FilmEntry): void {
    item.infotext = "Wirklich löschen? " + item.titel;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('filme/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private save(item: FilmEntry): void {
    this.rest.post('filme/save', item).then((resdata: any) => {
      this.load();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public openDialog(item: FilmEntry): void {
    const edititem: FilmEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(FilmeComponentDialog, {
      width: '350px',
      height: '550px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: FilmEntry) => {
      if (result) {
        result.editby = 'web';
        result.date = Math.round(Date.now() / 1000);
        this.save(result);
      }
    });
  }
}

@Component({
  selector: 'app-filme-dialog',
  templateUrl: './filme.dialog.html',
})
export class FilmeComponentDialog {
  constructor(public dialogRef: MatDialogRef<FilmeComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: FilmEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
