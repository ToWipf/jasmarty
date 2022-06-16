import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Telegram } from '../datatypes';
import { DialogWartenComponent } from '../dialog/main.dialog';
import { ServiceWipf } from './serviceWipf';

@Injectable({
  providedIn: 'root',
})

export class ServiceRest {
  constructor(private http: HttpClient, public dialog: MatDialog, public serviceWipf: ServiceWipf) { }

  //private host: string = '';
  private sHost: string = 'http://localhost:8080/';
  private sAuth: string = "";

  public gethost(): string {
    return this.sHost;
  }

  public sethost(host: string): void {
    this.sHost = host;
  }

  public setLoginData(user: string, passwort: string) {
    this.sAuth = btoa(user + ":" + passwort);
   // this.sAuth = Buffer.from(user + ":" + passwort, 'base64').toString();
  }

  public sethostExpect(): void {
    let sHref = window.location.href;
    let sTmp = sHref.substring(0, sHref.lastIndexOf('/'));
    const sHostExp = sTmp.substring(0, sTmp.lastIndexOf('/') + 1);
    // Wenn keine sinvolle "Domain" vorhanden ist, nichts setzen
    if (sHostExp.length > 10) {
      console.log(sHostExp);
      this.sHost = sHostExp;
    }
  }

  public doHttp(path: string): Promise<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        "Authorization": "Basic " + this.sAuth

      })
    };

    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.get(this.gethost() + path, httpOptions).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
          //return resdata;
        });
      });
  }

}
