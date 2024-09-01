import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceVersion } from 'src/app/service/serviceVersion';
// import { ListeComponent } from '../../_wipf/liste/liste.component';
import { ListeComponent } from 'src/app/components/_wipf/liste/liste.component';

@Component({
  selector: 'app-mainmenue',
  templateUrl: './mainmenue.component.html',
  styleUrls: ['./mainmenue.component.less']
})
export class MainmenueComponent implements OnInit {

  constructor(private rest: ServiceRest, private serviceVersion: ServiceVersion, public listevw: ListeComponent) { }

  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;

  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public bWipfActive: boolean = false;
  public bHideMenueButtonAndFooter: boolean = false;
  public bShowMenue: boolean = true;
  public bEisenbahnMitlesenActive: boolean = false;
  public selectedSite: string = 'login';

  ngOnInit(): void {
    this.rest.setHostExpect();
    this.serviceVersion.loadBackend();
    this.getActiveModules();
  }

  public selectSite(s: string): void {
    this.bShowMenue = false;
    this.selectedSite = s;
  }

  public useQuickLink(s: string): void {
    this.selectedSite = s;
    this.bHideMenueButtonAndFooter = true;
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
      });
      this.rest.getNoWartenDialog('basesettings/get/debug').then((resdata: any) => {
        this.bDevActive = resdata.active;
        if (this.bDevActive) {
          this.showAll();
        }
      });
    });
  }

  public showAll(): void {
    this.bDevActive = true;
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.bWipfActive = true;
    this.bEisenbahnMitlesenActive = true;
  }

  public addThingToList(): void {
    this.listevw.newItem();
  }
}
