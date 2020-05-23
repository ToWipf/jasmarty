import { Component, OnInit } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";

@Component({
  selector: "app-jasmartyFullView",
  templateUrl: "./jasmartyFullView.component.html",
  styleUrls: ["./jasmartyFullView.component.less"],
})
export class JasmartyFullViewComponent implements OnInit {
  constructor(private http: HttpClient) {}
  
  public optionnumbers: number[] = [48, 49, 50, 53];

  ngOnInit() {
    //
  }
  
}

