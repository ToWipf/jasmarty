import { Component, Inject } from "@angular/core";
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DialogInfoContent, DialogInputOneThingContent } from "../datatypes";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { SharedMaterialModule } from '../shared/shared-material.module';

@Component({
  templateUrl: './jaNein.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, SharedMaterialModule]
})
export class DialogJaNeinComponent {
  constructor(public dialogRef: MatDialogRef<DialogJaNeinComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './warten.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, SharedMaterialModule]
})
export class DialogWartenComponent {
  constructor(public dialogRef: MatDialogRef<DialogWartenComponent>, @Inject(MAT_DIALOG_DATA) public data: null) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './inputOneThing.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, SharedMaterialModule]
})
export class DialogInputOneThingComponent {
  constructor(public dialogRef: MatDialogRef<DialogInputOneThingComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInputOneThingContent) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './variablen.hilfe.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, SharedMaterialModule]
})
export class DialogVariablenHilfeComponent {
  constructor(public dialogRef: MatDialogRef<DialogVariablenHilfeComponent>, @Inject(MAT_DIALOG_DATA) public data: null) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  templateUrl: './infobox.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, SharedMaterialModule]
})
export class DialogInfoboxComponent {
  constructor(public dialogRef: MatDialogRef<DialogInfoboxComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogInfoContent) { }
}
