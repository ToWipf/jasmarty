import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-telegram-config',
  templateUrl: './telegramConfig.component.html',
  styleUrls: ['./telegramConfig.component.less'],
})
export class TelegramConfigComponent implements OnInit {
  constructor( private rest: ServiceRest) {}

  public sText: string;
  public sStatus: string;
  public sBotKey: string;
  public bTelegramActive: boolean;

  ngOnInit() {
    this.getTelegramActive();
    this.getBotKey();
  }

  public getBotKey(): void {
    this.rest.get('telegram/getbot').then((resdata: any) => {
      this.sBotKey = resdata.botkey;
    });
  }

  public setBotKey(): void {
    this.rest.post('telegram/setbot/' + this.sBotKey, '').then((resdata: any) => {
      console.log(resdata);
      this.refreshOn();
    });
  }

  public refreshOn(): void {
    this.rest.get('telegram/on').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public refreshOff(): void {
    this.rest.get('telegram/off').then((resdata: any) => {
      console.log(resdata);
    });
  }

  public sendMsgToGroup(): void {
    if (this.sText) {
      //TODO: escape input String (TEXTBOX?)
      this.rest.post('telegram/sendMsgToAdmin/' + this.sText, null).then((resdata) => {
        this.sStatus = resdata.toString();
        this.sText = null;
      });
    }
  }

  public getTelegramActive(): void {
    this.rest.get('basesettings/get/telegram').then((resdata: any) => {
      this.bTelegramActive = resdata.active;
    });
  }

  public setTelegramActive(bStatus: boolean): void {
    this.rest.post('basesettings/set/telegram/' + bStatus, '').then((resdata: any) => {
      console.log(resdata);
    });
  }
}
