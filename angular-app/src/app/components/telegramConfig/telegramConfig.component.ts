import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig } from "src/app/datatypes";

@Component({
  selector: "app-telegramConfig",
  templateUrl: "./telegramConfig.component.html",
  styleUrls: ["./telegramConfig.component.less"],
})
export class TelegramConfigComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public todo: string;

  ngOnInit() {
    // TODO:
  }

  public save(): void {
    // TODO:
  }

  public refreshOn(): void {
    this.http.get("http://localhost:8080/telegram/on").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.http.get("http://localhost:8080/telegram/off").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }

  public stopApp(): void {
    this.http.get("http://localhost:8080/wipf/stop").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }
}
