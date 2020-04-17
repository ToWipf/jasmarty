import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { buttonaction } from "src/app/datatypes";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

@Component({
  selector: "app-jasmartyActions",
  templateUrl: "./jasmartyActions.component.html",
  styleUrls: ["./jasmartyActions.component.less"],
})
export class JasmartyActionsComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog) {}

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
  
  animal: string;
  name: string;

  openDialog(): void {
    const dialogRef = this.dialog.open(JasmartyActionsComponentDialog, {
      width: "250px",
      data: { name: this.name, animal: this.animal },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log("The dialog was closed");
      this.animal = result;
    });
  }
}

@Component({
  selector: "app-jasmartyActions-dialog",
  templateUrl: "./jasmartyActions.dialog.html",
})
export class JasmartyActionsComponentDialog {

  constructor(
    public dialogRef: MatDialogRef<JasmartyActionsComponentDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}

export interface DialogData {
  animal: string;
  name: string;
}