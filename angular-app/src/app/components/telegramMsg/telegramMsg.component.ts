import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-telegram-msg',
  templateUrl: './telegramMsg.component.html',
  styleUrls: ['./telegramMsg.component.less'],
})
export class TelegramMsgComponent implements OnInit  {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {}

  ngOnInit() {
    this.load();
  }

  private load(): void {
    // this.http.get(this.rest.gethost() + 'filme/getAll').subscribe((resdata: Telegram[]) => {
    //   console.log(resdata);
    // });
    //TODO:
  }

}
