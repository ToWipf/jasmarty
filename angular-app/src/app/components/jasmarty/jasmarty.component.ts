import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { jaconfig, japage } from "src/app/datatypes";

@Component({
  selector: "app-jasmarty",
  templateUrl: "./jasmarty.component.html",
  styleUrls: ["./jasmarty.component.less"],
})
export class JasmartyComponent implements OnInit {
  constructor(private http: HttpClient) {}

  public sText: string;
  public sStatus: string;
  public jaconfig: jaconfig = {};
  public japage: japage = {};
  public selectedPage: number = 0;
  public lines: string[] = [];

  ngOnInit() {
    this.load();
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
    if (this.selectedPage > 0) {
      this.selectedPage--;
    }
    this.getSite();
  }

  public save(): void {
    var sLines: string = "";
    this.lines.forEach((line) => {
      if (sLines.length != 0) {
        sLines = sLines + "\n";
      }
      sLines = sLines + line;
    });

    this.japage.lines = sLines;

    this.http.post("http://localhost:8080/pages/save", JSON.stringify(this.japage)).subscribe((resdata: any) => {
      if (resdata.save) {
        console.log("saved");
      } else {
        //TODO: Meldung Fehler
        console.log("fehler");
      }
    });
  }

  private getSite(): void {
    this.http.get("http://localhost:8080/pages/get/" + this.selectedPage).subscribe((resdata: japage) => {
      this.japage = resdata;
      this.lines = this.japage.lines.substring(0, this.japage.lines.length).split("\n");
    });
  }

  public selectPage(): void {
    this.http.get("http://localhost:8080/pages/select/" + this.selectedPage).subscribe((resdata) => {
      console.log(resdata);
    });
  }

  public newPage() {
    this.lines = [];
    this.japage = {};
    this.japage.id = this.selectedPage;
    this.japage.options = "00000000"; //TODO: automatisch ermitteln
    for (let index = 0; index < this.jaconfig.height; index++) {
      this.lines.push("");
    }
  }
}
