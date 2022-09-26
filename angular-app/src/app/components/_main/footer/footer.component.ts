import { Component, OnInit, Inject } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import packageJson from '../../../../../package.json';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.less'],
})
export class FooterComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest) { }

  public sAppVersion: string = packageJson.version;
  public sJavaVersion: string = '0.0';
  public bOldVersionWarn: boolean = false;
  public bCantLoad: boolean = true;

  ngOnInit() {
    this.getVersion();
  }

  public getVersion(): void {
    this.rest.get('wipf/ver').then(
      (resdata: any) => {
        this.bCantLoad = false;
        this.sJavaVersion = resdata.ver.toString();
        if (this.sAppVersion !== this.sJavaVersion) {
          this.bOldVersionWarn = true;
        }
      },
    );
  }

  public openSetServer(): void {
    this.rest.openSetServer().then((b) => {
      this.getVersion();
    });
  }

  public reloadPage(): void {
    window.location.reload();
  }
}
