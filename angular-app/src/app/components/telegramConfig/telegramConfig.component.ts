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

  public sText: string;
  public sStatus: string;
  public sBotKey: string;
  public bTelegramActive: boolean;

  ngOnInit() {
    this.getTelegramActive();
    this.getBotKey();
  }

  public getBotKey(): void {
    this.http.get(this.rest.gethost() + "telegram/getbot").subscribe((resdata: any) => {
      this.sBotKey = resdata.botkey;
    });
  }

  public setBotKey(): void {
    this.http.post(this.rest.gethost() + "telegram/setbot/" + this.sBotKey, "").subscribe((resdata: any) => {
      console.log(resdata);
    });
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

  public sendMsgToGroup(): void {
    if (this.sText) {
      //TODO: escape input String (TEXTBOX?)
      this.http.post(this.rest.gethost() + "telegram/sendMsgToGroup/" + this.sText, null).subscribe((resdata) => {
        this.sStatus = resdata.toString();
        this.sText = null;
      });
    }
  }

  public getTelegramActive(): void {
    this.http.get(this.rest.gethost() + "basesettings/get/telegram").subscribe((resdata: any) => {
      this.bTelegramActive = resdata.active;
    });
  }

  public setTelegramActive(bStatus: boolean): void {
    this.http.post(this.rest.gethost() + "basesettings/set/telegram/" + bStatus, "").subscribe((resdata: any) => {
      console.log(resdata);
    });
  }
}
