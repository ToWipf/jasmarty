import { Component, Inject, Injectable, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ListeEntry, ListeType } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Injectable({
  providedIn: 'root',
})

@Component({
  selector: 'app-checkliste',
  templateUrl: './checkliste.component.html',
  styleUrls: ['./checkliste.component.less']
})
export class ChecklisteComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit() {
  }

}
