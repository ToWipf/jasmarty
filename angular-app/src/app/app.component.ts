import { Component, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
})
export class AppComponent {
  title = 'Wipf';

  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;

  public selectedSite: string = 'firstpage';

  public selectSite(s: string) {
    this.drawer.toggle();
    this.selectedSite = s;
  }
}
