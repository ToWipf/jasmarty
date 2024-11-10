import { Component, Inject } from '@angular/core';
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-pwa-prompt-component',
  standalone: true,
  imports: [],
  templateUrl: './pwa-prompt-component.component.html',
  styleUrl: './pwa-prompt-component.component.less'
})
export class PWAPromptComponent {

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) public data: { promptEvent?: any },
    private bottomSheetRef: MatBottomSheetRef<PWAPromptComponent>
  ) {}

  public installPwa(): void {
    this.data.promptEvent.prompt();
    this.close();
  }

  public close() {
    this.bottomSheetRef.dismiss();
  }
}