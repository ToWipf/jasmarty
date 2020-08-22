import { Component } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { HttpClient } from '@angular/common/http';
import { CryptItem } from 'src/app/datatypes';

@Component({
  selector: 'app-crypt',
  templateUrl: './crypt.component.html',
  styleUrls: ['./crypt.component.less'],
})
export class CryptComponent {
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf) {

  }

  public crItem: CryptItem = { data: "", key: "" };
  public textOut: string = 'out';
  // TODO: out !

  public crypt(): void {
    this.http.post(this.rest.gethost() + 'crypt/encrypt', this.crItem).subscribe((resdata: any) => {
      this.textOut = resdata;
    });
  }

  public decrypt(): void {
    this.http.post(this.rest.gethost() + 'crypt/decrypt', this.crItem).subscribe((resdata: any) => {
      this.textOut = resdata;
    });
  }

}
