import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogInfoContent, DialogInputOneThingContent } from "../datatypes";

@Component({
    templateUrl: './jaNein.dialog.html',
    standalone: false
})
export class DialogJaNeinComponent {
  constructor(public dialogRef: MatDialogRef<DialogJaNeinComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
    templateUrl: './warten.dialog.html',
    standalone: false
})
export class DialogWartenComponent {
  constructor(public dialogRef: MatDialogRef<DialogWartenComponent>, @Inject(MAT_DIALOG_DATA) public data: null) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
    templateUrl: './inputOneThing.dialog.html',
    standalone: false
})
export class DialogInputOneThingComponent {
  constructor(public dialogRef: MatDialogRef<DialogInputOneThingComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInputOneThingContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
    templateUrl: './variablen.hilfe.dialog.html',
    standalone: false
})
export class DialogVariablenHilfeComponent {
  constructor(public dialogRef: MatDialogRef<DialogVariablenHilfeComponent>, @Inject(MAT_DIALOG_DATA) public data: null) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
    templateUrl: './infobox.dialog.html',
    standalone: false
})
export class DialogInfoboxComponent {
  constructor(public dialogRef: MatDialogRef<DialogInfoboxComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }
}
