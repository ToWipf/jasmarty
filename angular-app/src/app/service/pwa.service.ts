import { Platform } from '@angular/cdk/platform';
import { Injectable } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { timer, take } from 'rxjs';
import { PWAPromptComponent } from '../components/_main/pwa-prompt-component/pwa-prompt-component.component';

@Injectable({
  providedIn: 'root'
})
export class PwaService {
  private promptEvent: any;

  constructor(
    private bottomSheet: MatBottomSheet,
    private platform: Platform
  ) { }

  public initPwaPrompt() {
    
    console.log("0")
    window.addEventListener('beforeinstallprompt', (event: any) => {
      console.log("1")
      event.preventDefault();
      this.promptEvent = event;
      this.bottomSheet.open(PWAPromptComponent, { data: { promptEvent: this.promptEvent } })
    });

  }

}