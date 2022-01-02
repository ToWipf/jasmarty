import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { MatDrawer } from '@angular/material/sidenav';
import { DomSanitizer } from '@angular/platform-browser';
import { Jaconfig } from './datatypes';
import { ServiceRest } from './service/serviceRest';

const ICON_TRASH = `<svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px"><path d="M0 281.296l0 -68.355q1.953 -37.107 29.295 -62.496t64.449 -25.389l93.744 0l0 -31.248q0 -39.06 27.342 -66.402t66.402 -27.342l312.48 0q39.06 0 66.402 27.342t27.342 66.402l0 31.248l93.744 0q37.107 0 64.449 25.389t29.295 62.496l0 68.355q0 25.389 -18.553 43.943t-43.943 18.553l0 531.216q0 52.731 -36.13 88.862t-88.862 36.13l-499.968 0q-52.731 0 -88.862 -36.13t-36.13 -88.862l0 -531.216q-25.389 0 -43.943 -18.553t-18.553 -43.943zm62.496 0l749.952 0l0 -62.496q0 -13.671 -8.789 -22.46t-22.46 -8.789l-687.456 0q-13.671 0 -22.46 8.789t-8.789 22.46l0 62.496zm62.496 593.712q0 25.389 18.553 43.943t43.943 18.553l499.968 0q25.389 0 43.943 -18.553t18.553 -43.943l0 -531.216l-624.96 0l0 531.216zm62.496 -31.248l0 -406.224q0 -13.671 8.789 -22.46t22.46 -8.789l62.496 0q13.671 0 22.46 8.789t8.789 22.46l0 406.224q0 13.671 -8.789 22.46t-22.46 8.789l-62.496 0q-13.671 0 -22.46 -8.789t-8.789 -22.46zm31.248 0l62.496 0l0 -406.224l-62.496 0l0 406.224zm31.248 -718.704l374.976 0l0 -31.248q0 -13.671 -8.789 -22.46t-22.46 -8.789l-312.48 0q-13.671 0 -22.46 8.789t-8.789 22.46l0 31.248zm124.992 718.704l0 -406.224q0 -13.671 8.789 -22.46t22.46 -8.789l62.496 0q13.671 0 22.46 8.789t8.789 22.46l0 406.224q0 13.671 -8.789 22.46t-22.46 8.789l-62.496 0q-13.671 0 -22.46 -8.789t-8.789 -22.46zm31.248 0l62.496 0l0 -406.224l-62.496 0l0 406.224zm156.24 0l0 -406.224q0 -13.671 8.789 -22.46t22.46 -8.789l62.496 0q13.671 0 22.46 8.789t8.789 22.46l0 406.224q0 13.671 -8.789 22.46t-22.46 8.789l-62.496 0q-13.671 0 -22.46 -8.789t-8.789 -22.46zm31.248 0l62.496 0l0 -406.224l-62.496 0l0 406.224z"/></svg>`;
const ICON_THUMBUP =`<svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px"><path d="M0 0h24v24H0z" fill="none"/><path d="M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.` +`44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5` +`1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-1.91l-.01-.01L23 10z"/></svg>`;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
})
export class AppComponent implements OnInit {
  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer, private http: HttpClient, private rest: ServiceRest) {

    iconRegistry.addSvgIconLiteral('trash', sanitizer.bypassSecurityTrustHtml(ICON_TRASH));
    iconRegistry.addSvgIconLiteral('thumbsup', sanitizer.bypassSecurityTrustHtml(ICON_THUMBUP));
   }

  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;

  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public jasmartyType: string;
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
