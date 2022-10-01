import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceVersion } from 'src/app/service/serviceVersion';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less'],
})
export class LoginComponent {
  constructor(public dialog: MatDialog, public rest: ServiceRest, public serviceVersion: ServiceVersion) { }

  public sUsername: string;
  public sPasswort: string;

  public login(): void {
    this.rest.setLoginData(this.sUsername, this.sPasswort);
    this.rest.get("wipf/up").then(res => {
      if (res == 1) {
        this.sUsername = "";
        this.sPasswort = "";
        this.rest.setLoginOk("true");
        //window.location.reload(); TODO: neuladen des Menüs
      } else {
        this.logoff();
      }
    });
  }

  public logoff(): void {
    this.rest.doLogout();
  }

}
