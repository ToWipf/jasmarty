import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-telegram-config',
  templateUrl: './telegramConfig.component.html',
  styleUrls: ['./telegramConfig.component.less'],
})
export class TelegramConfigComponent implements OnInit {
  constructor( private rest: ServiceRest) {}

  public sBotKey: string;
  public bTelegramActive: boolean;

  ngOnInit() {
    this.getBotKey();
  }

  public getBotKey(): void {
    this.rest.get('telegram/getbot').then((resdata: any) => {
      this.sBotKey = resdata.botkey;
    });
  }

  public setBotKey(): void {
    this.rest.post('telegram/setbot/' + this.sBotKey, '').then((resdata: any) => {
      this.refreshOn();
    });
  }

  public refreshOn(): void {
    this.rest.get('telegram/on').then((resdata: any) => {
    });
  }

  public refreshOff(): void {
    this.rest.get('telegram/off').then((resdata: any) => {
    });
  }

}
