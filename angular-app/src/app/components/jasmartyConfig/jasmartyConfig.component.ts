import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-jasmartyConfig",
  templateUrl: "./jasmartyConfig.component.html",
  styleUrls: ["./jasmartyConfig.component.less"]
})
export class JasmartyConfigComponent {
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
