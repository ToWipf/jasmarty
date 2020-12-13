import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { TodoEntry } from 'src/app/datatypes';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
  selector: 'app-todolist',
  templateUrl: './todolist.component.html',
  styleUrls: ['./todolist.component.less'],
})
export class TodolistComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['id', 'data', 'date', 'editby', 'button'];
  public toarry: TodoEntry[] = [];
  private nextId: number;
  public bNew: boolean = true;
  public bTodo: boolean = true;
  public bDone: boolean = false;
  public bLater: boolean = true;
  public bDeleteEnable: boolean = false;
  public bShowWarning: boolean = false;
  public sFilter: String = "";

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.toarry = [];

    this.http.get(this.rest.gethost() + 'todolist/getAll').subscribe((resdata: TodoEntry[]) => {
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
      });

      this.dataSource = new MatTableDataSource(this.toarry);
      this.dataSource.sort = this.sort;
      this.nextId = this.getNextId();
      this.applyFilter();
    });
  }

  public applyFilter() {
    this.dataSource.filter = this.sFilter.trim();
  }

  public newItem(): void {
    let td: TodoEntry = {};

    td.id = this.nextId;
    td.remind = '';
    td.active = 'NEW';

    td.date = Math.round(Date.now() / 1000);
    td.editby = 'web';
    this.openDialog(td);
  }

  public deleteItem(item: TodoEntry): void {
    // TODO: ADD nachfragen dialog
    this.bDeleteEnable = false;
    this.http.delete(this.rest.gethost() + 'todolist/delete/' + item.id).subscribe((resdata: any) => {
      this.load();
    });
  }

  private getNextId(): number {
    let nextId: number = 0;
    this.toarry.forEach((item: TodoEntry) => {
      if (item.id > nextId) {
        nextId = item.id;
      }
    });
    return nextId * 1 + 1;
  }

  private saveTodo(item: TodoEntry): void {
    this.http.post(this.rest.gethost() + 'todolist/saveTodo', item).subscribe((resdata: any) => {
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
      height: '350px',
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
