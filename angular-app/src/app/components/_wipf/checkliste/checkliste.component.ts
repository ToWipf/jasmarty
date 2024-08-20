import { Component, Inject, Injectable, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CheckListeListe, CheckListeType, ListeEntry, ListeType } from 'src/app/datatypes';
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

  public checkListeliste: CheckListeListe[];
  public checkListetypes: CheckListeType[];

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit() {
    this.loadCheckListeListe();
    this.loadCheckListeType();
  }

  public loadCheckListeListe(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/liste/getAll').then((resdata: CheckListeListe[]) => {
      this.checkListeliste = resdata;
      warten.close();
    });
  }

  public loadCheckListeType(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListeliste = resdata;
      warten.close();
    });
  }

}
