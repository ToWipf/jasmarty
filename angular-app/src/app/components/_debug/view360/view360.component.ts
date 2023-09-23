import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { View360Options, EquirectProjection } from '@egjs/ngx-view360';
import { take } from 'rxjs';
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

  public list: string[] = [];
  public pic360List: Partial<View360Options>[] = [];

  ngOnInit(): void {
    this.getBilderliste();
  }

  public getBilderliste(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('file/getAll').then((resdata: string[]) => {
      resdata.forEach((str) => {
        if (!str.startsWith(".") && str.includes(".jpg")) {
          this.list.push(str);

          this.pic360List.push({ autoplay: true, projection: new EquirectProjection({ src: "../file/downloadScale/600/" + str }) });
        }
      })
      warten.close();
    });
  }

}
