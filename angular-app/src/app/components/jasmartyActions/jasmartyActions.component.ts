import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { buttonaction } from "src/app/datatypes";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";

@Component({
  selector: "app-jasmartyActions",
  templateUrl: "./jasmartyActions.component.html",
  styleUrls: ["./jasmartyActions.component.less"],
})
export class JasmartyActionsComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public buttonactions: buttonaction[];
  public tableDataSource: any;
  public displayedColumns: string[] = ["id", "button", "active", "action", "edit"];

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

  public edit(item: buttonaction): void {
    console.log(item);

    const dialogRef = this.dialog.open(JasmartyActionsComponentDialog, {
      width: "400px",
      height: "300px",
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log("The dialog was closed");
      console.log(result); //save TODO:
    });
  }
}

@Component({
  selector: "app-jasmartyActions-dialog",
  templateUrl: "./jasmartyActions.dialog.html",
})
export class JasmartyActionsComponentDialog {
  constructor(public dialogRef: MatDialogRef<JasmartyActionsComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: buttonaction) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
