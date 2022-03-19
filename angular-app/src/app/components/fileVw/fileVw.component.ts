import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Telegram, TelegramUserCache } from 'src/app/datatypes';
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
  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) {

  }
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['name', 'button'];
  public filenames: String[] = [];
  public sFilter: string = "";
  public fileToUpload: File | null = null;

  ngOnInit(): void {
    this.load();
  }

  public load(): void {
    this.filenames = [];
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.http.get(this.rest.gethost() + 'file/getAll').subscribe((resdata: String[]) => {
      this.filenames = resdata;

      this.dataSource = new MatTableDataSource(this.filenames);
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
        this.http.delete(this.rest.gethost() + 'file/del/' + name).subscribe((resdata: any) => {
          this.load();
        });
      }
    });
  }

  public downloadItem(name: String): void {
    window.open(this.rest.gethost() + 'file/download/' + name);
  }

  public uploadFile(files: FileList) {
    this.fileToUpload = files.item(0);
    //TODO
  }

}
