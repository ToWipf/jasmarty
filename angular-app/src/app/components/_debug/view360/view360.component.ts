import { Component, OnInit } from '@angular/core';
import { View360Options, EquirectProjection } from '@egjs/ngx-view360';

@Component({
  selector: 'app-view360',
  templateUrl: './view360.component.html',
  styleUrls: ['./view360.component.less']
})
export class View360Component implements OnInit {

  constructor() { }

  pic360: Partial<View360Options> = {
    projection: new EquirectProjection({
      src: "/20230922_203755_800-8k.jpg",
    }),
    autoplay: true,
    autoResize: true, // ???
    //initialZoom: 100, 
  };

  
  ngOnInit(): void {
    //
  }
}
