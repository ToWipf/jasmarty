import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Globals } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-footer",
  templateUrl: "./footer.component.html",
  styleUrls: ["./footer.component.less"],
})
export class FooterComponent implements OnInit {
  constructor(private http: HttpClient, public globals: Globals, private rest: ServiceRest) {}

  public sAppVersion: string = this.globals.version;
  public sJavaVersion: string = "0.0";
  public bOldVersionWarn: boolean = false;
  public bCantLoad: boolean = true;

  ngOnInit() {
    this.getVersion();
  }

  private getVersion(): void {
    this.http.get(this.rest.gethost() + "wipf/ver").subscribe((resdata: any) => {
      this.bCantLoad = false;
      this.sJavaVersion = resdata.ver.toString();
      if (this.sAppVersion < this.sJavaVersion) {
        this.bOldVersionWarn = true;
      }
    });
  }
}
