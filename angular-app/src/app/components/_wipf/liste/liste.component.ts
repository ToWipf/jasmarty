import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ListeEntry, ListeType } from 'src/app/datatypes';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { ListeTypeComponentDialogTypeListComponent } from './listeType.component';

@Component({
  selector: 'app-liste',
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.less']
})
export class ListeComponent implements OnInit {

  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public bShowWarning: boolean = false;
  public displayedColumns: string[] = [];
  public sFilter: string = "";
  public bShowAllTableColumns: boolean = true;
  public listeTypeForFilter: ListeType[];
  public selectedTypeFilter: ListeType;
  public fullListe: ListeEntry[];

  ngOnInit() {
    this.loadTypes();
    this.load();
    this.showAllTableColumns();
  }

  public idToColor(id: number): string {
    if (id === 1)
      return "#fc5f53";
    if (id === 2)
      return "#fcc653";
    if (id === 3)
      return "#99fc53";
    if (id === 4)
      return "#53fca2";
    if (id === 5)
      return "#53ddfc";
    if (id === 6)
      return "#5e53fc";
    if (id === 7)
      return "#be53fc";
    if (id === 8)
      return "#fc53be";
    else
      return "green";
  }

  public showAllTableColumns(): void {
    this.bShowAllTableColumns = !this.bShowAllTableColumns;
    if (this.bShowAllTableColumns) {
      this.displayedColumns = ['id', 'typeid', 'type', 'data', 'date', 'button'];
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
      warten.close();
    });
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('liste/getAll').then((resdata: ListeEntry[]) => {
      this.fullListe = resdata;
      this.dataSource = new MatTableDataSource(this.fullListe);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
      this.applyFilterByType();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public applyFilterByType() {
    let eventlistToShow: ListeEntry[] = [];
    if (this.selectedTypeFilter != undefined) {

      this.fullListe.forEach((event: ListeEntry) => {
        if (event.typeid == this.selectedTypeFilter.id) {
          eventlistToShow.push(event);
        }
      });
      this.dataSource = new MatTableDataSource(eventlistToShow);
    } else {
      // Wie Normal -> alles anzeigen
      this.dataSource = new MatTableDataSource(this.fullListe);
    }

  }

  public newItem(): void {
    let n: ListeEntry = {};
    n.date = new Date(Date.now()).toISOString().split('T')[0]; // heuteigen Tag als vorauswahl
    n.data = "";
    n.typeid = 1;
    this.openDialog(n);
  }

  private save(item: ListeEntry): void {
    this.rest.post('liste/save', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public deleteItem(item: any): void {
    item.infotext = "Wirklich löschen? " + item.data;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('liste/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  public openDialog(item: ListeEntry): void {
    const edititem: ListeEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(ListeComponentDialogComponent, {
      data: edititem,
      autoFocus: true,
      minWidth: '300px',
      minHeight: '250px',
    });

    dialogRef.afterClosed().subscribe((result: ListeEntry) => {
      if (result) {
        this.save(result);
      }
    });
  }

}

@Component({
  selector: 'app-liste-dialog',
  templateUrl: './liste.dialog.html',
})
export class ListeComponentDialogComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<ListeComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ListeEntry) { }

  public listeType: ListeType[];

  ngOnInit(): void {
    this.loadTypes();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  public loadTypes(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
    this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
      this.listeType = resdata;
      warten.close();
    });
  }

}

