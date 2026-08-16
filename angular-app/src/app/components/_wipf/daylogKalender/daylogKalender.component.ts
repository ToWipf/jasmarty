import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DaylogDay, DaylogEvent, DaylogType, DialogInputOneThingContent } from 'src/app/datatypes';
import { DialogInputOneThingComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
    selector: 'app-daylogKalender',
    templateUrl: './daylogKalender.component.html',
    styleUrls: ['./daylogKalender.component.less'],
    standalone: false
})
export class DaylogKalenderComponent implements OnInit {

  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  public sFilterYYYY: number = new Date(Date.now()).getFullYear();
  public sFilterMON: number = new Date(Date.now()).getMonth() + 1;
  public sFilerMONasText: string = "";
  public kalenderRawArray: KalDay[] = [];
  public kalenderShowArray: KalShowZelle[] = [];
  public typelistForEventFilter: DaylogType[] = [];
  public selectedEventTypeFilter: DaylogType[] = [];
  public sFilter: string = "";
  public hideNoMenues: boolean = true;
  public table_size: number = 76;
  public jetztLadenZeigen: boolean = true;

  ngOnInit(): void {
    this.initFilter();
    this.loadTypeListForEventFilter();
    this.jetztLadenZeigen = true;
  }

  private initFilter(): void {
    this.sFilterYYYY = (new Date(Date.now()).getFullYear());
    this.sFilterMON = (new Date(Date.now()).getMonth() + 1);
  }

  public changeTableHeight(delta: number): void {
    const newHeight = this.table_size + delta;
    if (newHeight >= 0 && newHeight <= 100) {
      this.table_size = newHeight;
    }
  }

  public applyTextFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.renderKalender();
    });
  }

  public loadDays(): void {
    this.jetztLadenZeigen = false;
    this.kalenderRawArray = new Array(37);
    this.kalenderShowArray = [];
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

        // Test überhaupt ein Tag vorhanden ist
        if (resdata.length != 0) {
          for (var dayNr = 1; dayNr <= tageImMonat; dayNr++) {
            resdata.forEach((d: DaylogDay) => {
              if (d.date && new Date(d.date).getDate() === dayNr) {
                // Tag vorhanden
                this.addToInhaltsarray(dayNr + erstWochentag - 1, dayNr, d);
              } else {
                // Tag gibt es nicht
                this.addToInhaltsarray(dayNr + erstWochentag - 1, dayNr, null as unknown as DaylogDay);
              }
            })
          }
        } else {
          // Wenn kein Tag vorhanden ist - leer füllen
          for (var dayNr = 1; dayNr <= tageImMonat; dayNr++) {
            this.addToInhaltsarray(dayNr + erstWochentag - 1, dayNr, null as unknown as DaylogDay);
          }
        }

        warten.close();
        // Unschön - TODO:
        setTimeout(() => {
          this.renderKalender();
        }, 2000);
      });
    }
  }

  public renderKalender(): void {
    this.kalenderShowArray = new Array(37).fill({ tagestext: "-" });

    this.kalenderRawArray.forEach((zelle: KalDay) => {
      const kdShowDay: KalShowZelle = { dayNr: zelle.dayNr, tagestext: '-', eventKV: [] };

      if (zelle.daylogDayday) {
        kdShowDay.tagestext = zelle.daylogDayday.tagestext ?? '-';

        if (zelle.daylogEvent) {
          zelle.daylogEvent.forEach((de: DaylogEvent) => {
            const deTypeId = de.typid ?? '';
            const eventTypeMatch = this.typelistForEventFilter.find(tl => (tl.id ?? -1).toString() === deTypeId.toString());
            const filterMatch = this.sFilter.trim().length === 0 || (de.text ?? '').toLocaleLowerCase().includes(this.sFilter.toLocaleLowerCase().trim());

            if (eventTypeMatch && filterMatch) {
              if (this.selectedEventTypeFilter.length > 0) {
                this.selectedEventTypeFilter.forEach((fi: DaylogType) => {
                  const fiId = fi.id ?? -1;
                  if (deTypeId.toString() === fiId.toString()) {
                    kdShowDay.eventKV = kdShowDay.eventKV ?? [];
                    kdShowDay.eventKV.push({ value: de.text ?? '', key: eventTypeMatch.type ?? '', color: eventTypeMatch.color ?? '' });
                  }
                });
              } else {
                kdShowDay.eventKV = kdShowDay.eventKV ?? [];
                kdShowDay.eventKV.push({ value: de.text ?? '', key: eventTypeMatch.type ?? '', color: eventTypeMatch.color ?? '' });
              }
            }
          });
        }
      }

      if (typeof zelle.zellenID === 'number') {
        this.kalenderShowArray[zelle.zellenID] = kdShowDay;
      }
    });
  }

  public async addToInhaltsarray(zellenID: number, dayNr: number, dd: DaylogDay | null): Promise<void> {
    const loadedEvents = dd ? await this.loadEventsByDay(dd) : [];
    const kd: KalDay = { dayNr: dayNr, daylogDayday: dd ?? undefined, daylogEvent: loadedEvents, zellenID: zellenID };
    this.kalenderRawArray.push(kd);
  }

  public changeMonat(vorRueck: boolean): void {
    if (vorRueck) {
      if (this.sFilterMON == 1) {
        this.sFilterMON = 12;
        this.changeYYYY(true);
      } else if (this.sFilterMON > 1) {
        this.sFilterMON--;
      }
    } else {
      if (this.sFilterMON == 12) {
        this.sFilterMON = 1;
        this.changeYYYY(false);
      } else if (this.sFilterMON < 12) {
        this.sFilterMON++;
      }
    }
    this.jetztLadenZeigen = true;
    this.kalenderRawArray = new Array(37);
    this.kalenderShowArray = [];
  }

  public changeYYYY(vorRueck: boolean): void {
    if (vorRueck) {
      this.sFilterYYYY--;
    }
    else {
      this.sFilterYYYY++;
    }
    this.jetztLadenZeigen = true;
    this.kalenderRawArray = new Array(37);
    this.kalenderShowArray = [];
  }

  public setMonat(): void {
    let datenfeld: DialogInputOneThingContent = {
      type: "number",
      infotext: "Monat einstellen",
      infotext2: "Monat",
      data: this.sFilterMON
    };

    const dialogRef = this.dialog.open(DialogInputOneThingComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: datenfeld,
    });

    dialogRef.afterClosed().subscribe((result: DialogInputOneThingContent) => {
      if (result) {
        if (result.data > 0 && result.data < 13) {
          this.sFilterMON = result.data;
          this.loadDays();
        }
      }
    });
  }

  public setYYYY(): void {
    let datenfeld: DialogInputOneThingContent = {
      type: "number",
      infotext: "Jahr einstellen",
      infotext2: "Jahr",
      data: this.sFilterYYYY
    };

    const dialogRef = this.dialog.open(DialogInputOneThingComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: datenfeld,
    });

    dialogRef.afterClosed().subscribe((result: DialogInputOneThingContent) => {
      if (result) {
        this.sFilterYYYY = result.data;
        this.loadDays();
      }
    });
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
  eventKV?: KalZelleData[];
}

export interface KalZelleData {
  key?: string;
  value?: string;
  color?: string;
}
