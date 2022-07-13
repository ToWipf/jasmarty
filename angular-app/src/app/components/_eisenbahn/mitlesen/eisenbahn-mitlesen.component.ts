import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-eisenbahn-mitlesen',
  templateUrl: './eisenbahn-mitlesen.component.html',
  styleUrls: ['./eisenbahn-mitlesen.component.less'],
})
export class EisenbahnMitlesenComponent implements OnInit {
  constructor(private rest: ServiceRest) { }

  ngOnInit() {
  }

  public start(): void {
    this.rest.get('eisenbahn/mitlesen/start').then((resdata) => {
      console.log(resdata);
    });
  }

  public stop(): void {
    this.rest.get('eisenbahn/mitlesen/stop').then((resdata) => {
      console.log(resdata);
    });
  }

}
