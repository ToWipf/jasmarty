import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DaylogDay, DaylogEvent, DaylogType, KeyValEntry } from 'src/app/datatypes';
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
  public kalenderRawArray: KalDay[] = [];
  public kalenderShowArray: KalShowZelle[] = [];
  public typelistForEventFilter: DaylogType[] = [];
  public selectedEventTypeFilter: DaylogType = undefined;

  ngOnInit(): void {
    this.initFilter();
    this.loadTypeListForEventFilter();
    this.loadDays();
  }

  private initFilter(): void {
    this.sFilterYYYY = (new Date(Date.now()).getFullYear());
    this.sFilterMON = (new Date(Date.now()).getMonth() + 1);
  }

  public loadDays(): void {
    this.kalenderRawArray = new Array(37);
    const warten = this.dialog.open(DialogWartenComponent, {});

    let sq = this.sFilterYYYY + "-" + this.serviceWipf.pad((this.sFilterMON), 2);
    this.sFilerMONasText = new Date(0, this.sFilterMON, 0).toLocaleDateString('de-de', { month: 'long' });

    if (sq.length != 0) {
      this.rest.get('daylog/day/getAllByDateQuery/' + sq).then(async (resdata: DaylogDay[]) => {
        // resdata.forEach((d: DaylogDay) => {
        //   d.extrafeld_wochentag = new Date(d.date).toLocaleDateString('de-de', { weekday: 'short' });
        // });
        // den Wochentag des 1. eines Monats
        var erstWochentag = new Date(this.sFilterYYYY, this.sFilterMON - 1, 1).getDay();
        // 0=Son 1=Mo wird zu 1=Mo 2=Di
        erstWochentag = (erstWochentag + 6) % 7;

        // Bug in Javascript? Das Monat ist um 1 höher wie bei "erstWochentag"?
        var tageImMonat = new Date(this.sFilterYYYY, this.sFilterMON, 0).getDate();

        for (var dayNr = 1; dayNr <= tageImMonat; dayNr++) {
          resdata.forEach((d: DaylogDay) => {
            if (new Date(d.date).getDate() === dayNr) {
              // Tag vorhanden
              this.addToInhaltsarray(dayNr + erstWochentag - 1, dayNr, d);
            } else {
              // Tag gibt es nicht
              this.addToInhaltsarray(dayNr + erstWochentag - 1, dayNr, null);
            }
          })
        }

        warten.close();
        this.renderKalender();
      });
    }
  }

  public renderKalender(): void {
    this.kalenderShowArray = new Array(37).fill({ tagestext: "-" });

    this.kalenderRawArray.forEach((zelle: KalDay) => {
      var kdShowDay: KalShowZelle = {};
      kdShowDay.eventKV = [];
      kdShowDay.dayNr = zelle.dayNr;

      if (zelle.daylogDayday) {
        kdShowDay.tagestext = zelle.daylogDayday.tagestext;
        if (zelle.daylogEvent) {

          zelle.daylogEvent.forEach((de: DaylogEvent) => {
            this.typelistForEventFilter.forEach((tl: DaylogType) => {

              if (de.typid.toString() === tl.id.toString()) {
                // Typ passt dazu
                if (this.selectedEventTypeFilter != undefined) {
                  // Mit Filter
                  if (de.typid.toString() === this.selectedEventTypeFilter.id.toString()) {
                    kdShowDay.eventKV.push({ value: de.text, key: tl.type });
                  }
                } else {
                  // Ohne Filter
                  kdShowDay.eventKV.push({ value: de.text, key: tl.type });
                }
              }
            });
          });
        }
      } else {
        kdShowDay.tagestext = "-";
      }
      this.kalenderShowArray[zelle.zellenID] = kdShowDay;
    });
  }

  /**
   * 
   * @param zellenID 
   * @param dayNr 
   * @param dd 
   */
  public async addToInhaltsarray(zellenID: number, dayNr: number, dd: DaylogDay): Promise<void> {
    var kd: KalDay = { dayNr: dayNr, daylogDayday: dd, daylogEvent: await this.loadEventsByDay(dd), zellenID: zellenID };
    this.kalenderRawArray.push(kd);
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

  public loadTypeListForEventFilter(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.typelistForEventFilter = [];

    this.rest.get('daylog/type/getAll').then((resdata: DaylogType[]) => {
      this.typelistForEventFilter = resdata;
      warten.close();
    });
  }

  /**
* Alle Events eines Tages Laden 
*/
  private loadEventsByDay(d: DaylogDay): any { //  DaylogEvent[] geht nicht
    return new Promise(
      resolve => {
        if (d && d.id != undefined) {
          const warten = this.dialog.open(DialogWartenComponent, {});
          this.rest.get('daylog/event/get/' + d.id).then((resdata: DaylogEvent[]) => {
            resolve(resdata);
            warten.close();
          });
        } else {
          resolve(null);
        }
      }
    )
  }

}

export interface KalDay {
  dayNr?: number;
  daylogDayday?: DaylogDay;
  daylogEvent?: DaylogEvent[];
  zellenID?: number;
}

export interface KalShowZelle {
  dayNr?: number;
  tagestext?: string;
  eventKV?: KeyValEntry[];
}
