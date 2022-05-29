import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-debugSeite',
  templateUrl: './debugSeite.component.html',
  styleUrls: ['./debugSeite.component.less'],
})
export class DebugSeiteComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) { }

  public sSQL_IN: string;
  public sSQL_OUT: string;

  ngOnInit() {
  }

  public doSQL(): void {
    this.http.post(this.rest.gethost() + 'sql/query/' + this.sSQL_IN, '').subscribe((resdata: any) => {
      this.sSQL_OUT = resdata.res;
    });
  }

}
