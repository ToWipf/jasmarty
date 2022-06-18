import { Component, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { DynpageEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-dynpageShow',
  templateUrl: './dynpageShow.component.html',
  styleUrls: ['./dynpageShow.component.less'],
})
export class DynpageShowComponent {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public nPageId: Number = 0;
  public sHtml: string = "";
  public sScript: string = "";
  public sStyle: string = "";

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('dynpages/get/' + this.nPageId).then((resdata: DynpageEntry) => {
      this.sHtml = resdata.html;
      this.sScript = resdata.script;
      this.sStyle = resdata.style;

      warten.close();
    });
  }

  public getSytle(): string {
    return JSON.stringify(this.sStyle);
  }

  // public getRest(sPath: string): any {
  //   const warten = this.dialog.open(DialogWartenComponent, {});
  //   this.http.get(sPath).subscribe((result: any) => {
  //     warten.close();
  //     return result;
  //   });
  // }

}
