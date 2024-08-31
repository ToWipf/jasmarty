import { Component, Inject, Injectable, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ListeEntry, ListeType } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { ListeServiceColor } from './liste.service.color';
import { ListeCryptComponentDialogComponent } from './listeCrypt.component';
import { ListeTypeComponentDialogTypeListComponent } from './listeType.component';

@Injectable({
  providedIn: 'root',
})

@Component({
  selector: 'app-liste',
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.less']
})
export class ListeComponent implements OnInit {

  constructor(public lsColor: ListeServiceColor, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSourceList;
  public bShowWarning: boolean = false;
  public displayedColumns: string[] = [];
  public sFilter: string = "";
  public bShowAllTableColumns: boolean = true;
  public listeTypeForFilter: ListeType[];
  public selectedTypeFilter: ListeType;

  ngOnInit() {
    this.loadTypes();
    this.loadLast();
    this.showAllTableColumns();
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['id', 'type', 'data', 'date', 'button'];
    } else {
      this.displayedColumns = ['type', 'data', 'button'];
    }
  }

  public openDialogTypeVW() {
    const dialogRef = this.dialog.open(ListeTypeComponentDialogTypeListComponent, {
      width: '550px',
      height: '550px',
    });

    // Typen nach den Edit neu laden
    dialogRef.afterClosed().subscribe(() => {
      this.loadTypes();
    });
  }

  public loadTypes(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
      this.listeTypeForFilter = resdata;
      this.listeTypeForFilter.push({ id: -1, typename: "crypt" });
      this.listeTypeForFilter.push({ id: -2, typename: "counter" });
      warten.close();
    });
  }

  public loadAll(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('liste/getAll').then((resdata: ListeEntry[]) => {
      this.dataSourceList = new MatTableDataSource(resdata);
      this.applyTextFilter();
      this.dataSourceList.sort = this.sort;
      warten.close();
    });
  }

  public loadLast(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('liste/getLast/10').then((resdata: ListeEntry[]) => {
      this.dataSourceList = new MatTableDataSource(resdata);
      this.applyTextFilter();
      this.dataSourceList.sort = this.sort;
      warten.close();
    });
  }

  private loadByType(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('liste/getAllByType/' + this.selectedTypeFilter.id).then((resdata: ListeEntry[]) => {
      this.dataSourceList = new MatTableDataSource(resdata);
      this.applyTextFilter();
      this.dataSourceList.sort = this.sort;
      warten.close();
    });
  }

  public applyTextFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSourceList.filter = this.sFilter.trim();
    });
  }

  /**
   * Darüber wird die Liste überhaupt geladen
   */
  public applyFilterByType() {
    if (this.selectedTypeFilter) {
      if (this.selectedTypeFilter.id == -99) {
        // Spaltenansichten mit type und ohne Date
        this.displayedColumns = ['type', 'data', 'button'];
        this.loadAll();
      } else {
        // Spaltenansichten ohne type
        this.displayedColumns = ['data', 'date', 'button'];
        this.loadByType();
      }
    } else {
      // Speichern in der "Last 10" übersicht
      this.loadLast();
    }
  }

  public newItem(): void {
    let n: ListeEntry = {};
    n.date = new Date(Date.now()).toISOString().split('T')[0]; // heuteigen Tag als vorauswahl
    n.data = "";
    if (this.selectedTypeFilter?.id) {
      n.typeid = this.selectedTypeFilter.id;
    } else {
      n.typeid = 1;
    }
    this.openDialog(n);
  }

  private save(item: ListeEntry): void {
    this.rest.post('liste/save', item).then((resdata: any) => {
      this.applyFilterByType();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen?";
    item.infotext2 = item.data + "\n\n" + item.date;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('liste/delete/' + item.id).then((resdata: any) => {
          this.applyFilterByType();
        });
      }
    });
  }

  public openDialog(item: ListeEntry): void {
    const edititem: ListeEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(ListeComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '150px',
    });

    dialogRef.afterClosed().subscribe((result: ListeEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

  public openCryptDialog(item: ListeEntry): void {
    const edititem: ListeEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(ListeCryptComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '200px',
      minHeight: '290px',
    });

    dialogRef.afterClosed().subscribe((result: ListeEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

  public copyToClipboard(item: ListeEntry): void {
    navigator.clipboard.writeText(item.data);
  }

  public convertDateToCount(item: ListeEntry): string {
    let dEntry = new Date(item.date);
    let dNow = new Date();
    dNow.setUTCHours(0,0,0);
    return (((dNow.getTime() - dEntry.getTime()) / (1000 * 60 * 60))/24).toFixed(0);
  }

}

@Component({
  selector: 'app-liste-dialog',
  templateUrl: './liste.dialog.html',
})
export class ListeComponentDialogComponent implements OnInit {
  constructor(public lsColor: ListeServiceColor, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<ListeComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ListeEntry) {
    dialogRef.disableClose = true;
    dialogRef.updateSize("70%", "70%");
  }

  public listeType: ListeType[];

  public ngOnInit(): void {
    this.loadTypes();
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public loadTypes(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
      this.listeType = resdata;
      this.listeType.push({ id: -1, typename: "crypt" });
      this.listeType.push({ id: -2, typename: "counter" });
      warten.close();
    });
  }

}

