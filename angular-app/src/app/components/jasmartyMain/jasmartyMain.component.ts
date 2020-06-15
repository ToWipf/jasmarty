import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { displayLcd, japageForList } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-jasmartyMain",
  templateUrl: "./jasmartyMain.component.html",
  styleUrls: ["./jasmartyMain.component.less"],
})
export class JasmartyMainComponent implements OnInit, OnDestroy {
  constructor(private http: HttpClient, private rest: ServiceRest) {}

  public displayLive: displayLcd;
  public display: string[] = [];
  public pagelist: japageForList[] = [];
  public bLoopStop: boolean = false;
  
  ngOnInit() {
    this.loopLoadNew();
    this.getAllPages();
  }

  public ngOnDestroy(): void {
    this.bLoopStop = true;
  }

  public refreshNow(): void {
    this.http.get(this.rest.gethost() + "refresh/refreshCache").subscribe((res: any) => {
      console.log(res);
    });
  }

  public selectSite(item: japageForList): void {
    this.http.get(this.rest.gethost() + "pages/select/" + item.id).subscribe(() => {
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
    this.http.get(this.rest.gethost() + "lcd/soll").subscribe(
      (resdata: displayLcd) => {
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
    this.http.get(this.rest.gethost() + "pages/getAllPages").subscribe((resdata: japageForList[]) => {
      this.pagelist = resdata;
      this.pagelist.sort((a, b) => a.name.localeCompare(b.name));
    });
  }
}
