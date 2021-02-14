import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    templateUrl: './jaNein.dialog.html',
  })
  export class JaNeinDialogComponent {
    constructor(public dialogRef: MatDialogRef<JaNeinDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }
  
    onNoClick(): void {
      this.dialogRef.close();
    }
  }
  