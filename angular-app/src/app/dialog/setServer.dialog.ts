import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ServiceRest } from '../service/serviceRest';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-setServer',
  templateUrl: './setServer.dialog.html',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule]
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
