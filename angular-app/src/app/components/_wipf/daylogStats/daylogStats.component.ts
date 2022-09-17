import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { DaylogType } from 'src/app/datatypes';

@Component({
  selector: 'app-daylogStats',
  templateUrl: './daylogStats.component.html',
  styleUrls: ['./daylogStats.component.less']
})
export class DaylogStatsComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public statsDataSource;
  public sFilter: string = "";
  public displayedColumns: string[] = [];
  public bShowAllTable: boolean = true;
  public bvDataForDateChart = [];
  public bvDataForWochentagVorkomnisseChart = [];
  public typelistForSelect: DaylogType[] = [];
  public selectedTypes: DaylogType[] = [];
  public diagramRawData: any[];

  ngOnInit(): void {
    this.showAllTableColumns();
    this.loadAllTypes();
  }

  public load(): void {
    this.loadTabelle();
    this.loadDiagramme();
  }

  public showAllTableColumns(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.displayedColumns = ['text', 'anz', 'first_id', 'first_dateid', 'frist_typ'];
    } else {
      this.displayedColumns = ['anz', 'text'];
    }
  }

  public loadTabelle(): void {
    if (this.getSelectedTypes().length > 0) {
      const warten = this.dialog.open(DialogWartenComponent, {});
      this.rest.get('daylog/event/getStats/' + this.getSelectedTypes()).then((resdata: StatsEntry[]) => {
        this.statsDataSource = new MatTableDataSource(resdata);
        this.statsDataSource.sort = this.sort;
        this.statsDataSource.filter = this.sFilter.trim();
        this.applyFilter();
        warten.close();
      });
    } else {
      this.statsDataSource = new MatTableDataSource();
    }
  }

  public loadDiagramme(): void {
    if (this.getSelectedTypes().length > 0) {
      this.rest.get('daylog/event/getAllById/' + this.getSelectedTypes()).then((resdata: any[]) => {
        this.diagramRawData = resdata;
        this.genDiagramme(this.diagramRawData);
      });
    } else {
      // Diagramme nur leeren
      this.bvDataForDateChart = [];
      this.bvDataForWochentagVorkomnisseChart = [];
    }
  }

  private genDiagramme(indata: any[]): void {
    this.bvDataForDateChart = [];
    this.bvDataForWochentagVorkomnisseChart = [];
    indata.forEach((element: any) => {
      let wochentag = new Date(element.date).toLocaleDateString('de-de', { weekday: 'short' });
      let nVal = this.textToDigNumber(element.text);
      this.bvDataForDateChart.push({ name: element.date, value: nVal, wtag: wochentag });
    });

    // Zweites Diagramm
    let aWochentage = ["Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"];
    aWochentage.forEach((wotag) => {
      let i = this.bvDataForDateChart.filter((val) => val.wtag === wotag).length;
      this.bvDataForWochentagVorkomnisseChart.push({ name: wotag, value: i });
    });
  }

  private textToDigNumber(input: any): number {
    if (input == null) {
      return -1;
    } else if (input === "true") {
      return 1;
    } else if (input === "false") {
      return 0;
    } else if (this.serviceWipf.isNumber(input)) {
      // Wenn es eine Zahl ist
      return input;
    } else if (this.serviceWipf.startsWithNumber(input)) {
      // Keine Zahl - bei text mit Zahl zu beginn - nur die Zahl ausgeben
      return input.match(/\d+/)[0]; // Nur die erste Zahl ausgeben
    } else {
      return -1;
    }
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.statsDataSource.filter = this.sFilter.trim();
      var fileredDate = this.diagramRawData.filter((i) => {
        if (i.text) {
          return i.text.toLocaleLowerCase().includes(this.sFilter.toLocaleLowerCase());
        } else {
          // diser Fall sollte nicht passieren -> Im Fehlerfall ausfiltern
          return false;
        }
      });
      this.genDiagramme(fileredDate);
    });
  }

  public getNamedColor = (statName: string) => {
    return 'red';
  }

  public loadAllTypes(): void {
    this.rest.get('daylog/type/getAll').then((resdata: DaylogType[]) => {
      this.typelistForSelect = resdata;
    });
  }

  /**
  * Zusammenbau der Auswahlids
  * @returns 
  */
  private getSelectedTypes(): string {
    var tlist: string = "";
    this.selectedTypes.forEach((dt) => {
      if (tlist.length > 0) {
        tlist = tlist + ",";
      }
      tlist = tlist + dt.id;
    });
    return tlist;
  }
}

export interface StatsEntry {
  anz?: number;
  first_id?: number;
  first_dateid?: number;
  frist_typ?: number;
  text?: string;
}
