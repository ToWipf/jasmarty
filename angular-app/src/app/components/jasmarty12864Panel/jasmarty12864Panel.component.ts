import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-jasmarty12864panel',
  templateUrl: './jasmarty12864Panel.component.html',
  styleUrls: ['./jasmarty12864Panel.component.less'],
})
export class Jasmarty12864PanelComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }
  public screen: boolean[][] = new Array(128).fill(false).map(() => new Array(64).fill(false));

  ngOnInit(): void {
    for (let x = 0; x < 64; x++) {
      for (let y = 0; y < 128; y++) {
        this.screen[x][y] = false;
      }
    }
  }

  public setBlock(x: number, y: number) {
    console.log(x, y);
    this.screen[x][y] = !this.screen[x][y];
  }

}
