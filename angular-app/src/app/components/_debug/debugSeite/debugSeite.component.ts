import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-debugSeite',
  templateUrl: './debugSeite.component.html',
  styleUrls: ['./debugSeite.component.less'],
})
export class DebugSeiteComponent implements OnInit {
  constructor(private rest: ServiceRest, public dialog: MatDialog) { }

  public sSQL_IN: string;
  public sSQL_OUT: string;
  public serverTime: number;
  public clientTime: number;

  public testData = [
    { name: "ABC", value: 2 },
    { name: "Etwas", value: 4 },
    { name: "bbb", value: 2 },
    { name: "xyz", value: 0 },
    { name: "20.20.20", value: 10 }
  ];

  ngOnInit() {
    this.loadUhr();
  }

  public loadUhr(): void {
    this.getTime();
    this.clientTime = Date.now();
  }

  public getTime(): void {
    this.rest.get('wipf/time').then((resdata: any) => {
      this.serverTime = resdata.time;
    });
  }

  public warten(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
  }

}
