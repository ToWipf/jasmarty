import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { DaylogDay } from 'src/app/datatypes';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-daylogKalender',
  templateUrl: './daylogKalender.component.html',
  styleUrls: ['./daylogKalender.component.less']
})
export class DaylogKalenderComponent implements OnInit {

  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  public sFilterYYYY: number = undefined;
  public sFilterMON: number = undefined;
  public sFilerMONasText: string = "";
  public kale: string[] = [];

  ngOnInit(): void {
    this.initFilter();
    this.loadDays();
  }

  private initFilter(): void {
    this.sFilterYYYY = (new Date(Date.now()).getFullYear());
    this.sFilterMON = (new Date(Date.now()).getMonth() + 1);
  }

  public loadDays(): void {
    this.kale = new Array(37).fill("-");
    const warten = this.dialog.open(DialogWartenComponent, {});

    let sq = this.sFilterYYYY + "-" + this.serviceWipf.pad((this.sFilterMON), 2);
    this.sFilerMONasText = new Date(0,this.sFilterMON, 0).toLocaleDateString('de-de', { month: 'long' });

    if (sq.length != 0) {
      this.rest.get('daylog/day/getAllByDateQuery/' + sq).then((resdata: DaylogDay[]) => {
        resdata.forEach((d: DaylogDay) => {
          d.extrafeld_wochentag = new Date(d.date).toLocaleDateString('de-de', { weekday: 'short' });
        });
        // den Wochentag des 1. eines Monats
        var erstWochentag = new Date(this.sFilterYYYY, this.sFilterMON-1, 1).getDay();
        // 0=Son 1=Mo wird zu 1=Mo 2=Di
        erstWochentag = (erstWochentag + 6) % 7;

        // Bug in Javascript? Das Monat ist um 1 höher wie bei "erstWochentag"?
        var tageImMonat = new Date(this.sFilterYYYY, this.sFilterMON , 0).getDate();

        for (var dayNr = 1; dayNr <= tageImMonat; dayNr++) {

          var inhaltDesTages = "(" + (dayNr) + ") ";
          resdata.forEach((d: DaylogDay) => {
            if (new Date(d.date).getDate() === dayNr) {
              inhaltDesTages += d.tagestext;
            }
          })

          this.kale[dayNr + erstWochentag -1] = inhaltDesTages;
        }

        warten.close();
      });
    } else {
      console.error("EE");
    }
  }

  public changeMonat(vorRueck: boolean) {
    if (vorRueck) {
      if (this.sFilterMON > 1) {
        this.sFilterMON--;
      }
    } else {
      if (this.sFilterMON < 12) {
        this.sFilterMON++;
      }
    }
    this.loadDays();
  }

  public changeYYYY(vorRueck: boolean) {
    if (vorRueck) {
      this.sFilterYYYY--;
    }
    else {
      this.sFilterYYYY++;
    }
    this.loadDays();
  }

}