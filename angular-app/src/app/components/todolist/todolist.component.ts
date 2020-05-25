import { Component, OnInit, ViewChild } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { todoEntry } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";

@Component({
  selector: "app-todolist",
  templateUrl: "./todolist.component.html",
  styleUrls: ["./todolist.component.less"],
})
export class todolistComponent implements OnInit {
  constructor(private http: HttpClient, private rest: ServiceRest) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ["id", "data", "active", "date", "editby"];
  public toarry: todoEntry[] = [];

  ngOnInit() {
    this.getAll();
  }

  private getAll(): void {
    this.http.get(this.rest.gethost() + "todolist/getAllJson").subscribe((resdata: any) => {
      this.toarry = resdata;
      this.dataSource = new MatTableDataSource(this.toarry);
      this.dataSource.sort = this.sort;
    });
  }
}
