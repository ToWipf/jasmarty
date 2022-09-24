import { Component, OnInit, OnDestroy } from '@angular/core';
import { DisplayLcd, JapageForList } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-jasmartyMain',
  templateUrl: './jasmartyMain.component.html',
  styleUrls: ['./jasmartyMain.component.less'],
})
export class JasmartyMainComponent implements OnInit, OnDestroy {
  constructor(private rest: ServiceRest) { }

  public displayLive: DisplayLcd;
  public display: string[] = [];
  public pagelist: JapageForList[] = [];
  public bLoopStop: boolean = false;

  ngOnInit() {
    this.loopLoadNew();
    this.getAllPages();
  }

  public ngOnDestroy(): void {
    this.bLoopStop = true;
  }

  public refreshNow(): void {
    this.rest.get('refresh/refreshCache').then((res: any) => {
    });
  }

  public selectSite(item: JapageForList): void {
    this.rest.get('pages/select/' + item.id).then(() => {
      this.refreshNow();
    });
  }

  private loopLoadNew(): void {
    if (!this.bLoopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadNew();
      }, 1000);
      this.getLcdSoll();
    }
  }

  private getLcdSoll(): void {
    this.rest.get('lcd/soll').then(
      (resdata: DisplayLcd) => {
        this.displayLive = resdata;
        this.displayLive.display.forEach((line) => {
          this.display[line.line] = line.data;
        });
      },
      (error) => {
        this.bLoopStop = true;
      }
    );
  }

  private getAllPages(): void {
    this.rest.get('pages/getAllPages').then((resdata: JapageForList[]) => {
      this.pagelist = resdata;
      this.pagelist.sort((a, b) => a.name.localeCompare(b.name));
    });
  }
}
