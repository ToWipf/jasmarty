import { Component, OnInit, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Globals } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";

@Component({
  selector: "app-footer",
  templateUrl: "./footer.component.html",
  styleUrls: ["./footer.component.less"],
})
export class FooterComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, public globals: Globals, private rest: ServiceRest) {}

  public sAppVersion: string = this.globals.version;
  public sJavaVersion: string = "0.0";
  public bOldVersionWarn: boolean = false;
  public bCantLoad: boolean = true;
  private sHost: string = this.rest.gethost();

  ngOnInit() {
    this.getVersion();
  }

  private getVersion(): void {
    this.http.get(this.sHost + "wipf/ver").subscribe((resdata: any) => {
      this.bCantLoad = false;
      this.sJavaVersion = resdata.ver.toString();
      if (this.sAppVersion < this.sJavaVersion) {
        this.bOldVersionWarn = true;
      }
    });
  }

  public openSetServer(): void {


    const dialogRef = this.dialog.open(FooterComponentSetServerDialog, {
      width: "250px",
      height: "250px",
      data: this.sHost,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.sHost = result;
        this.rest.sethost(result);
        this.getVersion();
      }
    });
  }
}

@Component({
  selector: "app-footer-setServer",
  templateUrl: "./footer.setServer.html",
})
export class FooterComponentSetServerDialog {
  constructor(public dialogRef: MatDialogRef<FooterComponentSetServerDialog>, @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
