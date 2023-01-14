import { Component, OnInit, ViewChild, Inject } from "@angular/core";
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ListeType } from "src/app/datatypes";
import { DialogWartenComponent, DialogJaNeinComponent } from "src/app/dialog/main.dialog";
import { ServiceRest } from "src/app/service/serviceRest";
import { ServiceWipf } from "src/app/service/serviceWipf";

@Component({
    selector: 'app-listetype-dialogtypelist',
    templateUrl: './listeType.dialogTypeList.html',
    styleUrls: ['./liste.component.less']
})

export class ListeTypeComponentDialogTypeListComponent implements OnInit {
    constructor(public dialogRef: MatDialogRef<ListeTypeComponentDialogTypeListComponent>, public dialog: MatDialog, private rest: ServiceRest, public serviceWipf: ServiceWipf) { }

    public dataSource;
    public eventlistDisplayedColumns: string[] = ['id', 'type', 'button'];
    public sFilter: string = "";
    public bShowWarning: boolean = false;
    @ViewChild(MatSort, { static: true }) sortType: MatSort;

    ngOnInit() {
        this.loadType();
    }

    public newType(): void {
        let e: ListeType = {};
        e.typename = "";
        this.openDialogType(e);
    }

    public editType(e: ListeType): void {
        this.openDialogType(e);
    }

    public openDialogType(item: ListeType): void {
        const edititem: ListeType = this.serviceWipf.deepCopy(item);

        const dialogRef = this.dialog.open(ListeTypeComponentDialogTypeComponent, {
            data: edititem,
            autoFocus: true,
            minWidth: '200px',
            minHeight: '150px',
        });

        dialogRef.afterClosed().subscribe((result: ListeType) => {
            if (result) {
                this.saveType(result);
            }
        });
    }

    private saveType(item: ListeType): void {
        this.bShowWarning = true;
        this.rest.post('listeType/save', item).then((resdata: any) => {
            if (resdata) {
                this.bShowWarning = false;
                this.loadType();
            }
        });
    }

    public loadType(): void {
        const warten = this.dialog.open(DialogWartenComponent, {});

        this.rest.get('listeType/getAll').then((resdata: ListeType[]) => {
            this.dataSource = new MatTableDataSource(resdata);
            this.dataSource.sort = this.sortType;
            warten.close();
        });
    }

    public deleteType(item: any): void {
        item.infotext = "Wirklich löschen?";
        item.infotext2 = item.typename;
        const dialogRef = this.dialog.open(DialogJaNeinComponent, {
            minWidth: '200px',
            minHeight: '150px',
            data: item,
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.rest.delete('listeType/delete/' + item.id).then((resdata: any) => {
                    this.loadType();
                });
            }
        });
    }

    public applyFilter() {
        this.serviceWipf.delay(200).then(() => {
            this.dataSource.filter = this.sFilter.trim();
        });
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }
}


@Component({
    selector: 'app-listeType-dialogtype',
    templateUrl: './listeType.dialogType.html',
})
export class ListeTypeComponentDialogTypeComponent {
    constructor(public dialogRef: MatDialogRef<ListeTypeComponentDialogTypeComponent>, @Inject(MAT_DIALOG_DATA) public data: ListeType) { }

    public onNoClick(): void {
        this.dialogRef.close();
    }
}
