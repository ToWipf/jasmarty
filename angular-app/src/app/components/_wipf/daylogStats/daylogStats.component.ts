import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-daylogStats',
  templateUrl: './daylogStats.component.html',
  styleUrls: ['./daylogStats.component.less']
})
export class DaylogStatsComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public statsDataSource;
  public statsarry = [];
  public sFilter: String = "";
  public displayedColumns: string[] = [];
  public bShowAllTable: boolean = true;
  public bvData = [];



  ngOnInit(): void {
    this.load();
    this.showAllTable();
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.displayedColumns = ['text', 'anz', 'first_id', 'first_dateid', 'frist_typ'];
    } else {
      this.displayedColumns = ['anz', 'text'];
    }
  }

  public load(): void {
    this.statsarry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('daylog/event/getStats').then((resdata: StatsEntry[]) => {
      resdata.forEach((element) => {
        this.statsarry.push(element);
      });

      this.statsDataSource = new MatTableDataSource(this.statsarry);
      this.statsDataSource.sort = this.sort;
      this.statsDataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });

    this.rest.get('daylog/event/getAllById/8').then((resdata: any[]) => {
      resdata.forEach((element: any) => {
        let nVal = element.text.match(/\d+/)[0]; // Nur die erste Zahl ausgeben
        let data = { name: element.datum, orgname: element.dateid, value: nVal, orgvalue: element.text };
        this.bvData.push(data);
      });
      console.log(this.bvData);
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.statsDataSource.filter = this.sFilter.trim();
    });
  }

  public getNamedColor = (statName: string) => {
    return 'red';
  }

}

export interface StatsEntry {
  anz?: number;
  first_id?: number;
  first_dateid?: number;
  frist_typ?: number;
  text?: string;
}
