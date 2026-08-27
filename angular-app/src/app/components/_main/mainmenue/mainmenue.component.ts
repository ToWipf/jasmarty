import { Component, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceVersion } from 'src/app/service/serviceVersion';
import { ListeComponent } from 'src/app/components/_wipf/liste/liste.component';
import { TelegramLogComponent } from 'src/app/components/_telegram/telegramLog/telegramLog.component';
import { TelegramMsgComponent } from 'src/app/components/_telegram/telegramMsg/telegramMsg.component';
import { WipfUserVwComponent } from 'src/app/components/_main/wipfUserVw/wipfUserVw.component';
import { AuthKeyComponent } from 'src/app/components/_main/authKey/authKey.component';
import { Jasmarty12864PanelComponent } from 'src/app/components/_jasmarty/jasmarty12864Panel/jasmarty12864Panel.component';
import { Jasmarty12864PagesComponent } from 'src/app/components/_jasmarty/jasmarty12864Pages/jasmarty12864Pages.component';
import { DebugSeiteComponent } from 'src/app/components/_debug/debugSeite/debugSeite.component';
import { JasmartyConfigComponent } from 'src/app/components/_jasmarty/jasmartyConfig/jasmartyConfig.component';
import { JasmartyActionsComponent } from 'src/app/components/_jasmarty/jasmartyActions/jasmartyActions.component';
import { TelegramConfigComponent } from 'src/app/components/_telegram/telegramConfig/telegramConfig.component';
import { TelegramChatComponent } from 'src/app/components/_telegram/telegramChat/telegramChat.component';
import { MedienComponent } from 'src/app/components/_wipf/medien/medien.component';
import { DayLogComponent } from 'src/app/components/_wipf/daylog/daylog.component';
import { FileVwComponent } from 'src/app/components/_main/fileVw/fileVw.component';
import { SettingsComponent } from 'src/app/components/_main/settings/settings.component';
import { RndEventComponent } from 'src/app/components/_wipf/rndEvent/rndEvent.component';
import { DaylogStatsComponent } from 'src/app/components/_wipf/daylogStats/daylogStats.component';
import { LoginComponent } from 'src/app/components/_main/login/login.component';
import { EisenbahnMitlesenComponent } from 'src/app/components/_eisenbahn/mitlesen/eisenbahn-mitlesen.component';
import { CommonModule } from '@angular/common';
import { SharedMaterialModule } from 'src/app/shared/shared-material.module';
import { GlowiComponent } from 'src/app/components/glowi/glowi.component';
import { View360Component } from 'src/app/components/_debug/view360/view360.component';
import { DaylogKalenderComponent } from 'src/app/components/_wipf/daylogKalender/daylogKalender.component';
import { ChecklisteComponent } from 'src/app/components/_wipf/checkliste/checkliste.component';
import { FooterComponent } from 'src/app/components/_main/footer/footer.component';

@Component({
    selector: 'app-mainmenue',
    templateUrl: './mainmenue.component.html',
    styleUrls: ['./mainmenue.component.less'],
    standalone: true,
    imports: [CommonModule, SharedMaterialModule, ListeComponent, GlowiComponent, View360Component, DaylogKalenderComponent, ChecklisteComponent, FooterComponent, DayLogComponent, FileVwComponent, SettingsComponent, RndEventComponent, DaylogStatsComponent, LoginComponent, EisenbahnMitlesenComponent, TelegramLogComponent, TelegramMsgComponent, WipfUserVwComponent, AuthKeyComponent, Jasmarty12864PanelComponent, Jasmarty12864PagesComponent, DebugSeiteComponent, JasmartyActionsComponent, TelegramConfigComponent, TelegramChatComponent, MedienComponent, JasmartyConfigComponent]
})
export class MainmenueComponent implements OnInit {

  constructor(private rest: ServiceRest, private serviceVersion: ServiceVersion, public listevw: ListeComponent) { }

  @ViewChild(MatDrawer, { static: true }) drawer!: MatDrawer;

  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public bWipfActive: boolean = false;
  public bHideMenueButtonAndFooter: boolean = false;
  public bShowMenue: boolean = false;
  public bEisenbahnMitlesenActive: boolean = false;
  public selectedSite: WritableSignal<string> = signal('login');

  ngOnInit(): void {
    this.rest.setHostExpect();
    this.serviceVersion.loadBackend();
    this.getActiveModules();
  }

  public selectSite(s: string): void {
    this.bShowMenue = false;
    this.selectedSite.set(s);
  }

  public useQuickLink(s: string): void {
    this.selectedSite.set(s);
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
