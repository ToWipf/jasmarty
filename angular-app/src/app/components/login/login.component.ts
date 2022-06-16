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
  
  send() {
    this.rest.setLoginData(this.sUsername, this.sPasswort);
  }

}
