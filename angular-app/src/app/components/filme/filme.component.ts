import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { FilmEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-filme',
  templateUrl: './filme.component.html',
  styleUrls: ['./filme.component.less'],
})
export class FilmeComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  //public displayedColumns: string[] = ['id', 'titel', 'art', 'gesehen', 'bewertung', 'infotext', 'date', 'editby', 'button'];
  public displayedColumns: string[] = ['titel', 'art', 'gesehen', 'bewertung', 'infotext', 'button'];
  public farry: FilmEntry[] = [];
  private nextId: number;

  ngOnInit() {
    this.load();
  }

  private load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.http.get(this.rest.gethost() + 'filme/getAll').subscribe((resdata: FilmEntry[]) => {
      this.farry = resdata;
      this.dataSource = new MatTableDataSource(this.farry);
      this.dataSource.sort = this.sort;
      this.nextId = this.getNextId();
      warten.close();
    });
  }

  public newItem(): void {
    var td: FilmEntry = {};

    td.id = this.nextId;
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
    // TODO: ADD nachfragen dialog
    this.http.delete(this.rest.gethost() + 'filme/delete/' + item.id).subscribe((resdata: any) => {
      this.load();
    });
  }

  private getNextId(): number {
    var nextId: number = 0;
    this.farry.forEach((item: FilmEntry) => {
      if (item.id > nextId) {
        nextId = item.id;
      }
    });
    return nextId * 1 + 1;
  }

  private save(item: FilmEntry): void {
    this.http.post(this.rest.gethost() + 'filme/save', item).subscribe((resdata: any) => {
      this.load();
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
  constructor(public dialogRef: MatDialogRef<FilmeComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: FilmEntry) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
