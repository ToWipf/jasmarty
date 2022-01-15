import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less'],
})
export class loginComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest) {}

  public sUsername: string;
  public sPasswort: string;
  public bLoginOk: boolean;
  
  ngOnInit() {
    // TODO: login Seite
  }

}
