import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogWartenComponent } from '../dialog/main.dialog';
import { ElementSetServerDialog } from '../dialog/setServer.dialog';
import { ServiceWipf } from './serviceWipf';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class ServiceRest {
  constructor(private http: HttpClient, public dialog: MatDialog, public serviceWipf: ServiceWipf) { }

  // HttpClientModule

  private sHost: string = 'http://localhost:8080/';
  private httpOptions: HttpHeaders = new HttpHeaders();
  private bLoginOk: string = "false";
  private PWCRYPTKEY: string = "TODO:KEY"

  public setLoginData(user: string, passwort: string): void {
    // this.sAuth = Buffer.from(user + ":" + passwort, 'base64').toString();
    const sAuth = btoa(user + ":" + passwort);

    this.setLogin(sAuth);

    // Anmeldedaten speichern

    localStorage.setItem('auth', this.serviceWipf.crypt(sAuth, this.PWCRYPTKEY));
  }

  private setLogin(base64Auth: string) {
    this.httpOptions =
      new HttpHeaders({ "Authorization": "Basic " + base64Auth });
  }

  public getHostExpectFromUrl(): string {
    let sHref = window.location.href;
    let sTmp = sHref.substring(0, sHref.lastIndexOf('/'));
    return sTmp.substring(0, sTmp.lastIndexOf('/') + 1);
  }

  public setHostExpect(): void {
    // den letzen Apihost laden
    let apihost = localStorage.getItem("apihost");
    if (!apihost) {
      let sHostExp = this.getHostExpectFromUrl();
      // Wenn keine sinvolle "Domain" vorhanden ist, nichts setzen
      if (sHostExp.length > 10) {
        this.sHost = sHostExp;
      }
    } else {
      this.sHost = apihost;
    }

    // den letzen auth laden, wenn vorhanden
    const lastAuth = this.serviceWipf.decrypt(localStorage.getItem('auth'), this.PWCRYPTKEY);
    if (lastAuth) {
      this.setLogin(lastAuth);
      this.setLoginOk("may");
    }
  }

  public doLogout(): void {
    this.setLoginData("", "");
    this.setLoginOk("false");
    localStorage.removeItem('auth');
    localStorage.removeItem('apihost');
  }

  public gethost(): string {
    return this.sHost;
  }

  public sethost(host: string): void {
    this.sHost = host;
    localStorage.setItem("apihost", host);
  }

  public getLoginOk(): string {
    return this.bLoginOk;
  }

  public setLoginOk(b: string): void {
    this.bLoginOk = b;
  }

  public openSetServerDialog(): Promise<boolean> {
    return new Promise(resolve => {
      const dialogRef = this.dialog.open(ElementSetServerDialog, {
        minWidth: '200px',
        height: '300px',
        data: this.gethost(),
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.sethost(result);
          resolve(true);
        } else {
          resolve(false);
        }
      });
    });
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * http get
   * 
   * @param path 
   * @returns 
   */
  public get(path: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.get(this.gethost() + path, { headers: this.httpOptions }).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  public getPlain(path: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.get(this.gethost() + path, { headers: this.httpOptions, responseType: 'text' }).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  /**
   * http get ohne Warten Dialog
   * 
   * @param path 
   * @returns 
   */
  public getNoWartenDialog(path: string): Promise<any> {
    return new Promise(
      resolve => {
        this.http.get(this.gethost() + path, { headers: this.httpOptions }).subscribe((resdata: any) => {
          resolve(resdata);
        });
      });
  }

  /**
   * http post
   * 
   * @param path 
   * @param item 
   * @returns 
   */
  public post(path: string, item: any): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.post(this.gethost() + path, item, { headers: this.httpOptions }).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  /**
   * http post
   * 
   * @param path 
   * @param item 
   * @returns 
   */
  public postNoWartenDialog(path: string, item: any): Promise<any> {
    return new Promise(
      resolve => {
        this.http.post(this.gethost() + path, item, { headers: this.httpOptions }).subscribe((resdata: any) => {
          resolve(resdata);
        });
      });
  }

  /**
   * http delete
   * 
   * @param path 
   * @returns 
   */
  public delete(path: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.delete(this.gethost() + path, { headers: this.httpOptions }).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  /**
   * http file Download
   * 
   * @param path 
   * @returns 
   */
  public downloadFile(path: string): Observable<HttpResponse<Blob>> {
    return this.http.get<Blob>(this.gethost() + path, { headers: this.httpOptions, observe: 'response', responseType: 'blob' as 'json' });
  }

  /**
   * 
   * @param fullpath 
   * @returns 
   */
  public getExternalDomain(fullpath: string): Promise<any> {
    return new Promise(
      resolve => {
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.http.get(fullpath, { headers: this.httpOptions }).subscribe((resdata: any) => {
          warten.close();
          resolve(resdata);
        });
      });
  }

  /**
   * 
   * @param param 
   * @returns 
   */
  public getConfigParam(param: string): Promise<string> {
    return new Promise(
      resolve => {
        this.getNoWartenDialog('basesettings/' + param).then((resdata: any) => {
          resolve(resdata.param);
        });
      });
  }

}
