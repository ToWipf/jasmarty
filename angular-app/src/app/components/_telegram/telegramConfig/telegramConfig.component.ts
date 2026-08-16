import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedMaterialModule } from 'src/app/shared/shared-material.module';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
    selector: 'app-telegram-config',
    templateUrl: './telegramConfig.component.html',
    styleUrls: ['./telegramConfig.component.less'],
    standalone: true,
    imports: [CommonModule, SharedMaterialModule]
})
export class TelegramConfigComponent implements OnInit {
  constructor(private rest: ServiceRest) { }

  public sBotKey: string = "";
  public bTelegramActive: boolean = false;

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
