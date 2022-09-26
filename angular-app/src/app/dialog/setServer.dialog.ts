import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-setServer',
  templateUrl: './setServer.dialog.html',
})
export class ElementSetServerDialog {
  constructor(public dialogRef: MatDialogRef<ElementSetServerDialog>, @Inject(MAT_DIALOG_DATA) public data: string) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
