import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig, japage } from "src/app/datatypes";

@Component({
  selector: "app-jasmartyPages",
  templateUrl: "./jasmartyPages.component.html",
  styleUrls: ["./jasmartyPages.component.less"],
})
export class JasmartyPagesComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public sText: string;
  public sStatus: string;
  public jaconfig: jaconfig = {};
  public selectedPage: number = 1;
  public japage: japage = {};

  public optionnumbers: number[] = [48,49,50];

  ngOnInit() {
    this.load();
    this.getSite();
  }

  public load(): void {
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

  // public setOptionForLine(line: number, option: number) {
  //   const opt =
  //     this.japage.options.substr(0, line) +
  //     option +
  //     this.japage.options.substr(line + this.japage.options.length);
  //   this.japage.options = opt;
  // }

  public saveAndSelectPage() {
    this.save();
    setTimeout(() => {
      this.selectPage();
    }, 1000);
  }
}
