import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";
import { serviceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-jasmartyConfig",
  templateUrl: "./jasmartyConfig.component.html",
  styleUrls: ["./jasmartyConfig.component.less"],
})
export class JasmartyConfigComponent {
  constructor(private http: HttpClient, private rest: serviceRest) {}

  public jaconfig: jaconfig = {};

  public save(): void {
    console.log(this.jaconfig);
    this.http.post("http://localhost:8080/lcd/config", this.jaconfig).subscribe((resdata) => {
      console.log(resdata);
    });
  }

  public load(): void { // TODO: oninit
    console.log(this.jaconfig);
    this.http.get("http://localhost:8080/lcd/config").subscribe((resdata) => {
      console.log(resdata);
      // TODO:
      this.jaconfig = {baudrate:9600}
    });
  }
}
