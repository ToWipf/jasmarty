import { Component } from "@angular/core";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.less"]
})
export class AppComponent {
  title = "Wipf";
  public opened = false;
  public selectedSite: string = "default";

  public toggleSidebar() {
    this.opened = !this.opened;
  }

  public selectSite(s: string) {
    this.selectedSite = s;
    this.opened = false;
  }
}
