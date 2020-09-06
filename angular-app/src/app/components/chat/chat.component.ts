import { Component } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { HttpClient } from '@angular/common/http';
import { Telegram } from 'src/app/datatypes';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.less'],
})
export class ChatComponent {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }

  public tMsg: Telegram = { chatid: 0, message: '', type: 'website', editby: 'website' };
  public textOut: string = '-';
  // TODO: out !

  public send(): void {
    this.http.post(this.rest.gethost() + 'telegram/chat', this.tMsg).subscribe((resdata: any) => {
      this.textOut = resdata.msg;
    });
  }



}
