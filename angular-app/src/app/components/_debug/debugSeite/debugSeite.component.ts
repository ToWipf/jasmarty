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

  ngOnInit() {
  }

  public doSQL(): void {
    this.rest.post(this.rest.gethost() + 'sql/query/' + this.sSQL_IN, '').then((resdata: any) => {
      this.sSQL_OUT = resdata.res;
    });
  }

}
