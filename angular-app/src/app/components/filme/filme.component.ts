import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { filmEntry } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ServiceWipf } from "src/app/service/serviceWipf";

@Component({
  selector: "app-filme",
  templateUrl: "./filme.component.html",
  styleUrls: ["./filme.component.less"],
})
export class filmeComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) {}

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ["id", "titel", "art", "gesehen", "bewertung", "infotext", "date", "editby", "button"];
  public farry: filmEntry[] = [];
  private nextId: number;

  ngOnInit() {
    this.load();
  }
  
  private load(): void {
    this.http.get(this.rest.gethost() + "filme/getAll").subscribe((resdata: filmEntry[]) => {
      this.farry = resdata;
      this.dataSource = new MatTableDataSource(this.farry);
      this.dataSource.sort = this.sort;
      this.nextId = this.getNextId();
    });
  }

  public newItem(): void {
    var td: filmEntry = {};

    td.id = this.nextId;

    td.date = Math.round(Date.now() / 1000);
    td.editby = "web";
    this.openDialog(td);
  }

  public deleteItem(item: filmEntry): void {
    // TODO: ADD nachfragen dialog
    this.http.delete(this.rest.gethost() + "filme/delete/" + item.id).subscribe((resdata: any) => {
      this.load();
    });
  }

  private getNextId(): number {
    var nextId: number = 0;
    this.farry.forEach((item: filmEntry) => {
      if (item.id > nextId) {
        nextId = item.id;
      }
    });
    return nextId * 1 + 1;
  }

  private save(item: filmEntry): void {
    this.http.post(this.rest.gethost() + "filme/save", item).subscribe((resdata: any) => {
      this.load();
    });
  }

  public openDialog(item: filmEntry): void {
    let edititem: filmEntry = this.serviceWipf.deepCopy(item);

    const dialogRef = this.dialog.open(filmeComponentDialog, {
      width: "350px",
      height: "350px",
      data: edititem,
    });

    dialogRef.afterClosed().subscribe((result: filmEntry) => {
      if (result) {
        result.editby = "web";
        result.date = Math.round(Date.now() / 1000);
        this.save(result);
      }
    });
  }
}

@Component({
  selector: "app-filme-dialog",
  templateUrl: "./filme.dialog.html",
})
export class filmeComponentDialog {
  constructor(public dialogRef: MatDialogRef<filmeComponentDialog>, @Inject(MAT_DIALOG_DATA) public data: filmEntry) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
