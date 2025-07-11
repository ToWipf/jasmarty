import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { NgxCroppedEvent, NgxPhotoEditorService } from 'ngx-photo-editor';
import { Lcd12864PageDescription, Lcd12864PageDescriptionDynamic } from 'src/app/datatypes';
import { MatDialog } from '@angular/material/dialog';
import { DialogVariablenHilfeComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
    selector: 'app-jasmarty12864pages',
    templateUrl: './jasmarty12864Pages.component.html',
    styleUrls: ['./jasmarty12864Pages.component.less'],
    standalone: false
})
export class Jasmarty12864PagesComponent implements OnInit {
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog, private ngxPhotoEditorService: NgxPhotoEditorService) {

  }

  public imageChangedEvent: any;
  public imageoutput?: NgxCroppedEvent;
  public nKontrast: number = 256 / 2;
  public lcdDescription: Lcd12864PageDescription = {};

  public ngOnInit(): void {
    this.lcdDescription.dynamicData = [];
    this.lcdDescription.id = 1;
    this.lcdDescription.timeoutTime = 1;
    this.loadLcdDescription();
  }

  fileChangeHandler($event: any) {
    this.ngxPhotoEditorService.open($event, {
      aspectRatio: 2 / 1,
      resizeToHeight: 64,
      autoCropArea: 1
    }).subscribe(data => {
      this.lcdDescription.staticData = new Array(64).fill(false).map(() => new Array(128).fill(false));
      this.imageoutput = data;
      this.serviceWipf.delay(50).then(() => {
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
      const imgPixels = cnx.getImageData(0, 0, width, height);

      for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
          const i = (y * 4) * width + x * 4;
          const avg = (imgPixels.data[i] + imgPixels.data[i + 1] + imgPixels.data[i + 2]) / 3;

          if (avg > this.nKontrast) {

            this.lcdDescription.staticData[y][x] = false;
          } else {
            this.lcdDescription.staticData[y][x] = true;
          }

        }
      }
    } else {
      this.lcdDescription.staticData = [];
    }
  }

  public addDynLine(): void {
    var dp: Lcd12864PageDescriptionDynamic = {};
    dp.data = "";
    dp.font = "";
    dp.type = "";
    dp.x = "0";
    dp.y = "0";
    this.lcdDescription.dynamicData.push(dp);
  }

  public delDynLine(id: number): void {
    this.lcdDescription.dynamicData.splice(id, 1);
  }

  public selectLcdDescription(): void {
    this.rest.post('lcd12864/selectPage/' + this.lcdDescription.id, null).then((res) => {
    });
  }

  public async sendSelectedPage(): Promise<void> {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.saveLcdDescription();
    this.serviceWipf.delay(100).then(() => {
      this.selectLcdDescription();
      warten.close();
    });
  }

  public async pageNext(): Promise<void> {
    this.lcdDescription.id++;
    this.loadLcdDescription();
    this.serviceWipf.delay(100).then(() => {
      this.selectLcdDescription();
    });
  }

  public async pageLast(): Promise<void> {
    if (this.lcdDescription.id > 0) {
      this.lcdDescription.id--;
      this.loadLcdDescription();
      this.serviceWipf.delay(100).then(() => {
        this.selectLcdDescription();
      });
    }
  }

  public clearPic(): void {
    this.lcdDescription.staticData = [];
  }

  private saveLcdDescription(): void {
    this.rest.post('lcd12864/save', this.lcdDescription).then((resID) => {
      this.lcdDescription.id = resID;
    });
  }

  private loadLcdDescription(): void {
    this.imageoutput = null;
    this.rest.get('lcd12864/get/' + this.lcdDescription.id).then((res: any) => {
      if (res == null) {
        // Neue leere Seite erstellen
        this.lcdDescription = {};
        this.lcdDescription.timeoutTime = 1;
        this.lcdDescription.name = "Neu";
        this.lcdDescription.dynamicData = [];
        this.lcdDescription.staticData = [];
      } else {
        // Inhalte laden -> Json String convert
        if (res.dynamicData?.empty) {
          res.dynamicData = [];
        } else if (res.dynamicData) {
          res.dynamicData = JSON.parse(res.dynamicData);
        } else {
          res.dynamicData = [];
        }

        if (res.staticData?.empty) {
          res.staticData = [];
        } else if (res.staticData) {
          res.staticData = JSON.parse(res.staticData);
        } else {
          res.staticData = [];
        }

        this.lcdDescription = res;
      }
      console.log(this.lcdDescription);
    });
  }

  public openariablenHilfeDialog(): void {
    this.dialog.open(DialogVariablenHilfeComponent, { minWidth: '200px', height: '300px' });
  }

}
