import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { displayLcd } from 'src/app/datatypes';

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

  ngOnInit() {
    this.loopLoadNew();
  }

  public ngOnDestroy(): void {
    this.loopStop = true;
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
    this.http.get("http://localhost:8080/lcd/soll").subscribe((resdata: any) => {
      this.displayLive = resdata;

      this.displayLive.display.forEach((line) =>{
        this.display[line.line] = line.data;
      })
      console.log(this.display);
            
    });
  }
}
