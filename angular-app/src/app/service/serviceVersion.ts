import { Injectable, OnInit } from '@angular/core';
import { ServiceRest } from './serviceRest';
import { ServiceWipf } from './serviceWipf';
import packageJson from '../../../package.json';
import { MatDialog } from '@angular/material/dialog';
import { CookieDialogComponent } from '../dialog/cookie.dialog';

@Injectable({
    providedIn: 'root',
})

export class ServiceVersion {
    constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

    private sBackend: string;

    /**
     * 
     */
    public loadBackend(): void {
        // Set Backend
        this.rest.getNoWartenDialog('wipf/ver').then((resdata: any) => {
            this.sBackend = resdata.ver.toString();
        });
    }

    /**
     * 
     * @returns 
     */
    public checkVersions(): boolean {
        return (this.getFrontendVersion() !== this.getBackendVersion());
    }

    /**
     * 
     * @returns 
     */
    public getBackendVersion(): string {
        return this.sBackend;
    }

    /**
     * 
     * @returns 
     */
    public getFrontendVersion(): string {
        return packageJson.version;
    }

    /**
     * 
     */
    public openCookieDialog(): void {
        this.dialog.open(CookieDialogComponent, {
            autoFocus: true,
            minWidth: '300px',
            minHeight: '250px',
        });
    }

    /**
     * 
     */
    public reloadApp(): void {
        window.location.reload();
    }

    /**
     * 
     */
    public redirectToApiFrontend(): void {
        //window.location.replace(this.rest.gethost());
        window.location.href = this.rest.gethost();
    }
}