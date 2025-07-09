import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceRest } from '../service/serviceRest';

@Component({
    selector: 'app-setServer',
    templateUrl: './setServer.dialog.html',
    standalone: false
})
export class ElementSetServerDialog {
  constructor(public dialogRef: MatDialogRef<ElementSetServerDialog>, @Inject(MAT_DIALOG_DATA) public data: string, private rest: ServiceRest) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public reset(): void {
    this.data = this.rest.getHostExpectFromUrl();
  }
}
