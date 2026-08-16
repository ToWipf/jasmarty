import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CheckListeItem, CheckListeListe, CheckListeType, CheckListeVerkn } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-checkliste',
  templateUrl: './checkliste.component.html',
  styleUrls: ['./checkliste.component.less'],
  standalone: false
})
export class ChecklisteComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  public dataSourceCheckListeListe: MatTableDataSource<CheckListeListe> = new MatTableDataSource();
  public dataSourceCheckListeType: MatTableDataSource<CheckListeType> = new MatTableDataSource();
  public dataSourceCheckListeItem: MatTableDataSource<CheckListeItem> = new MatTableDataSource();
  public dataSourceCheckListeVerkn: MatTableDataSource<CheckListeVerkn> = new MatTableDataSource();
  public bShowWarning: boolean = false;
  public bShowAllTableColumns: boolean = true;
  public displayedColumnsCheckListeListe: string[] = [];
  public displayedColumnsCheckListeType: string[] = [];
  public displayedColumnsCheckListeItem: string[] = [];
  public displayedColumnsCheckListeVerkn: string[] = [];
  public view = "cl";
  public allTypesCache: CheckListeType[] = [];
  public viewCL: CheckListeListe = {};
  public selectetType: CheckListeType = {};
  public lastNewPrio: number = 0;
  public offeneItems: number = 0;
  public sFilter: string = "";
  public bFilterDone: boolean = false;
  public verkListBackupForFilter: CheckListeVerkn[] = [];
  private selectedClID: number = 0;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  async ngOnInit() {

    this.dataSourceCheckListeListe = new MatTableDataSource();
    this.dataSourceCheckListeType = new MatTableDataSource();
    this.dataSourceCheckListeItem = new MatTableDataSource();
    this.dataSourceCheckListeVerkn = new MatTableDataSource();

    this.loadCheckListeType().then(() => {
      this.loadCheckListeListe();
    });
    this.showAllTableColumns();
    this.setView("listevw");
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSourceCheckListeListe.filter = this.sFilter.trim();
      this.dataSourceCheckListeType.filter = this.sFilter.trim();
      this.dataSourceCheckListeItem.filter = this.sFilter.trim();
      this.dataSourceCheckListeVerkn.filter = this.sFilter.trim();
    });
  }

  public setView(val: string): void {
    this.view = val;
    if (val == "menue") {
      // Rücksetzen von lastPrio
      this.lastNewPrio = 0;
    }
  }

  public elementToColor(e: CheckListeVerkn): string {
    if (e.checked == null) {
      return "red";
    } else {
      if (e.checked) {
        // Grün
        return "#29bc00";
      } else {
        // Grau
        return "#4f4f4f";
      }
    }
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumnsCheckListeListe = ['id', 'listenname', 'date', 'types', 'typesNamen', 'button'];
      this.displayedColumnsCheckListeType = ['id', 'type', 'button'];
      this.displayedColumnsCheckListeItem = ['id', 'item', 'prio', 'type', 'button'];
      this.displayedColumnsCheckListeVerkn = ['id', 'item', 'prio', 'button'];
    } else {
      this.displayedColumnsCheckListeListe = ['listenname', 'date', 'typesNamen', 'button'];
      this.displayedColumnsCheckListeType = ['type', 'button'];
      this.displayedColumnsCheckListeItem = ['item', 'prio', 'type', 'button'];
      this.displayedColumnsCheckListeVerkn = ['button', 'item'];
    }
  }

  public loadCheckListeListe(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/liste/getAll').then((resdata: CheckListeListe[]) => {
      resdata.forEach((cl: CheckListeListe) => {
        const numbers = cl.typesNummbers ?? [];
        const cache = cl.typesCache ?? [];
        cl.typesNummbers = numbers;
        cl.typesCache = cache;
        if (cl.types) {
          cl.types.split(",").forEach((tid: string) => {
            numbers.push(Number(tid));
            this.allTypesCache.forEach((t) => {
              if (Number(tid) == t.id) {
                cache.push(t);
              }
            });
          });
        }
      });
      this.dataSourceCheckListeListe = new MatTableDataSource(resdata);
      warten.close();
    });
  }

  public async loadCheckListeType(): Promise<void> {
    return new Promise<void>(
      resolve => {
        this.displayedColumnsCheckListeItem = ['item', 'prio', 'button'];
        const warten = this.dialog.open(DialogWartenComponent, {});
        this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
          this.dataSourceCheckListeType = new MatTableDataSource(resdata);
          this.allTypesCache = resdata;
          warten.close();
          resolve();
        });
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
      // Höchsten Priowert ermitteln und merken
      if (resdata.length > 0) {
        this.lastNewPrio = Math.max(...resdata.map(item => item.prio ?? 0));
      }
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
    const itemTypes = item.types ?? "";
    item.types = itemTypes;
    item.typesCache = item.typesCache ?? [];
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
      const typeId = edititem.checkListeType?.id ?? 0;
      if (this.lastNewPrio == 0) {
        this.lastNewPrio = typeId * 100;
      } else {
        this.lastNewPrio = this.lastNewPrio + 2;
      }
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
    this.selectedClID = cl.id ?? 0;

    this.rest.getNoWartenDialog('checkliste/verkn/getByClID/' + cl.id).then((resdata: CheckListeVerkn[]) => {
      this.dataSourceCheckListeVerkn = new MatTableDataSource(resdata);
      this.offeneItems = resdata.length;

      resdata.forEach((clv) => {
        if (clv.checked == false || clv.checked == true) {
          this.offeneItems = this.offeneItems - 1;
        }
      });
    });
  }

  public checkItemVerkn(iverk: CheckListeVerkn): void {
    this.bFilterDone = false;
    this.verkListBackupForFilter = [];
    iverk.checked = !iverk.checked;
    this.rest.postNoWartenDialog('checkliste/verkn/save', iverk).then((res: any) => {
      this.ladeChecklistenView(this.viewCL);
    });
  }

  /// Reset der Liste

  public resetListe(): void {
    this.rest.delete('checkliste/verkn/reset/' + this.selectedClID).then((res: any) => {
      this.ladeChecklistenView(this.viewCL);
    });
  }

  public filterDone(): void {
    this.bFilterDone = !this.bFilterDone;

    if (this.bFilterDone) {
      this.verkListBackupForFilter = [...this.dataSourceCheckListeVerkn.data];
      // this.dataSourceCheckListeVerkn.data = this.dataSourceCheckListeVerkn.data.filter((clv: CheckListeVerkn) => {
      // !clv.checked;
      // });
      this.dataSourceCheckListeVerkn.data =
        this.dataSourceCheckListeVerkn.data.filter(
          clv => !clv.checked
        );
    }
    else {
      this.dataSourceCheckListeVerkn.data = [...this.verkListBackupForFilter];
    }

    this.applyFilter();
  }
}

