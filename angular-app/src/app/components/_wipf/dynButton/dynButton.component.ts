import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-dynButton',
  templateUrl: './dynButton.component.html',
  styleUrls: ['./dynButton.component.less']
})
export class DynButtonComponent implements OnInit {

  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  public buttodomain: string = "";
  private BUTTONDOMAINALIAS: string = "dynButtonDomain";

  async ngOnInit(): Promise<void> {
    if (!localStorage.getItem(this.BUTTONDOMAINALIAS) || localStorage.getItem(this.BUTTONDOMAINALIAS) == "null") {
      // Initiale Config laden
      localStorage.setItem(this.BUTTONDOMAINALIAS, await this.rest.getConfigParam(this.BUTTONDOMAINALIAS));
    } else {
      this.buttodomain = localStorage.getItem(this.BUTTONDOMAINALIAS);
    }
  }

  public doClickButton(): void {
    this.rest.getExternalDomain(this.buttodomain);
  }
}
