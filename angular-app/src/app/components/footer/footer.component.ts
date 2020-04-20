import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from 'src/app/datatypes';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.less']
})
export class FooterComponent implements OnInit {

  constructor(
    private http: HttpClient,
    public globals: Globals,
  ) { }

  public sAppVersion: string = this.globals.version;
  public sJavaVersion: string = "<Laden ...>";

  ngOnInit() {
    this.getVersion();
   }

  private getVersion(): void {
    this.http.get("http://localhost:8080/wipf/ver")
      .subscribe((resdata: any) => {
        this.sJavaVersion = resdata.ver.toString();
      });
  }
}
