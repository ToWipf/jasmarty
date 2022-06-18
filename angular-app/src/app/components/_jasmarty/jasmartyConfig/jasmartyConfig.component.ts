import { Component, OnInit } from '@angular/core';
import { Jaconfig } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-jasmartyConfig',
  templateUrl: './jasmartyConfig.component.html',
  styleUrls: ['./jasmartyConfig.component.less'],
})
export class JasmartyConfigComponent implements OnInit {
  constructor( private rest: ServiceRest) { }

  public jaconfig: Jaconfig = {};
  public ports: [{ name: string }];
  public bJasmartyActive: boolean;

  ngOnInit() {
    this.load();
    this.getPorts();
    this.getJasmartyActive();
  }

  public load(): void {
    this.rest.get('lcd/config/get').then((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public save(): void {
    this.rest.post('lcd/config/set', JSON.stringify(this.jaconfig)).then((resdata: any) => {
      if (resdata.save) {
        console.log('saved');
        this.startLcdAgain();
      } else {
        //TODO: Meldung Fehler
        console.log('fehler');
      }
    });
  }

  public startLcdAgain(): void {
    this.rest.get('debug/jasmarty/restart').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public getPorts(): void {
    this.rest.get('wipf/ports').then((resdata: any) => {
      console.log(resdata);
      this.ports = resdata.list;
    });
  }

  public start(): void {
    this.rest.get('lcd/open').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public stop(): void {
    this.rest.get('lcd/open').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOn(): void {
    this.rest.get('refresh/on').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.rest.get('refresh/off').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public getJasmartyActive(): void {
    this.rest.get('basesettings/get/jasmarty').then((resdata: any) => {
      this.bJasmartyActive = resdata.active;
    });
  }

  public setJasmartyActive(bStatus: boolean): void {
    this.rest.post('basesettings/set/jasmarty/' + bStatus, '').then((resdata: any) => {
      console.log(resdata);
    });
  }
}
