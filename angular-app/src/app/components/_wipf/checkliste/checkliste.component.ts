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

  public dataSourceCheckListeListe;
  public bShowWarning: boolean = false;
  // public checkListeliste: CheckListeListe[];
  public checkListetypes: CheckListeType[];
  public displayedColumnsCheckListeListe = ['id', 'listenname', 'date', 'types', 'button'];

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit() {
    this.loadCheckListeListe();
    this.loadCheckListeType();
  }

  public loadCheckListeListe(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/liste/getAll').then((resdata: CheckListeListe[]) => {
      // this.checkListeliste = resdata;
      this.dataSourceCheckListeListe = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public loadCheckListeType(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListetypes = resdata;
      warten.close();
    });
  }

  public newItemCheckListeListe(): void {
    let n: CheckListeListe = {};
    n.date = new Date(Date.now()).toISOString().split('T')[0]; // heuteigen Tag als vorauswahl
    this.openDialogCheckListeListe(n);
  }

  public openDialogCheckListeListe(item: CheckListeListe): void {
    const edititem: CheckListeListe = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(CheckListeDialogCheckliste, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: CheckListeListe) => {
      if (result) {
        this.saveCheckListeListe(result);
      }
    });
  }

  private saveCheckListeListe(item: CheckListeListe): void {
    this.rest.post('checkliste/liste/save', item).then((resdata: any) => {
      // this.applyFilterByType();
      // TODO: Reload
      // if (!resdata) {
      //   this.bShowWarning = true;
      // }
      this.loadCheckListeListe();
    });
  }

  public deleteItemCheckListeListe(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.listenname + "\n\n" + item.date;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('checkliste/liste/delete/' + item.id).then((resdata: any) => {
          // this.applyFilterByType();
          // TODO: Reload
        });
      }
    });
  }

}

@Component({
  selector: 'app-checklisteliste-dialog',
  templateUrl: './checkliste.dialog.checkliste.html',
})
export class CheckListeDialogCheckliste implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<CheckListeDialogCheckliste>, @Inject(MAT_DIALOG_DATA) public data: CheckListeListe) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public ngOnInit(): void {
    // this.loadTypes();
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  // public loadTypes(): void {
  //   const warten = this.dialog.open(DialogWartenComponent, {});
  //   this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
  //     this.listeType = resdata;
  //     warten.close();
  //   });
  // }

}

@Component({
  selector: 'app-checklistetypes-dialog',
  templateUrl: './checkliste.dialog.type.html',
})
export class CheckListeDialogTypes implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<CheckListeDialogTypes>, @Inject(MAT_DIALOG_DATA) public data: CheckListeType) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public ngOnInit(): void {
    // this.loadTypes();
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  // public loadTypes(): void {
  //   const warten = this.dialog.open(DialogWartenComponent, {});
  //   this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
  //     this.listeType = resdata;
  //     warten.close();
  //   });
  // }

}