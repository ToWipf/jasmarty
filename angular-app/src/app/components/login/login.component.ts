import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less'],
})
export class LoginComponent {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest) {}

  public sUsername: string;
  public sPasswort: string;
  public bLoginOk: boolean = false;

  send() {
    this.rest.setLoginData(this.sUsername, this.sPasswort);
    this.rest.doHttpGet("wipf/up").then(res => {
      if (res == 1){
        this.sUsername = "";
        this.sPasswort = "";
        this.bLoginOk = true;
      }
    });
  }

}
