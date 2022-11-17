import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Jaconfig } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceVersion } from 'src/app/service/serviceVersion';

@Component({
  selector: 'app-mainmenue',
  templateUrl: './mainmenue.component.html',
  styleUrls: ['./mainmenue.component.less']
})
export class MainmenueComponent implements OnInit {

  constructor(private rest: ServiceRest, private serviceVersion: ServiceVersion) { }


  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;

  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public bWipfActive: boolean = false;
  public bEisenbahnMitlesenActive: boolean = false;
  public jasmartyType: string;
  public selectedSite: string = 'login';

  ngOnInit(): void {
    this.rest.setHostExpect();
    this.serviceVersion.loadBackend();
    this.getActiveModules();
  }

  public selectSite(s: string) {
    this.drawer.toggle();
    this.selectedSite = s;
  }

  public getActiveModules(): void {
    this.bDevActive = false;

    this.rest.getNoWartenDialog('basesettings/get/wipf').then((resdata: any) => {
      // Weiteres nur laden, wenn das erste funktioniert
      this.bWipfActive = resdata.active;

      this.rest.getNoWartenDialog('basesettings/get/telegram').then((resdata: any) => {
        this.bTelegramActive = resdata.active;
      });

      this.rest.getNoWartenDialog('basesettings/get/eisenbahn_mitlesen').then((resdata: any) => {
        this.bEisenbahnMitlesenActive = resdata.active;
      });

      this.rest.getNoWartenDialog('basesettings/get/jasmarty').then((resdata: any) => {
        this.bJasmartyActive = resdata.active;

        if (this.bJasmartyActive) {
          // Wenn Jasmarty true ist, den Typ holen
          this.rest.getNoWartenDialog('lcd/config/get').then((resdata) => {
            var jaconfig: Jaconfig = resdata;
            this.jasmartyType = jaconfig.type;
          });
        }
      });
      this.rest.getNoWartenDialog('basesettings/get/debug').then((resdata: any) => {
        this.bDevActive = resdata.active;
        if (this.bDevActive) {
          // Beim einschalten auch 12864 zeigen
          this.showAll12864();
        }
      });
    });
  }

  public showDevModulesBySecretKey(): void {
    this.bDevActive = true
    this.showAll12864();
  }

  public showDevModules(): void {
    this.bDevActive = !this.bDevActive;
  }

  public showAll2004(): void {
    this.showAll();
    this.jasmartyType = "LCD_2004";
  }

  public showAll12864(): void {
    this.showAll();
    this.jasmartyType = "LCD_12864";
  }

  private showAll(): void {
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.bWipfActive = true;
    this.bEisenbahnMitlesenActive = true;
  }
}