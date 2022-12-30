import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less'],
})
export class LoginComponent {
  constructor(public dialog: MatDialog, public rest: ServiceRest) { }

  public sUsername: string;
  public sPasswort: string;

  public login(): void {
    this.rest.setLoginData(this.sUsername, this.sPasswort);
    this.rest.get("wipf/up").then(res => {
      if (res == 1) {
        this.sUsername = "";
        this.sPasswort = "";
        this.rest.setLoginOk("true");
      } else {
        this.logoff();
      }
    });
  }

  public logoff(): void {
    this.rest.doLogout();
  }

}
