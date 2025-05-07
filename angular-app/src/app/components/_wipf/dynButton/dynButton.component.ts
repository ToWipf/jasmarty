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

  public buttobez: string = "dynButtonName";
  private BUTTONNAME: string = "dynButtonName";
  private BUTTONDOMAIN: string = "dynButtonDomain";

  ngOnInit(): void {
    if (localStorage.getItem(this.BUTTONNAME).length < 0) {
      localStorage.setItem(this.BUTTONNAME, "");
      localStorage.setItem(this.BUTTONDOMAIN, "https://");
    } else {
      this.buttobez = localStorage.getItem(this.BUTTONNAME);
    }
  }

  public doClickButton(): void {
    this.rest.getExternalDomain(localStorage.getItem(this.BUTTONDOMAIN));
  }
}
