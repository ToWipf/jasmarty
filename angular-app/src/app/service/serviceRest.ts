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
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  private host: string = '';
  //private host: string = 'http://localhost:8080/';

  public gethost(): string {
    return this.host;
  }

  public sethost(host: string): void {
    this.host = host;
  }

  public sethostExpect(): void {
    let sHref = window.location.href;
    let sTmp = sHref.substring(0, sHref.lastIndexOf('/'));
    this.host = sTmp.substring(0, sTmp.lastIndexOf('/') + 1);
  }

  public doHttp(path: string): any {

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Basic my-auth-token'
      })
    };


    const warten = this.dialog.open(DialogWartenComponent, {});
    this.http.get(this.rest.gethost() + path, httpOptions).subscribe((resdata: any) => {
      warten.close();
      return resdata;
    });
  }

}
