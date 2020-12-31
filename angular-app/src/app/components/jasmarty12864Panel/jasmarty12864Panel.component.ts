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
  public screen: boolean[][] = new Array(64).fill(false).map(() => new Array(128).fill(false));

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

  public genScreenToSend() {
    let jarray: number[] = [];

    for (let x = 0; x < 64; x++) {
      for (let y = 0; y < (128 / 8); y++) {

        let nr = 0;

        for (let n = 0; n < 8; n++) {
          console.log(x, y + n);
          if (this.screen[x][y + n]) {
            nr = nr + n;
          }
        }

        //this.screen[x][y * 8];

        //var ns = parseInt( , 2);
      }
    }


    // for (let r = 0; r < 1024; r++) {
    //   let nNr: number = 0;
    //   for (let nTmp = 0; nTmp < 8; nTmp++) {
    //     console.log(Math.floor(r / 16), nTmp * r);
    //     if (this.screen[Math.floor(r / 16)][nTmp]) {
    //       nNr = nNr + nTmp + 1;
    //     }
    //   }
    //   jarray.push(nNr);
    // }
    console.log(jarray);
  }

  private dec2bin(dec) {
    return (dec >>> 0).toString(2);
  }



}
