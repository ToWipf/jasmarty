import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogInfoContent } from "../datatypes";

@Component({
  templateUrl: './jaNein.dialog.html',
})
export class DialogJaNeinComponent {
  constructor(public dialogRef: MatDialogRef<DialogJaNeinComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './warten.dialog.html',
})
export class DialogWartenComponent {
  constructor(public dialogRef: MatDialogRef<DialogWartenComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './variablen.hilfe.dialog.html',
})
export class DialogVariablenHilfeComponent {
  constructor(public dialogRef: MatDialogRef<DialogVariablenHilfeComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
