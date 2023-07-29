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
    this.getSize();
    this.getCache();
  }

  public getSize(): void {
    this.rest.get('mttt/size').then((resdata: Number) => {
      this.size = resdata;
    });
  }

  public getCache(): void {
    this.rest.get('mttt/getCache').then((resdata: MtttData[][]) => {
      this.mtttData = resdata;
    });
    // fix farben
    // for (let x in this.mtttData){
    //   for (let y in x){

    //   }
    // }
  }
}

export interface MtttData {
  farbe_B?: number;
  farbe_G?: number;
  farbe_R?: number;
  funktion?: string;
}