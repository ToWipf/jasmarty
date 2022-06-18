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
    this.rest.get(this.rest.gethost() + 'wipf/ver').then(
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
    const dialogRef = this.dialog.open(FooterComponentSetServerDialog, {
      width: '250px',
      height: '300px',
      data: this.rest.gethost(),
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.sethost(result);
        this.getVersion();
      }
    });
  }
}

@Component({
  selector: 'app-footer-setServer',
  templateUrl: './footer.setServer.html',
})
export class FooterComponentSetServerDialog {
  constructor(public dialogRef: MatDialogRef<FooterComponentSetServerDialog>, @Inject(MAT_DIALOG_DATA) public data: string) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
