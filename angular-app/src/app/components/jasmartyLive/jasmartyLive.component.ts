import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-jasmartyLive",
  templateUrl: "./jasmartyLive.component.html",
  styleUrls: ["./jasmartyLive.component.less"],
})
export class JasmartyLiveComponent implements OnInit, OnDestroy  {
  constructor(private http: HttpClient) {}

  public display: any;
  public loopStop: boolean = false;

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
      console.log(resdata);
      this.display = resdata;
    });
  }
}
