import { Component, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { ServiceVersion } from 'src/app/service/serviceVersion';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.less'],
})
export class FooterComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public version: ServiceVersion) { }

  public sFrontendVersion: string = "0.0"

  ngOnInit() {
    this.version.loadBackend();
    this.sFrontendVersion = this.version.getFrontendVersion();
  }

  public openSetServer(): void {
    this.rest.openSetServer().then((b) => {
    });
  }

  public reloadPage(): void {
    window.location.reload();
  }
}
