import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DaylogDay, DaylogEvent } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-daylog',
  templateUrl: './daylog.component.html',
  styleUrls: ['./daylog.component.less'],
})
export class DayLogComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public daylistDataSource;
  public daydetaillistDataSource;
  public daylist: DaylogDay[] = [];
  public daydetaillist: DaylogEvent[] = [];
  public userid = 0;
  public daylistDisplayedColumns: string[] = ['id', 'date', 'tagestext', 'userid', 'button'];
  public daydetaillistDisplayedColumns: string[] = ['id', 'date', 'tagestext', 'userid', 'button'];

  ngOnInit() {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.daylist = [];
    this.daydetaillist = [];
    
    this.http.get(this.rest.gethost() + 'daylog/day/getAll/' + this.userid).subscribe((resdata: DaylogDay[]) => {
      this.daylist = resdata;

      this.daylistDataSource = new MatTableDataSource(this.daylist);
      this.daylistDataSource.sort = this.sort;
      warten.close();
    });

    // TODO: laden detail
  }

  public openDay(d: DaylogDay): void {
    console.log(d);
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.data;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item.date,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.http.delete(this.rest.gethost() + 'daylog/day/delete/' + item.id).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

}
