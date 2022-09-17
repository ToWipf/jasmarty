import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { TodoEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-todolist',
  templateUrl: './todolist.component.html',
  styleUrls: ['./todolist.component.less'],
})
export class TodolistComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = [];
  public toarry: TodoEntry[] = [];
  public bNew: boolean = true;
  public bTodo: boolean = true;
  public bDone: boolean = false;
  public bLater: boolean = false;
  public bZitat: boolean = false;
  public bShowWarning: boolean = false;
  public sFilter: string = "";
  public bShowAllTable: boolean = true;

  ngOnInit() {
    this.load();
    this.showAllTable();
  }

  public showAllTable(): void {
    this.bShowAllTable = !this.bShowAllTable;
    if (this.bShowAllTable) {
      this.displayedColumns = ['id', 'data', 'date', 'editby', 'button'];
    } else {
      this.displayedColumns = ['id', 'data', 'button'];
    }
  }

  public load(): void {
    this.toarry = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('todolist/getAll').then((resdata: TodoEntry[]) => {
      resdata.forEach((element) => {

        if (element.active === 'LATER' && this.bLater) {
          this.toarry.push(element);
        }
        if (element.active === 'TODO' && this.bTodo) {
          this.toarry.push(element);
        }
        if (element.active === 'DONE' && this.bDone) {
          this.toarry.push(element);
        }
        if (element.active === 'NEW' && this.bNew) {
          this.toarry.push(element);
        }
        if (element.active === 'ZITAT' && this.bZitat) {
          this.toarry.push(element);
        }
      });

      this.dataSource = new MatTableDataSource(this.toarry);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public newItem(): void {
    let td: TodoEntry = {};
    td.remind = '';
    td.active = 'NEW';
    td.date = Math.round(Date.now() / 1000);
    td.editby = 'web';
    this.openDialog(td);
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
        this.rest.delete('todolist/delete/' + item.id).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  private saveTodo(item: TodoEntry): void {
    this.rest.post('todolist/saveTodo', item).then((resdata: any) => {
      this.load();
      if (!resdata) {
        this.bShowWarning = true;
      }
    });
  }

  public openDialog(item: TodoEntry): void {
    const edititem: TodoEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(TodolistComponentDialogComponent, {
      width: '350px',
      height: '420px',
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: TodoEntry) => {
      if (result) {
        result.editby = 'web';
        result.date = Math.round(Date.now() / 1000);
        this.saveTodo(result);
      }
    });
  }
}

@Component({
  selector: 'app-todolist-dialog',
  templateUrl: './todolist.dialog.html',
})
export class TodolistComponentDialogComponent {
  constructor(public dialogRef: MatDialogRef<TodolistComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: TodoEntry) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
