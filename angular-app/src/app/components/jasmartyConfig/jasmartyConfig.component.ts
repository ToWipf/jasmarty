import { Component, OnInit } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";

@Component({
  selector: "app-jasmartyConfig",
  templateUrl: "./jasmartyConfig.component.html",
  styleUrls: ["./jasmartyConfig.component.less"],
})
export class JasmartyConfigComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public jaconfig: jaconfig = {};
  public ports: [{ name: string }];

  ngOnInit() {
    this.load();
    this.getPorts();
  }

  public load(): void {
    this.http.get("http://localhost:8080/config/get").subscribe((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public save(): void {
    this.http.post("http://localhost:8080/config/set", JSON.stringify(this.jaconfig)).subscribe((resdata: any) => {
      if (resdata.save) {
        console.log("saved");
      } else {
        //TODO: Meldung Fehler
        console.log("fehler");
      }
    });
  }

  public getPorts(): void {
    this.http.get("http://localhost:8080/wipf/ports").subscribe((resdata: any) => {
      console.log(resdata);
      this.ports = resdata.list;
    });
  }

  public start(): void {
    this.http.get("http://localhost:8080/lcd/open").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public stop(): void {
    this.http.get("http://localhost:8080/lcd/open").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOn(): void {
    this.http.get("http://localhost:8080/refresh/on").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.http.get("http://localhost:8080/refresh/off").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }
}
