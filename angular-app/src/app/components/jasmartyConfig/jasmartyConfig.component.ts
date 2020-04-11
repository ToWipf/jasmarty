import { Component, OnInit } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { serviceRest } from "src/app/service/serviceRest";
import { jaconfig } from 'src/app/datatypes';

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

  public load(): void {
    this.http.get("http://localhost:8080/config/get").subscribe((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public save(): void {
    this.http.post("http://localhost:8080/config/set", JSON.stringify(this.jaconfig) ).subscribe((resdata: any) => {
      if(resdata.save){
        console.log("saved");
      }else{
        //TODO: Meldung Fehler
        console.log("fehler");
      }
    });
  }
}
