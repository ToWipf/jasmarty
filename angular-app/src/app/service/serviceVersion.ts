import { Injectable } from '@angular/core';
import { ServiceRest } from './serviceRest';
import { ServiceWipf } from './serviceWipf';
import packageJson from '../../../package.json';
import { MatDialog } from '@angular/material/dialog';
import { CookieDialogComponent } from '../dialog/cookieVW/cookie.dialog';
import { LocalStorageDialogComponent } from '../dialog/localStorageVW/localStorageVW.dialog';

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
            minWidth: '200px',
            minHeight: '150px',
        });
    }

    public openLocalStorageDialog(): void {
        this.dialog.open(LocalStorageDialogComponent, {
            autoFocus: true,
            minWidth: '200px',
            minHeight: '150px',
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
        window.location.href = this.rest.gethost();
    }
}
