import { Component, OnInit, ViewChild } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";

@Component({
  selector: "app-todoli",
  templateUrl: "./todol.component.html",
  styleUrls: ["./todol.component.less"]
})
export class ToDoLiComponent implements OnInit {
  constructor(private http: HttpClient) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  displayedColumns: string[] = ['id', 'data', 'active', 'date', 'editby'];
  public toarry: [
    { id: number; data: string; active: string; editby: string; date: number }
  ];

  ngOnInit() {
    this.getAll();
  }

  private getAll(): void {
    this.http
      .get("http://192.168.2.10:8080/todolist/getAllJson")
      .subscribe((resdata: any) => {
        this.toarry = resdata;
        this.dataSource = new MatTableDataSource(this.toarry);
        this.dataSource.sort = this.sort;
      });
  }
}
