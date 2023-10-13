import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxCroppedEvent, NgxPhotoEditorService } from 'ngx-photo-editor';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-glowi',
  templateUrl: './glowi.component.html',
  styleUrls: ['./glowi.component.less']
})
export class GlowiComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, private ngxPhotoEditorService: NgxPhotoEditorService) { }

  public size: Number;
  public bLoopStop: boolean = true;
  public mtttData: MtttData[][];

  public imageChangedEvent: any;
  public imageoutput?: NgxCroppedEvent;
  public nKontrast: number = 256 / 2;

  ngOnInit(): void {
    this.getCache();
  }

  fileChangeHandler($event: any) {
    this.ngxPhotoEditorService.open($event, {
      aspectRatio: 1 / 1,
      resizeToHeight: 15,
      autoCropArea: 1
    }).subscribe(data => {
      this.imageoutput = data;
      this.convertImgToArray();
      this.serviceWipf.delay(100).then(() => {
        this.convertImgToArray();
      });
    });
  }

  public convertImgToArray(): void {
    if (this.imageoutput.base64) {
      const myimage = new Image();
      myimage.src = this.imageoutput.base64;

      const cnx = document.createElement('canvas').getContext('2d');

      cnx.drawImage(myimage, 0, 0);
      const width = myimage.width;
      const height = myimage.height;
      if (width != 0) {
        const imgPixels = cnx.getImageData(0, 0, width, height);
        for (let y = 0; y < height; y++) {
          for (let x = 0; x < width; x++) {
            const i = (y * 4) * width + x * 4;
            this.mtttData[y][x].farbe_R = this.calcColor(imgPixels.data[i] -128 + this.nKontrast);
            this.mtttData[y][x].farbe_G = this.calcColor(imgPixels.data[i + 1] -128 + this.nKontrast);
            this.mtttData[y][x].farbe_B = this.calcColor(imgPixels.data[i + 2] -128 + this.nKontrast);
            this.mtttData[y][x].funktion = "Pic";
          }
        }
      }
    }
  }

  private calcColor(inN: number): number {
    if (inN < 0) {
      return 0;
    } else if (inN > 127) {
      return 127;
    } else {
      return inN;
    }
  }

  public saveFull(): void {
    this.rest.post('glowi/setFull', this.mtttData).then(() => this.getCache())
  }

  public getCache(): void {
    this.rest.getNoWartenDialog('glowi/getCache').then((resdata: MtttData[][]) => {
      this.mtttData = resdata;
      // fix farben
      this.mtttData.forEach((da: MtttData[]) => {
        da.forEach((d: MtttData) => {
          if (d.funktion != "Pic") {
            // Farben verstärken
            d.farbe_R = d.farbe_R * 2;
            d.farbe_G = d.farbe_G * 2;
            d.farbe_B = d.farbe_B * 2;
          }
        });
      });
    });
  }

  public doClick(x: number, y: number) {
    this.rest.getPlain('glowi/doClick/' + x + "/" + y).then((resdata: any) => {
      this.getCache(); // TODO nur diff laden?
    });
  }

  public start() {
    this.rest.getPlain('glowi/start').then((resdata: any) => {
      this.getCache();
    });
  }

  public cls() {
    this.rest.getPlain('glowi/cls').then((resdata: any) => {
      this.getCache();
    });
  }

  public startAutoload(): void {
    this.bLoopStop = false;
    this.loopLoadButton();
  }

  private loopLoadButton(): void {
    if (!this.bLoopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadButton();
      }, 1000);
      this.getCache();
    }
  }
}

export interface MtttData {
  farbe_B?: number;
  farbe_G?: number;
  farbe_R?: number;
  funktion?: string;
}