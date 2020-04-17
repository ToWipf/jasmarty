import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-jasmartyActions",
  templateUrl: "./jasmartyActions.component.html",
  styleUrls: ["./jasmartyActions.component.less"],
})
export class JasmartyActionsComponent implements OnInit {
  constructor(private http: HttpClient) {}

  ngOnInit() {}
}
