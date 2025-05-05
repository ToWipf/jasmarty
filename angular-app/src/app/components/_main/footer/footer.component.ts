import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { ServiceVersion } from 'src/app/service/serviceVersion';
import { ServicAuthKey } from 'src/app/service/serviceAuthKey';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.less'],
})
export class FooterComponent implements OnInit {
  constructor(public dialog: MatDialog, public rest: ServiceRest, public serviceVersion: ServiceVersion, public serviceAuthKey: ServicAuthKey) { }

  ngOnInit(): void {
    this.serviceAuthKey.loadAuthKey();
    if (!this.serviceAuthKey.getAuthKey()) {
      this.serviceAuthKey.setRndAuthKey();
    }
    this.serviceAuthKey.updateCookieExpiry();
  }
}
