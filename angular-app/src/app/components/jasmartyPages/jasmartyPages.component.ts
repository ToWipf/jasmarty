import { Component, OnInit, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig, japage, buttonaction } from "src/app/datatypes";
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog,
} from "@angular/material/dialog";
import { JasmartyActionsComponentDialog } from "../jasmartyActions/jasmartyActions.component";

@Component({
  selector: "app-jasmartyPages",
  templateUrl: "./jasmartyPages.component.html",
  styleUrls: ["./jasmartyPages.component.less"],
})
export class JasmartyPagesComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog) {}

  public sText: string;
  public sStatus: string;
  public jaconfig: jaconfig = {};
  public selectedPage: number = 1;
  public japage: japage = {};

  public optionnumbers: number[] = [48, 49, 50];

  ngOnInit() {
    this.loadConfig();
    this.getSite();
  }

  public loadConfig(): void {
    this.http.get("http://localhost:8080/config/get").subscribe((resdata) => {
      this.jaconfig = resdata;
    });
  }

  public next(): void {
    this.selectedPage++;
    this.getSite();
  }

  public last(): void {
    if (this.selectedPage > 1) {
      this.selectedPage--;
    }
    this.getSite();
  }

  public toPage(pg: number): void {
    this.selectedPage = pg;
    this.getSite();
  }

  public save(): void {
    // var sLines: string = "";
    // this.lines.forEach((line) => {
    //   if (sLines.length != 0) {
    //     sLines = sLines + "\n";
    //   }
    //   sLines = sLines + line;
    // });

    // this.japage.lines = sLines;

    this.http
      .post("http://localhost:8080/pages/save", JSON.stringify(this.japage))
      .subscribe((resdata: any) => {
        if (resdata.save) {
          console.log("saved");
        } else {
          //TODO: Meldung Fehler
          console.log("fehler");
        }
      });
  }

  private getSite(): void {
    if (this.selectedPage > 2147483647 || this.selectedPage < 1) {
      this.selectedPage = 1;
      return;
    }

    this.http
      .get("http://localhost:8080/pages/get/" + this.selectedPage)
      .subscribe((resdata: japage) => {
        this.japage = resdata;

        if (this.japage.id == 0) {
          this.newPage();
        }
      });
  }

  public selectPage(): void {
    this.http
      .get("http://localhost:8080/pages/select/" + this.selectedPage)
      .subscribe((resdata) => {
        console.log(resdata);
      });
  }

  public deletePage(): void {
    this.http
      .get("http://localhost:8080/pages/delete/" + this.selectedPage)
      .subscribe((resdata) => {
        this.getSite();
      });
  }

  private newPage() {
    this.japage = {};
    this.japage.id = this.selectedPage;
    this.japage.name = "neue Seite";
    this.japage.lines = [];
    for (let index = 0; index < this.jaconfig.height; index++) {
      this.japage.lines.push({
        data: "",
        line: index,
        option: 48,
      });
    }
  }

  public trackByIndex(index: number, obj: any): any {
    return index;
  }

  public saveAndSelectPage(): void {
    this.save();
    setTimeout(() => {
      this.selectPage();
    }, 1000);
  }

  public openDialogGoToPage(): void {
    var goToLine: number = this.selectedPage;

    const dialogRef = this.dialog.open(JasmartyPagesComponentGoToDialog, {
      width: "250px",
      height: "250px",
      data: goToLine,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.toPage(Math.round(result));
      }
    });
  }
}

@Component({
  selector: "app-jasmartyPages-goToDialog",
  templateUrl: "./jasmartyPages.goToDialog.html",
})
export class JasmartyPagesComponentGoToDialog {
  constructor(
    public dialogRef: MatDialogRef<JasmartyActionsComponentDialog>,
    @Inject(MAT_DIALOG_DATA) public data: number
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
