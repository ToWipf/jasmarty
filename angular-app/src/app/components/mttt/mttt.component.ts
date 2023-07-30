import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DaylogType } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-mttt',
  templateUrl: './mttt.component.html',
  styleUrls: ['./mttt.component.less']
})
export class MtttComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public size: Number;
  public mtttData: MtttData[][];

  ngOnInit(): void {
    this.getCache();
  }

  public getCache(): void {
    this.rest.get('mttt/getCache').then((resdata: MtttData[][]) => {
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
    this.rest.get('mttt/doClick/' + x + "/" + y).then((resdata: any) => {
      this.getCache(); // TODO nur diff laden?
    });
  }

  public startMttt() {
    this.rest.get('mttt/startMttt').then((resdata: any) => {
      this.getCache();
    });
  }
}

export interface MtttData {
  farbe_B?: number;
  farbe_G?: number;
  farbe_R?: number;
  funktion?: string;
}