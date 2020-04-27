import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { displayLcd, japageForList } from 'src/app/datatypes';

@Component({
  selector: "app-jasmartyLive",
  templateUrl: "./jasmartyLive.component.html",
  styleUrls: ["./jasmartyLive.component.less"],
})
export class JasmartyLiveComponent implements OnInit, OnDestroy  {
  constructor(private http: HttpClient) {}

  public displayLive: displayLcd;
  public loopStop: boolean = false;
  public display: string[] = [];
  public pagelist: japageForList[] = [];

  ngOnInit() {
    this.loopLoadNew();
    this.getAllPages();
  }

  public ngOnDestroy(): void {
    this.loopStop = true;
  }

  public refreshNow(): void {
    this.http.get("http://localhost:8080/refresh/refreshCache" ).subscribe((res: any) => {
          console.log(res);      
    });
  }

  public selectSite(item: japageForList): void{
      this.http
        .get("http://localhost:8080/pages/select/" + item.id)
        .subscribe(() => {
          this.refreshNow();
        });
    }

  private loopLoadNew(): void {
    if (!this.loopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadNew();
      }, 1000);
      this.getLcdSoll();
    }
  }

  private getLcdSoll(): void {
    this.http.get("http://localhost:8080/lcd/soll").subscribe((resdata: displayLcd) => {
      this.displayLive = resdata;

      this.displayLive.display.forEach((line) =>{
        this.display[line.line] = line.data;
      })
      console.log(this.display);       
    });
  }

  private getAllPages(): void {
    this.http.get("http://localhost:8080/pages/getAllPages").subscribe((resdata: japageForList[]) => {
      this.pagelist = resdata;
      this.pagelist.sort((a,b) => a.name.localeCompare(b.name));
    });
  }
}
