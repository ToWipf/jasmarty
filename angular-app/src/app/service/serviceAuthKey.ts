import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ServiceRest } from "./serviceRest";
import { ServiceWipf } from "./serviceWipf";

@Injectable({
    providedIn: 'root',
})

export class ServicAuthKey {
    constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

    private sAuthKey: string;
    private static AUTH_KEY_NAME: string = "authKey";

    /**
     * 
     */
    public loadAuthKey(): void {
        // Set Backend
        document.cookie.split(/\s*;\s*/).forEach((pair) => {
          var name = decodeURIComponent(pair.substring(0, pair.indexOf('=')));
          if (name === ServicAuthKey.AUTH_KEY_NAME){
              this.sAuthKey = decodeURIComponent(pair.substring(pair.indexOf('=') + 1));
          }
        });
    }

    /**
     * 
     * @returns 
     */
    public getAuthKey(): string {
        return this.sAuthKey;
    }

    /**
     * 
     */
    public setRndAuthKey(): void {
        var xnow = new Date();
        var time = xnow.getTime();
        var expireTime = time + 1000 * 36000 * 9999;
        xnow.setTime(expireTime);
        document.cookie = ServicAuthKey.AUTH_KEY_NAME + '=' + this.generateId() + ';expires=' + xnow.toUTCString() + ';path=/;SameSite=Strict';

        this.loadAuthKey();
    }

    private generateId() {
        return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    }

}

