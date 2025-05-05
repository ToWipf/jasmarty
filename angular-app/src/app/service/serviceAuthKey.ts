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
            if (name === ServicAuthKey.AUTH_KEY_NAME) {
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
        var key = this.serviceWipf.generateId();
        document.cookie = ServicAuthKey.AUTH_KEY_NAME + '=' + key + ';expires=' + xnow.toUTCString() + ';path=/;SameSite=Strict';

        // neuen Key senden
        this.rest.post("authkey/newkey", key);

        this.loadAuthKey();
    }

    /**
     * Alle Cookies um ein Jahr verlängern
     */
    public updateCookieExpiry(): void {
        const cookies = document.cookie.split('; '); // Leerzeichen nach Semikolon entfernen
        const expiryDate = new Date();
        expiryDate.setTime(expiryDate.getTime() + (356 * 24 * 60 * 60 * 1000)); // Neue Laufzeit in Tagen
    
        cookies.forEach(cookie => {
            const [name, ...valueParts] = cookie.split('=');
            const value = valueParts.join('='); // Falls der Wert '=' enthält
    
            // Das Cookie neu setzen
            document.cookie = `${name}=${value}; expires=${expiryDate.toUTCString()}; path=/`;
        });
    }

}

