import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { buttonaction } from "src/app/datatypes";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";
import { ServiceWipf } from "src/app/service/serviceWipf";

@Component({
  selector: "app-jasmartyActions",
  templateUrl: "./jasmartyActions.component.html",
  styleUrls: ["./jasmartyActions.component.less"],
})
export class JasmartyActionsComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, public serviceWipf: ServiceWipf) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public buttonactions: buttonaction[];
  public tableDataSource: any;
  public displayedColumns: string[] = ["id", "button", "active", "action", "edit"];
  public lastPressed: number = null;
  public loopStop: boolean = false;

  ngOnInit() {
    this.load();
    this.loopLoadButton();
  }

  public ngOnDestroy(): void {
    this.loopStop = true;
  }

  public newItem(): void {
    const nextid = this.buttonactions.length + 1;
    const empty: buttonaction = { id: nextid, active: false, button: this.lastPressed, action: "nichts" };
    this.editItem(empty);
  }

  public editItem(item: buttonaction): void {
    let edititem: buttonaction;

    edititem = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(JasmartyActionsComponentDialog, {
      width: "500px",
      height: "400px",
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.save(result);
      }
    });
  }

  private save(item: buttonaction): void {
    this.http.post("http://localhost:8080/actions/set", JSON.stringify(item)).subscribe((resdata: any) => {
      if (resdata.save) {
        console.log("saved");
        this.load();
      } else {
        //TODO: Meldung Fehler
        console.log("fehler");
      }
    });
  }

  private load(): void {
    this.http.get("http://localhost:8080/actions/getall").subscribe((resdata: any) => {
      console.log(resdata);
      this.buttonactions = resdata;
      this.tableDataSource = new MatTableDataSource(this.buttonactions);
      this.tableDataSource.sort = this.sort;
    });
  }

  private loopLoadButton(): void {
    if (!this.loopStop) {
      // get current button
      setTimeout(() => {
        this.loopLoadButton();
      }, 1000);
      this.getCurrentPressed();
    }
  }

  private getCurrentPressed(): void {
    this.http.get("http://localhost:8080/actions/currentPressed").subscribe((resdata: any) => {
      console.log(resdata.btn);
      if (resdata.btn){
        this.lastPressed = resdata.btn;
      }
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
