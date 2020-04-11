import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig, japage } from "src/app/datatypes";

@Component({
  selector: "app-jasmarty",
  templateUrl: "./jasmarty.component.html",
  styleUrls: ["./jasmarty.component.less"],
})
export class JasmartyComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public sText: string;
  public sStatus: string;
  public jaconfig: jaconfig = {};
  public japage: japage = {};
  public selectedSite: number = 0;

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.http.get("http://localhost:8080/config/get").subscribe((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public next(): void {
    this.selectedSite++;
    this.getSite();
  }

  public last(): void {
    if (this.selectedSite > 0) {
      this.selectedSite--;
    }
    this.getSite();
  }

  public save(): void {}

  private getSite(): void {
    this.http.get("http://localhost:8080/pages/get/" + this.selectedSite).subscribe((resdata: japage) => {
      this.japage = resdata;
    });
  }
}
