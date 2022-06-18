import { Component } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { CryptItem } from 'src/app/datatypes';

@Component({
  selector: 'app-crypt',
  templateUrl: './crypt.component.html',
  styleUrls: ['./crypt.component.less'],
})
export class CryptComponent {
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public crItem: CryptItem = { data: '', key: '' };
  public textOut: string = '-';
  // TODO: out !

  public crypt(): void {
    this.rest.post('crypt/encrypt', this.crItem).then((resdata: any) => {
      this.textOut = resdata.data;
    });
  }

  public decrypt(): void {
    this.rest.post('crypt/decrypt', this.crItem).then((resdata: any) => {
      this.textOut = resdata.data;
    });
  }

}
