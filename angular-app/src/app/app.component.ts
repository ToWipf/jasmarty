import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Jaconfig } from './datatypes';
import { ServiceRest } from './service/serviceRest';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
})
export class AppComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) {}
  
  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public jasmartyType: string;
  
  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;
  
  public selectedSite: string = 'firstpage';

  ngOnInit(): void {
    this.getActiveModules();
  }
  
  public selectSite(s: string) {
    this.drawer.toggle();
    this.selectedSite = s;
  }
  
  public getActiveModules(): void {
    this.bDevActive = false;
    this.http.get(this.rest.gethost() + 'basesettings/get/telegram').subscribe((resdata: any) => {
      this.bTelegramActive = resdata.active;
    });
    this.http.get(this.rest.gethost() + 'basesettings/get/jasmarty').subscribe((resdata: any) => {
      this.bJasmartyActive = resdata.active;
    });

    this.http.get(this.rest.gethost() + 'lcd/config/get').subscribe((resdata) => {
      var jaconfig: Jaconfig = resdata;
      this.jasmartyType = jaconfig.type;
    });
  }

  public showDevModules(): void {
    this.bDevActive = !this.bDevActive;
  }

  public showAll2004(): void {
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.jasmartyType = "LCD_2004";
  }

  public showAll12864(): void {
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.jasmartyType = "LCD_12864";
  }

}
