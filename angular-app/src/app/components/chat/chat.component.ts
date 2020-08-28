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

  public tMsg: Telegram = { active: '', antwort: '', chatid: 0, data: '', date: 0, editby: 0, from: 'website', id: 0, message: '', options: '', type: 'website' };
  public textOut: string = '-';
  // TODO: out !

  public send(): void {
    this.http.post(this.rest.gethost() + 'telegram/chat', this.tMsg).subscribe((resdata: any) => {
      this.textOut = resdata.msg;
    });
  }



}
