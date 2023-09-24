import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { View360Options, EquirectProjection } from '@egjs/ngx-view360';
import { retry, take } from 'rxjs';
import { WipfImage360 } from 'src/app/datatypes';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-view360',
  templateUrl: './view360.component.html',
  styleUrls: ['./view360.component.less']
})
export class View360Component implements OnInit {

  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  public pic360List: WipfImage360[] = [];
  public big360pic: Partial<View360Options> = {};
  public scale: number = 1024;
  public oneBigView: boolean = false;

  ngOnInit(): void {
    this.getBilderliste();
    // Get Max Pic Size
    const gl = document.createElement("canvas").getContext("webgl");
    this.scale = gl.getParameter(gl.MAX_TEXTURE_SIZE);
  }

  public getBilderliste(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('file/getAll').then((resdata: string[]) => {
      resdata.forEach((str) => {
        if (!str.startsWith(".") && str.includes(".jpg")) {
          this.pic360List.push({ name: str, pic: { autoplay: true, projection: new EquirectProjection({ src: "../file/downloadScale/1000/" + str }) } });
          // this.pic360List.push({ name: str, pic: { autoplay: true, projection: new EquirectProjection({ src: this.downloadItemToBase64("../file/downloadScaleB64/1000/" + str) }) } });
        }
      })
      warten.close();
    });
  }

  public selectPicSize(e: String): void {
    this.big360pic = { autoplay: true, projection: new EquirectProjection({ src: "../file/downloadScale/" + this.scale + "/" + e }) };
    this.oneBigView = true;
  }

  public selectPicOrg(e: String): void {
    this.big360pic = { autoplay: true, projection: new EquirectProjection({ src: "../file/download/" + e }) };
    this.oneBigView = true;
  }

  //this.big360pic = { autoplay: true, projection: new EquirectProjection({ src: 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==' }) };
  // public downloadItemToBase64(name: string): string {
  //   var re: string = "";
  //   this.rest.get(name).then((response: string) => {
  //     console.log(response);
  //     re = (response);
  //   });
  //   return re;
  // }

}
