import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { todoEntry } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ServiceWipf } from "src/app/service/serviceWipf";

@Component({
  selector: "app-todolist",
  templateUrl: "./todolist.component.html",
  styleUrls: ["./todolist.component.less"],
})
export class TodolistComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ["id", "data", "active", "date", "editby", "button"];
  public toarry: todoEntry[] = [];
  public filter: string = "ALL";
  private nextId: number;

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.toarry = [];

    this.http.get(this.rest.gethost() + "todolist/getAll").subscribe((resdata: todoEntry[]) => {
      resdata.forEach((element) => {
        if (this.filter === "ALL" || element.active === this.filter) {
          this.toarry.push(element);
        }
      });

      //this.toarry = resdata;

      this.dataSource = new MatTableDataSource(this.toarry);
      this.dataSource.sort = this.sort;
      this.nextId = this.getNextId();
    });
  }

  public newItem(): void {
    var td: todoEntry = {};

    td.id = this.nextId;
    td.remind = "";
    td.active = "NEW";

    td.date = Math.round(Date.now() / 1000);
    td.editby = "web";
    this.openDialog(td);
  }

  public deleteItem(item: todoEntry): void {
    // TODO: ADD nachfragen dialog
    this.http.delete(this.rest.gethost() + "todolist/delete/" + item.id).subscribe((resdata: any) => {
      this.load();
    });
  }

  private getNextId(): number {
    var nextId: number = 0;
    this.toarry.forEach((item: todoEntry) => {
      if (item.id > nextId) {
        nextId = item.id;
      }
    });
    return nextId * 1 + 1;
  }

  private saveTodo(item: todoEntry): void {
    this.http.post(this.rest.gethost() + "todolist/saveTodo", item).subscribe((resdata: any) => {
      this.load();
    });
  }

  public openDialog(item: todoEntry): void {
    let edititem: todoEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(TodolistComponentDialog, {
      width: "350px",
      height: "350px",
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: todoEntry) => {
      if (result) {
        result.editby = "web";
        result.date = Math.round(Date.now() / 1000);
        this.saveTodo(result);
      }
    });
  }
}

@Component({
  selector: "app-todolist-dialog",
  templateUrl: "./todolist.dialog.html",
})
export class TodolistComponentDialog {
  constructor(public dialogRef: MatDialogRef<TodolistComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: todoEntry) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
