import { Component } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { ServiceVersion } from 'src/app/service/serviceVersion';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.less'],
})
export class FooterComponent {
  constructor(public dialog: MatDialog, public rest: ServiceRest, public serviceVersion: ServiceVersion) { }

}
