import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";
import { serviceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-jasmartyConfig",
  templateUrl: "./jasmartyConfig.component.html",
  styleUrls: ["./jasmartyConfig.component.less"],
})
export class JasmartyConfigComponent implements OnInit {
  constructor(private http: HttpClient, private rest: serviceRest) {}

  public jaconfig: jaconfig = {};

  ngOnInit() {
    this.load();
  }

  public save(): void {
    console.log(this.jaconfig);
    this.http.post("http://localhost:8080/config/set", this.jaconfig).subscribe((resdata) => {
      console.log(resdata);
    });
  }

  public load(): void {
    console.log(this.jaconfig);
    this.http.get("http://localhost:8080/config/get").subscribe((resdata) => {
      console.log(resdata);
      this.jaconfig = resdata;
    });
  }
}
