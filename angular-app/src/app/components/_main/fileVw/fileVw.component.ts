import { Component, OnInit, ViewChild } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DialogJaNeinComponent, DialogWartenComponent } from 'src/app/dialog/main.dialog';
import { MatDialog } from '@angular/material/dialog';
import { take } from 'rxjs';

@Component({
    selector: 'app-file-vw',
    templateUrl: './fileVw.component.html',
    styleUrls: ['./fileVw.component.less'],
    standalone: false
})
export class FileVwComponent implements OnInit {
  constructor(private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  public dataSource;
  public displayedColumns: string[] = ['name', 'button'];
  public sFilter: string = "";

  ngOnInit(): void {
    this.load();
  }

  public load(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});

    this.rest.get('file/getAll').then((resdata: string[]) => {
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

  public deleteItem(name: string): void {
    let item: any = {};
    item.infotext = "Wirklich löschen?";
    item.infotext2 = name;
    const dialogRef = this.dialog.open(DialogJaNeinComponent, {
      minWidth: '200px',
      minHeight: '150px',
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

  public async downloadItem(name: string): Promise<void> {
    this.rest.downloadFile('../file/download/' + name).pipe(take(1)).subscribe((response) => {
      const downloadLink = document.createElement('a');
      downloadLink.href = URL.createObjectURL(new Blob([response.body], { type: response.body.type }));

      downloadLink.download = name;
      downloadLink.click();
    });
  }

  public uploadFile(files: FileList) {
    var fileToUpload: File | null = null;
    fileToUpload = files.item(0);

    this.rest.post('file/upload/' + fileToUpload.name, fileToUpload).then((resdata: any) => {
      this.load();
    });
  }

}
