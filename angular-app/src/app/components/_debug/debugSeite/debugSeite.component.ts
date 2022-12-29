import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-debugSeite',
  templateUrl: './debugSeite.component.html',
  styleUrls: ['./debugSeite.component.less'],
})
export class DebugSeiteComponent implements OnInit {
  constructor(private rest: ServiceRest) { }

  public sSQL_IN: string;
  public sSQL_OUT: string;
  public uhr: string;

  public testData = [
    { name: "ABC", value: 2 },
    { name: "Etwas", value: 4 },
    { name: "bbb", value: 2 },
    { name: "xyz", value: 0 },
    { name: "20.20.20", value: 10 }
  ];

  ngOnInit() {
    this.uhr = new Date(Date.now()).toISOString();
  }

  public doSQL(): void {
    this.rest.post('sql/query/' + this.sSQL_IN, '').then((resdata: any) => {
      this.sSQL_OUT = resdata.res;
    });
  }

}
