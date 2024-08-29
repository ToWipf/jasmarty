import { Component, Inject, Injectable, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CheckListeItem, CheckListeListe, CheckListeType, CheckListeVerkn } from 'src/app/datatypes';
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
  public dataSourceCheckListeType;
  public dataSourceCheckListeItem;
  public dataSourceCheckListeVerkn;
  public bShowWarning: boolean = false;
  public bShowAllTableColumns: boolean = true;
  public displayedColumnsCheckListeListe;
  public displayedColumnsCheckListeType;
  public displayedColumnsCheckListeItem;
  public displayedColumnsCheckListeVerkn;
  public view = "cl";
  public viewCL: CheckListeListe = {};
  public selectetType: CheckListeType = {};
  public lastNewPrio: number = 0;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit() {
    this.showAllTableColumns();
    this.loadCheckListeListe();
    this.loadCheckListeType();
    this.setView("menue");
  }

  public setView(val: string): void {
    this.view = val;
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumnsCheckListeListe = ['id', 'listenname', 'date', 'types', 'button'];
      this.displayedColumnsCheckListeType = ['id', 'type', 'button'];
      this.displayedColumnsCheckListeItem = ['id', 'item', 'prio', 'type', 'button'];
      this.displayedColumnsCheckListeVerkn = ['id', 'item', 'prio', 'button'];
    } else {
      this.displayedColumnsCheckListeListe = ['listenname', 'date', 'types', 'button'];
      this.displayedColumnsCheckListeType = ['type', 'button'];
      this.displayedColumnsCheckListeItem = ['item', 'prio', 'type', 'button'];
      this.displayedColumnsCheckListeVerkn = ['item', 'button', 'prio'];
    }
  }

  public loadCheckListeListe(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/liste/getAll').then((resdata: CheckListeListe[]) => {
      resdata.forEach((cl: CheckListeListe) => {
        cl.typesNummbers = [];
        if (cl.types) {
          cl.types.split(",").forEach((tid: string) => {
            cl.typesNummbers.push(Number(tid));
          });
        }
      });
      this.dataSourceCheckListeListe = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public loadCheckListeType(): void {
    this.displayedColumnsCheckListeItem = ['item', 'prio', 'button'];
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.dataSourceCheckListeType = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public loadCheckListeItemAll(): void {
    this.displayedColumnsCheckListeItem = ['item', 'prio', 'type', 'button'];
    this.selectetType = {}
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/item/getAll').then((resdata: CheckListeItem[]) => {
      this.dataSourceCheckListeItem = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public ladeViewItemsAll(): void {
    this.setView("itemvw");
    this.loadCheckListeItemAll();
  }

  public ladeViewItemsByType(type: CheckListeItem): void {
    this.setView("itemvw");
    this.selectetType = type;
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.post('checkliste/item/getAllByType', type).then((resdata: CheckListeItem[]) => {
      this.dataSourceCheckListeItem = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  ///
  /// Liste Liste
  ///

  public newItemCheckListeListe(): void {
    let n: CheckListeListe = {};
    n.date = new Date(Date.now()).toISOString().split('T')[0]; // heuteigen Tag als vorauswahl
    this.openDialogCheckListeListe(n);
  }

  public openDialogCheckListeListe(item: CheckListeListe): void {
    const edititem: CheckListeListe = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(CheckListeDialogCheckListe, {
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
    // Convert Typen in typ ids
    item.types = "";
    item.typesCache.forEach((t: CheckListeType) => {
      if (item.types.length == 0) {
        item.types = "" + (t.id);
      } else {
        item.types = item.types + "," + (t.id);
      }
    });

    this.rest.post('checkliste/liste/save', item).then((resdata: any) => {
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
          this.loadCheckListeListe();
        });
      }
    });
  }

  ///
  /// Liste Types
  ///

  public newItemCheckListeType(): void {
    let n: CheckListeType = {};
    this.openDialogCheckListeType(n);
  }

  public openDialogCheckListeType(item: CheckListeType): void {
    const edititem: CheckListeType = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(CheckListeDialogType, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: CheckListeType) => {
      if (result) {
        this.saveCheckListeType(result);
      }
    });
  }

  private saveCheckListeType(item: CheckListeType): void {
    this.rest.post('checkliste/type/save', item).then((resdata: any) => {
      this.loadCheckListeType();
    });
  }

  public deleteItemCheckListeType(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.type;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('checkliste/type/delete/' + item.id).then((resdata: any) => {
          this.loadCheckListeType();
        });
      }
    });
  }

  ///
  /// Liste Item
  ///

  public newItemCheckListeItem(): void {
    let n: CheckListeItem = {};
    this.openDialogCheckListeItem(n);
  }

  public openDialogCheckListeItem(item: CheckListeItem): void {
    const edititem: CheckListeItem = this.serviceWipf.deepCopy(item);

    if (!edititem.checkListeType) {
      edititem.checkListeType = this.selectetType;
    }
    if (!edititem.prio) {
      this.lastNewPrio = this.lastNewPrio +1
      edititem.prio = this.lastNewPrio;
    }

    const dialogRef = this.dialog.open(CheckListeDialogItem, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: CheckListeItem) => {
      if (result) {
        this.saveCheckListeItem(result);
      }
    });
  }

  private saveCheckListeItem(item: CheckListeItem): void {
    //item.checkListeTypeId = item.type.id;
    this.rest.post('checkliste/item/save', item).then((resdata: any) => {
      if (this.selectetType?.id) {
        this.ladeViewItemsByType(this.selectetType);
      } else {
        this.loadCheckListeItemAll();
      }
    });
  }

  public deleteItemCheckListeItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.item;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('checkliste/item/delete/' + item.id).then((resdata: any) => {
          this.loadCheckListeItemAll();
        });
      }
    });
  }

  ///
  ///
  /// Checkliste
  ///
  ///

  public ladeChecklistenView(cl: CheckListeListe): void {
    this.setView("checkliste");
    this.viewCL = cl;

    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/verkn/getByClID/' + cl.id).then((resdata: CheckListeVerkn[]) => {
      this.dataSourceCheckListeVerkn = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public checkItemVerkn(iverk: CheckListeVerkn): void {
    iverk.checked = !iverk.checked;
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.post('checkliste/verkn/save', iverk).then((res: any) => {
      this.ladeChecklistenView(this.viewCL);
      warten.close();
    });
  }

}

