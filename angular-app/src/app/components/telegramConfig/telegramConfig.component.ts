import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ServiceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-telegramConfig",
  templateUrl: "./telegramConfig.component.html",
  styleUrls: ["./telegramConfig.component.less"],
})
export class TelegramConfigComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) {}

  public todo: string;

  ngOnInit() {
    // TODO:
  }

  public save(): void {
    // TODO:
  }

  public refreshOn(): void {
    this.http.get(this.rest.gethost() + "telegram/on").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.http.get(this.rest.gethost() + "telegram/off").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public stopApp(): void {
    this.http.get(this.rest.gethost() + "wipf/stop").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }
}
