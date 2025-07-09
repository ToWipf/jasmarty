import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.less'],
    standalone: false
})
export class LoginComponent implements OnInit {
  constructor(public dialog: MatDialog, public rest: ServiceRest) { }

  ngOnInit(): void {
    // Letzten Besuch holen
    const lastVisit = localStorage.getItem("lastVisit");

    // Neuen Besuch speichern
    const now = new Date();
    localStorage.setItem("lastVisit", now.toISOString());

    // Zeitdifferenz berechnen
    if (lastVisit) {
      const last = new Date(lastVisit);
      const diffMs = now.getTime() - last.getTime();

      // In eine lesbare Form umwandeln
      const diffSeconds = Math.floor(diffMs / 1000);
      const seconds = diffSeconds % 60;
      const minutes = Math.floor((diffSeconds / 60)) % 60;
      const hours = Math.floor(diffSeconds / 3600);

      this.lastVisitToNow = `${hours}h ${minutes}m ${seconds}s`;
    } else {
      this.lastVisitToNow = 'Erster Besuch';
    }
  }

  public sUsername: string;
  public sPasswort: string;
  public lastVisitToNow: string;

  public login(): void {
    this.rest.setLoginData(this.sUsername.trim(), this.sPasswort);
    this.rest.get("wipf/up").then(res => {
      if (res == 1) {
        this.sUsername = "";
        this.sPasswort = "";
        this.rest.setLoginOk("true");
      } else {
        this.logoff();
      }
    });
  }

  public logoff(): void {
    this.rest.doLogout();
  }
}
