import { Component, OnInit, ViewChild } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { buttonaction } from "src/app/datatypes";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from '@angular/material/sort';

@Component({
  selector: "app-jasmartyActions",
  templateUrl: "./jasmartyActions.component.html",
  styleUrls: ["./jasmartyActions.component.less"],
})
export class JasmartyActionsComponent implements OnInit {
  constructor(private http: HttpClient) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public buttonactions: buttonaction[];
  public tableDataSource;
  displayedColumns: string[] = ["id", "button", "active", "action"];

  ngOnInit() {
    this.load();
  }

  public load(): void {
    this.http.get("http://localhost:8080/actions/getall").subscribe((resdata: any) => {
      console.log(resdata);
      this.buttonactions = resdata;
      this.tableDataSource = new MatTableDataSource(this.buttonactions);
      this.tableDataSource.sort = this.sort;
    });
  }
}
