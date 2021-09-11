import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-daylog',
  templateUrl: './daylog.component.html',
  styleUrls: ['./daylog.component.less'],
})
export class DayLogComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }
  
  public dataSource;

  ngOnInit() {
    this.load();
  }

  public load(): void {
    // this.toarry = [];
    // const warten = this.dialog.open(DialogWartenComponent, {});

    // this.http.get(this.rest.gethost() + 'todolist/getAll').subscribe((resdata: TodoEntry[]) => {
    //   this.toarry = resdata;

    //   this.dataSource = new MatTableDataSource(this.toarry);
    //   this.dataSource.sort = this.sort;
    //   this.dataSource.filter = this.sFilter.trim();
    //   this.getNextId();
    //   this.applyFilter();
    //   warten.close();
    // });
  }

}
