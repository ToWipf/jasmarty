import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-charGenerator',
  templateUrl: './charGenerator.component.html',
  styleUrls: ['./charGenerator.component.less']
})
export class CharGeneratorComponent implements OnInit {

  constructor() { }

  public screen: boolean[][];
  public dezdata: number[];
  public hexdata: string;
  public sizeX: number = 7;
  public sizeY: number = 5;

  ngOnInit(): void {
    this.reset();
  }

  public setBlock(x: number, y: number): void {
    this.screen[x][y] = !this.screen[x][y];
    this.get();
  }

  public reset() {
    this.hexdata = "";
    this.dezdata = new Array(this.sizeY).fill(0);
    this.screen = new Array(this.sizeX).fill(false).map(() => new Array(this.sizeY).fill(false));
    for (let x = 0; x < this.sizeX; x++) {
      for (let y = 0; y < this.sizeY; y++) {
        this.screen[x][y] = false;
      }
    }
  }

  public get() {
    this.dezdata = new Array(this.sizeY).fill(0);
    for (let x = 0; x < this.sizeX; x++) {
      for (let y = 0; y < this.sizeY; y++) {
        if (this.screen[x][y]) {
          this.dezdata[y] = this.dezdata[y] + Math.pow(2, x);
        }
      }
    }
    this.resToHex();
  }

  public resToHex() {
    this.hexdata = "";
    this.dezdata.forEach((element) => {
      let nr = element.toString(16);
      if (nr.length < 2) {
        nr = "0" + nr;
      }
      this.hexdata = this.hexdata + "0x" + nr + " ";
    });
  }

}
