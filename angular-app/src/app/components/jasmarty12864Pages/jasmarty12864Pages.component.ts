import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { HttpClient } from '@angular/common/http';
import { CroppedEvent } from 'ngx-photo-editor';
import { Lcd12864PageDescription, Lcd12864PageDescriptionDynamic } from 'src/app/datatypes';

@Component({
  selector: 'app-jasmarty12864pages',
  templateUrl: './jasmarty12864Pages.component.html',
  styleUrls: ['./jasmarty12864Pages.component.less'],
})
export class Jasmarty12864PagesComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }

  public imageChangedEvent: any;
  public base64: any;
  public nKontrast: number = 256 / 2;
  public lcdDescription: Lcd12864PageDescription = {};

  public ngOnInit(): void {
    this.lcdDescription.dynamic = [];
    this.lcdDescription.id = 4;
    this.lcdDescription.name = "demo";
  }

  public fileChangeEvent(event: any): void {
    this.imageChangedEvent = event;
    //this.convertImgToArray();
  }

  public imageCropped(event: CroppedEvent): void {
    console.log("now!")
    this.base64 = event.base64;
  }

  public convertImgToArray(): void {
    if (this.base64) {
      this.lcdDescription.static = new Array(64).fill(false).map(() => new Array(128).fill(false));

      const myimage = new Image();
      myimage.src = this.base64;

      const cnx = document.createElement('canvas').getContext('2d');

      cnx.drawImage(myimage, 0, 0);
      const width = myimage.width;
      const height = myimage.height;
      const imgPixels = cnx.getImageData(0, 0, width, height);

      for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
          const i = (y * 4) * width + x * 4;
          const avg = (imgPixels.data[i] + imgPixels.data[i + 1] + imgPixels.data[i + 2]) / 3;

          if (avg > this.nKontrast) {

            this.lcdDescription.static[y][x] = false;
          } else {
            this.lcdDescription.static[y][x] = true;
          }

        }
      }
    } else {
      this.lcdDescription.static = [];
    }
  }

  public addDynPage(): void {
    var dp: Lcd12864PageDescriptionDynamic = {};
    dp.data = "data";
    dp.font = "FONT_57";
    dp.type = "TEXT";
    dp.x = 0;
    dp.y = 0;
    this.lcdDescription.dynamic.push(dp);
  }

  public saveLcdDescription(): void {
    this.convertImgToArray();
    console.log(this.lcdDescription);
    this.http.post(this.rest.gethost() + 'lcd12864/savePage', this.lcdDescription).subscribe((res) => {
      console.log(res);
    });
  }

  public loadLcdDescription(): void {
    this.http.get(this.rest.gethost() + 'lcd12864/getPage/' + this.lcdDescription.id).subscribe((res) => {
      console.log(res);
      this.lcdDescription = res;
    });
  }

  public selectLcdDescription(): void {
    this.http.post(this.rest.gethost() + 'lcd12864/selectPage/' + this.lcdDescription.id, null).subscribe((res) => {
      console.log(res);
    });
  }

  public refreshLcdNow(): void {
    this.http.post(this.rest.gethost() + 'lcd12864/refreshNow', null).subscribe((res) => {
      console.log(res);
    });
  }

  public sendSelectedPage(): void {
    this.saveLcdDescription();
    this.selectLcdDescription();
  }



}
