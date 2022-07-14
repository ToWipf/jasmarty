import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { KeyValEntry } from 'src/app/datatypes';
import { DialogJaNeinComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-eisenbahn-mitlesen',
  templateUrl: './eisenbahn-mitlesen.component.html',
  styleUrls: ['./eisenbahn-mitlesen.component.less'],
})
export class EisenbahnMitlesenComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['key', 'val'];
  public itemarry: KeyValEntry[] = [];
  public sFilter: String = "";
  public bRun: Boolean = false;

  ngOnInit() {
  }

  public start(): void {
    this.rest.get('eisenbahn/mitlesen/start').then((resdata) => {
      console.log(resdata);
      this.bRun = true;
      this.loopResfresh();
    });
  }

  public stop(): void {
    this.bRun = false;
    this.rest.get('eisenbahn/mitlesen/stop').then((resdata) => {
      console.log(resdata);
    });
  }

  public connect(): void {
    this.rest.get('eisenbahn/mitlesen/connect').then((resdata) => {
      if (resdata.ok == "true") {
        resdata.infotext = "Verbindung Ok, Starten?";
        const dialogRef = this.dialog.open(DialogJaNeinComponent, {
          width: '250px',
          height: '250px',
          data: resdata,
        });

        dialogRef.afterClosed().subscribe((result) => {
          if (result) {
            this.start();
          }
        });
      }
    });
  }

  public reloadList(): void {
    this.rest.getNoWarten('eisenbahn/mitlesen/list').then((resdata: KeyValEntry[]) => {
      this.itemarry = resdata;

      this.dataSource = new MatTableDataSource(this.itemarry);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  private loopResfresh(): void {
    if (this.bRun) {
      setTimeout(() => {
        this.loopResfresh();
      }, 150);
      this.reloadList();
    }
  }

}
