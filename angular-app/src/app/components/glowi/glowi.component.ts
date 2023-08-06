import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-glowi',
  templateUrl: './glowi.component.html',
  styleUrls: ['./glowi.component.less']
})
export class GlowiComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public size: Number;
  public bLoopStop: boolean = true;
  public mtttData: MtttData[][];

  ngOnInit(): void {
    this.getCache();
  }

  public getCache(): void {
    this.rest.getNoWartenDialog('glowi/getCache').then((resdata: MtttData[][]) => {
      this.mtttData = resdata;
      // fix farben
      this.mtttData.forEach((da: MtttData[]) => {
        da.forEach((d: MtttData) => {
          d.farbe_R = d.farbe_R * 2;
          d.farbe_G = d.farbe_G * 2;
          d.farbe_B = d.farbe_B * 2;
        });
      });
    });
  }

  public doClick(x: number, y: number) {
    this.rest.get('glowi/doClick/' + x + "/" + y).then((resdata: any) => {
      this.getCache(); // TODO nur diff laden?
    });
  }

  public start() {
    this.rest.get('glowi/start').then((resdata: any) => {
      this.getCache();
    });
  }

  public cls() {
    this.rest.get('glowi/cls').then((resdata: any) => {
      this.getCache();
    });
  }

  public startAutoload(): void {
    this.bLoopStop = false;
    this.loopLoadButton();
  }

  private loopLoadButton(): void {
    if (!this.bLoopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadButton();
      }, 1000);
      this.getCache();
    }
  }
}

export interface MtttData {
  farbe_B?: number;
  farbe_G?: number;
  farbe_R?: number;
  funktion?: string;
}