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
      src: "/20230922_203755_800-8k.jpg",
    })
    
  };
  // src: "/20230922_203755_800.jpg",
  // src: "/egjs-view360/pano/equirect/veste.jpg",

  public sSQL_IN: string;
  public sSQL_OUT: string;
  public serverTime: number;
  public clientTime: number;
  public maxTextureSize: string;

  public testData = [
    { name: "ABC", value: 2 },
    { name: "Etwas", value: 4 },
    { name: "bbb", value: 2 },
    { name: "xyz", value: 0 },
    { name: "20.20.20", value: 10 }
  ];

  ngOnInit() {
    this.loadUhr();

    // Get Max Pic Size
    const gl = document.createElement("canvas").getContext("webgl");
    this.maxTextureSize = gl.getParameter(gl.MAX_TEXTURE_SIZE);
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
