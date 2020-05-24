import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { displayLcd } from "src/app/datatypes";

@Component({
  selector: "app-jasmartyFullView",
  templateUrl: "./jasmartyFullView.component.html",
  styleUrls: ["./jasmartyFullView.component.less"],
})
export class JasmartyFullViewComponent implements OnInit {
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
    this.http.get("http://localhost:8080/lcd/soll").subscribe((resdata: displayLcd) => {
      this.displayLive = resdata;

      this.displayLive.display.forEach((line) =>{
        this.display[line.line] = line.data;
      })
      console.log(this.display);       
    });
  }
  
}

