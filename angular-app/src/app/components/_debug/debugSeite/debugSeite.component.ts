import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { View360Options, EquirectProjection } from "@egjs/ngx-view360";

@Component({
  selector: 'app-debugSeite',
  templateUrl: './debugSeite.component.html',
  styleUrls: ['./debugSeite.component.less'],
})
export class DebugSeiteComponent implements OnInit {
  constructor(private rest: ServiceRest, public dialog: MatDialog) { }

  options: Partial<View360Options> = {
    projection: new EquirectProjection({
      src: "/f6db763e-a50b-4745-9ec6-6230b11cb510.jpg",
    })
  };
 // src: "/20230922_203755_800.jpg",
  //src: "/egjs-view360/pano/equirect/veste.jpg",

  https://github.com/naver/egjs-view360/issues/384

  
  public sSQL_IN: string;
  public sSQL_OUT: string;
  public serverTime: number;
  public clientTime: number;

  public testData = [
    { name: "ABC", value: 2 },
    { name: "Etwas", value: 4 },
    { name: "bbb", value: 2 },
    { name: "xyz", value: 0 },
    { name: "20.20.20", value: 10 }
  ];

  ngOnInit() {
    this.loadUhr();


  const canvas = document.createElement("canvas");
  const gl = canvas.getContext("webgl");
  const maxTextureSize = gl.getParameter(gl.MAX_TEXTURE_SIZE);

  console.log(maxTextureSize);
  }

  public loadUhr(): void {
    this.getTime();
    this.clientTime = Date.now();
  }

  public getTime(): void {
    this.rest.get('wipf/time').then((resdata: any) => {
      this.serverTime = resdata.time;
    });
  }

  public warten(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
  }

}
