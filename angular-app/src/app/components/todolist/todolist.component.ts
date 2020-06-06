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
export class todolistComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ["id", "data", "active", "date", "editby", "button"];
  public toarry: todoEntry[] = [];

  ngOnInit() {
    this.load();
  }

  private load(): void {
    this.http.get(this.rest.gethost() + "todolist/getAllJson").subscribe((resdata: any) => {
      this.toarry = resdata;
      this.dataSource = new MatTableDataSource(this.toarry);
      this.dataSource.sort = this.sort;
    });
  }

  public newItem(): void {
    var nextId: number = 0;
    this.toarry.forEach((item: todoEntry) => {
      if (item.id > nextId) {
        nextId = item.id;
      }
    });

    var td: todoEntry = {};
    console.log(td);
    td.id = nextId++;
    console.log(td);
    td.id = td.id + 1;
    console.log(td);
    td.date = Math.round(Date.now() / 1000);
    td.editby = "web";
    this.openDialog(td);
  }

  public deleteItem(item: todoEntry): void {
    // TODO: ADD nachfragen dialog
    this.http.get(this.rest.gethost() + "todolist/delete/" + item.id).subscribe((resdata: any) => {
      this.load();
    });
  }

  private saveTodo(item: todoEntry): void {
    this.http.post(this.rest.gethost() + "todolist/saveTodo", item).subscribe((resdata: any) => {
      this.load();
    });
  }

  public openDialog(item: todoEntry): void {
    let edititem: todoEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(todolistComponentDialog, {
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
export class todolistComponentDialog {
  constructor(public dialogRef: MatDialogRef<todolistComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: todoEntry) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
