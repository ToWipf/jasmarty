import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-file-vw',
  templateUrl: './fileVw.component.html',
  styleUrls: ['./fileVw.component.less'],
})
export class FileVwComponent implements OnInit {
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['name', 'button'];
  public sFilter: string = "";
  public fileToUpload: File | null = null;

  ngOnInit(): void {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('file/getAll').then((resdata: String[]) => {
      this.dataSource = new MatTableDataSource(resdata);
      this.dataSource.sort = this.sort;
      this.dataSource.filter = this.sFilter.trim();
      this.applyFilter();
      warten.close();
    });
  }

  public applyFilter() {
    this.serviceWipf.delay(200).then(() => {
      this.dataSource.filter = this.sFilter.trim();
    });
  }

  public deleteItem(name: String): void {
    let item: any = {};
    item.infotext = "Wirklich löschen? \n" + name;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      width: '250px',
      height: '250px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.rest.delete('file/del/' + name).then((resdata: any) => {
          this.load();
        });
      }
    });
  }

  public downloadItem(name: String): void {
    window.open('file/download/' + name);
  }

  // public uploadFile(files: FileList) {
  //   this.fileToUpload = files.item(0);
  // }

}