@Component({
  selector: 'app-checklisteliste-dialog',
  templateUrl: './checkliste.dialog.checkliste.html',
  standalone: false
})
export class CheckListeDialogCheckListe implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public dialogRef: MatDialogRef<CheckListeDialogCheckListe>, @Inject(MAT_DIALOG_DATA) public data: CheckListeListe) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public checkListetypes: CheckListeType[] = [];

  public ngOnInit(): void {
    this.data.typesCache = [];
    // Convert type ids to Types
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListetypes = resdata;

      if (this.data.types) {
        const numbers = this.data.typesNummbers ?? [];
        const cache = this.data.typesCache ?? [];
        this.data.typesNummbers = numbers;
        this.data.typesCache = cache;
        numbers.forEach((t: number) => {
          this.checkListetypes.forEach((xt: CheckListeType) => {
            if (t == xt.id) {
              cache.push(xt);
            }
          });
        });
      }
      warten.close();
    });
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public saveByEnter(): void {
    this.dialogRef.close(this.data);
  }
}

@Component({
  selector: 'app-checklistetypes-dialog',
  templateUrl: './checkliste.dialog.type.html',
  standalone: false
})
export class CheckListeDialogType {
  constructor(public dialog: MatDialog, public dialogRef: MatDialogRef<CheckListeDialogType>, @Inject(MAT_DIALOG_DATA) public data: CheckListeType) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public saveByEnter(): void {
    this.dialogRef.close(this.data);
  }
}

@Component({
  selector: 'app-checklisteitem-dialog',
  templateUrl: './checkliste.dialog.item.html',
  standalone: false
})
export class CheckListeDialogItem {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public dialogRef: MatDialogRef<CheckListeDialogItem>, @Inject(MAT_DIALOG_DATA) public data: CheckListeItem) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public checkListetypes: CheckListeType[] = [];

  public ngOnInit(): void {
    // Convert type id to Type
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('checkliste/type/getAll').then((resdata: CheckListeType[]) => {
      this.checkListetypes = resdata;

      if (this.data.checkListeType) {
        // Warum?
        this.checkListetypes.forEach((xt: CheckListeType) => {
          if ((this.data.checkListeType?.id ?? -1) == xt.id) {
            this.data.checkListeType = xt;
          }
        });
      }
      warten.close();
    });
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public saveByEnter(): void {
    this.dialogRef.close(this.data);
  }
}