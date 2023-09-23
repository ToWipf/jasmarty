import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.less']
})
export class TemplateComponent implements OnInit {

  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  ngOnInit(): void {
    //
  }
}
