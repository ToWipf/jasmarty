import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatSort } from "@angular/material/sort";
import { ListeEntry } from "src/app/datatypes";
import { ServiceRest } from "src/app/service/serviceRest";
import { ServiceWipf } from "src/app/service/serviceWipf";

@Component({
    selector: 'app-listecrypt-dialog',
    templateUrl: './listeCrypt.dialog.html',
    styleUrls: ['./liste.component.less']
})

export class ListeCryptComponentDialogComponent implements OnInit {
    constructor(public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialogRef: MatDialogRef<ListeCryptComponentDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ListeEntry) { }

    public sDecrypt: string = "";
    public sKey: string = "";

    @ViewChild(MatSort, { static: true }) sortType: MatSort;

    public ngOnInit() {
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }

    public changeKey(): void {
        this.serviceWipf.delay(300).then(() => {
            this.sDecrypt = this.serviceWipf.decrypt(this.data.data, this.sKey);
        });
    }

    public changeDaten(): void {
        this.serviceWipf.delay(300).then(() => {
            this.data.data = this.serviceWipf.crypt(this.sDecrypt, this.sKey);
        });
    }

}
