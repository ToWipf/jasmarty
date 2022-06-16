import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogWartenComponent } from '../dialog/main.dialog';
import { ServiceWipf } from './serviceWipf';

@Injectable({
  providedIn: 'root',
})

export class ServiceRest {
  constructor(private http: HttpClient, public dialog: MatDialog, public serviceWipf: ServiceWipf) { }

  //private host: string = '';
  private sHost: string = 'http://localhost:8080/';
  private httpOptions: any;

  public gethost(): string {
    return this.sHost;
  }

  public sethost(host: string): void {
    this.sHost = host;
  }

  public setLoginData(user: string, passwort: string) {
    // this.sAuth = Buffer.from(user + ":" + passwort, 'base64').toString();
    this.httpOptions = {
      headers: new HttpHeaders({
        "Authorization": "Basic " + btoa(user + ":" + passwort)
      })
    };
  }

  public sethostExpect(): void {
    let sHref = window.location.href;
    let sTmp = sHref.substring(0, sHref.lastIndexOf('/'));
    const sHostExp = sTmp.substring(0, sTmp.lastIndexOf('/') + 1);
    // Wenn keine sinvolle "Domain" vorhanden ist, nichts setzen
    if (sHostExp.length > 10) {
      this.sHost = sHostExp;
    }
  }

  public httpGet(path: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.get(this.gethost() + path, this.httpOptions).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  public httpPost(path: string, item: any): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.post(this.gethost() + path, item, this.httpOptions).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  public httpDelete(path: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.delete(this.gethost() + path, this.httpOptions).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

}