@Component({
  selector: 'app-checklisteliste-dialog',
  templateUrl: './checkliste.dialog.checkliste.html',
})
export class CheckListeDialogCheckListe implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public dialogRef: MatDialogRef<CheckListeDialogCheckListe>, @Inject(MAT_DIALOG_DATA) public data: CheckListeListe) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public checkListetypes: CheckListeType[];

  public ngOnInit(): void {
    this.data.typesCache = [];
    // Convert type ids to Types
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListetypes = resdata;

      if (this.data.types) {
        this.data.typesNummbers.forEach((t: number) => {
          this.checkListetypes.forEach((xt: CheckListeType) => {
            if (t == xt.id) {
              this.data.typesCache.push(xt);
            }
          })
        })
      }
      warten.close();
    });
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'app-checklistetypes-dialog',
  templateUrl: './checkliste.dialog.type.html',
})
export class CheckListeDialogType {
  constructor(public dialog: MatDialog, public dialogRef: MatDialogRef<CheckListeDialogType>, @Inject(MAT_DIALOG_DATA) public data: CheckListeType) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'app-checklisteitem-dialog',
  templateUrl: './checkliste.dialog.item.html',
})
export class CheckListeDialogItem {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public dialogRef: MatDialogRef<CheckListeDialogItem>, @Inject(MAT_DIALOG_DATA) public data: CheckListeItem) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public checkListetypes: CheckListeType[];

  public ngOnInit(): void {
    // Convert type id to Type
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListetypes = resdata;

      if (this.data.checkListeType) {
        // Warum?
        this.checkListetypes.forEach((xt: CheckListeType) => {
          if (this.data.checkListeType.id == xt.id) {
            this.data.checkListeType = xt;
          }
        })
      }
      warten.close();
    });
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}