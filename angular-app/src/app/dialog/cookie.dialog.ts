import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { KeyValEntry } from '../datatypes';

@Component({
  selector: 'app-cookie',
  templateUrl: './cookie.dialog.html',
})
export class CookieDialog {
  constructor(public dialogRef: MatDialogRef<CookieDialog>) { }

  public inData: KeyValEntry = {key:"", val:""};
  public cookies: string = "";

  onNoClick(): void {
    this.dialogRef.close();
  }

  public listCookies(): void {
    var theCookies = document.cookie.split(';');
    var aString = '';
    for (var i = 1; i <= theCookies.length; i++) {
      aString += i + ' ' + theCookies[i - 1] + "\n";
    }
    this.cookies = aString;
  }

  // public getCookie(cname: string): string {
  //   let name = cname + "=";
  //   let decodedCookie = decodeURIComponent(document.cookie);
  //   let ca = decodedCookie.split(';');
  //   for (let i = 0; i < ca.length; i++) {
  //     let c = ca[i];
  //     while (c.charAt(0) == ' ') {
  //       c = c.substring(1);
  //     }
  //     if (c.indexOf(name) == 0) {
  //       return c.substring(name.length, c.length);
  //     }
  //   }
  //   return "";
  // }

  public setCookie(cname: String, val: String): void {
    console.log(val);
    var xnow = new Date();
    var time = xnow.getTime();
    var expireTime = time + 1000 * 36000 * 9999;
    xnow.setTime(expireTime);
    document.cookie = cname + '=' + val + ';expires=' + xnow.toUTCString() + ';path=/;SameSite=Strict';

    this.listCookies();
  }

}
