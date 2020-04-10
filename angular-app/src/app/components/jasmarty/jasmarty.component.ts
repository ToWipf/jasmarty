import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-jasmarty",
  templateUrl: "./jasmarty.component.html",
  styleUrls: ["./jasmarty.component.less"]
})
export class JasmartyComponent {
  constructor(private http: HttpClient) {}

  public sText: string;
  public sStatus: string;

  public sendMsgToGroup(): void {
    if (this.sText) {
      //TODO: escape String
      this.http
        .post(
          "http://192.168.2.10:8080/telegram/sendMsgToGroup/" + this.sText,
          null
        )
        .subscribe(resdata => {
          this.sStatus = resdata.toString();
          this.sText = null;
        });
    }
  }
}
