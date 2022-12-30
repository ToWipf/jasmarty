import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-jasmarty12864panel',
  templateUrl: './jasmarty12864Panel.component.html',
  styleUrls: ['./jasmarty12864Panel.component.less'],
})
export class Jasmarty12864PanelComponent implements OnInit {
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }
  public screen: boolean[][] = new Array(64).fill(false).map(() => new Array(128).fill(false));

  ngOnInit(): void {
    for (let x = 0; x < 64; x++) {
      for (let y = 0; y < 128; y++) {
        this.screen[x][y] = false;
      }
    }
  }

  public setBlock(x: number, y: number): void {
    this.screen[x][y] = !this.screen[x][y];
  }

  public send(): void {
    this.rest.post('lcd12864/setScreen', this.screen).then((res) => {
    });
  }

  public load(): void {
    this.rest.get('lcd12864/getScreen').then((res: any) => {
      this.screen = res;
    });
  }

  public refreshLcdNow(): void {
    this.rest.post('lcd12864/refreshNow', null).then((res) => {
    });
  }

}
