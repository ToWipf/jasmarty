import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-jasmartyConfig",
  templateUrl: "./jasmartyConfig.component.html",
  styleUrls: ["./jasmartyConfig.component.less"],
})
export class JasmartyConfigComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) {}

  public jaconfig: jaconfig = {};
  public ports: [{ name: string }];
  public bJasmartyActive: boolean;

  ngOnInit() {
    this.load();
    this.getPorts();
    this.getJasmartyActive();
  }

  public load(): void {
    this.http.get(this.rest.gethost() + "lcd/config/get").subscribe((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public save(): void {
    this.http.post(this.rest.gethost() + "lcd/config/set", JSON.stringify(this.jaconfig)).subscribe((resdata: any) => {
      if (resdata.save) {
        console.log("saved");
        this.startLcdAgain();
      } else {
        //TODO: Meldung Fehler
        console.log("fehler");
      }
    });
  }

  public startLcdAgain(): void {
    this.http.get(this.rest.gethost() + "debug/jasmarty/restart").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public getPorts(): void {
    this.http.get(this.rest.gethost() + "wipf/ports").subscribe((resdata: any) => {
      console.log(resdata);
      this.ports = resdata.list;
    });
  }

  public start(): void {
    this.http.get(this.rest.gethost() + "lcd/open").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public stop(): void {
    this.http.get(this.rest.gethost() + "lcd/open").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOn(): void {
    this.http.get(this.rest.gethost() + "refresh/on").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.http.get(this.rest.gethost() + "refresh/off").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public stopApp(): void {
    this.http.get(this.rest.gethost() + "wipf/stop").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public getJasmartyActive(): void {
    this.http.get(this.rest.gethost() + "basesettings/get/jasmarty").subscribe((resdata: any) => {
      this.bJasmartyActive = resdata.active;
    });
  }

  public setJasmartyActive(bStatus: boolean): void {
    this.http.post(this.rest.gethost() + "basesettings/set/jasmarty/" + bStatus,"").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }
}
