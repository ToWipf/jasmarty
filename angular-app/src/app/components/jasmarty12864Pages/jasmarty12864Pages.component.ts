import { Component } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { HttpClient } from '@angular/common/http';
import { CroppedEvent } from 'ngx-photo-editor';

@Component({
  selector: 'app-jasmarty12864pages',
  templateUrl: './jasmarty12864Pages.component.html',
  styleUrls: ['./jasmarty12864Pages.component.less'],
})
export class Jasmarty12864PagesComponent {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }

  public imageChangedEvent: any;
  public base64: any;
  public screen: boolean[][] = new Array(64).fill(false).map(() => new Array(128).fill(false));

  public fileChangeEvent(event: any): void {
    this.imageChangedEvent = event;
  }

  public imageCropped(event: CroppedEvent): void {
    this.base64 = event.base64;
  }

  public test(): void {

    const myimage = new Image();
    myimage.src = this.base64;

    const cnv = document.createElement('canvas');
    const cnx = cnv.getContext('2d');

    cnx.drawImage(myimage, 0, 0);
    const width = myimage.width;
    const height = myimage.height;
    const imgPixels = cnx.getImageData(0, 0, width, height);

    console.log(imgPixels);

    for (let y = 0; y < height; y++) {
      for (let x = 0; x < width; x++) {
        const i = (y * 4) * width + x * 4;
        const avg = (imgPixels.data[i] + imgPixels.data[i + 1] + imgPixels.data[i + 2]) / 3;

        if (avg > 128){

          this.screen[y][x] = true;
        } else {
          this.screen[y][x] = false;
        }

      }
    }

    console.log(this.screen);

    this.send();


  }

  
  public send(): void {
    this.http.post(this.rest.gethost() + 'lcd/12864/setScreen', this.screen).subscribe((res) => {
      console.log(res);
    });
  }


}
