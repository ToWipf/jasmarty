import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-jasmartyLive",
  templateUrl: "./jasmartyLive.component.html",
  styleUrls: ["./jasmartyLive.component.less"],
})
export class JasmartyLiveComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public display: string;

  ngOnInit() {
    this.getLcdSoll();
  }

  private getLcdSoll(): void {
    this.http.get("http://localhost:8080/lcd/soll").subscribe((resdata: any) => {
      console.log(resdata);
      this.display = resdata;
    });
  }
}
