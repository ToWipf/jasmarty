import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DisplayLcd } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-jasmartyFullView',
  templateUrl: './jasmartyFullView.component.html',
  styleUrls: ['./jasmartyFullView.component.less'],
})
export class JasmartyFullViewComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) {}

  public displayLive: DisplayLcd;
  public bLoopStop: boolean = false;
  public display: string[] = [];

  ngOnInit() {
    this.loopLoadNew();
  }

  public ngOnDestroy(): void {
    this.bLoopStop = true;
  }

  private loopLoadNew(): void {
    if (!this.bLoopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadNew();
      }, 1000);
      this.getLcdSoll();
    }
  }

  private getLcdSoll(): void {
    this.http.get(this.rest.gethost() + 'lcd/soll').subscribe(
      (resdata: DisplayLcd) => {
        this.displayLive = resdata;
        this.displayLive.display.forEach((line) => {
          this.display[line.line] = line.data;
        });
      },
      (error) => {
        this.bLoopStop = true;
      }
    );
  }
}
