import { Component, OnInit, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Jaconfig, Japage } from 'src/app/datatypes';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';

@Component({
  selector: 'app-jasmartyPages',
  templateUrl: './jasmartyPages.component.html',
  styleUrls: ['./jasmartyPages.component.less'],
})
export class JasmartyPagesComponent implements OnInit {
  constructor(private http: HttpClient, public dialog: MatDialog, private rest: ServiceRest) {}

  public sText: string;
  public sStatus: string;
  public Jaconfig: Jaconfig = {};
  public selectedPage: number = 1;
  public Japage: Japage = {};

  public optionnumbers: number[] = [48, 49, 50, 53];

  ngOnInit() {
    this.getCurrentSelectedSite();
    this.loadConfig();
  }

  public getCurrentSelectedSite(): void {
    this.http.get(this.rest.gethost() + 'pages/current').subscribe((resdata: Japage) => {
      this.selectedPage = resdata.id;
      this.Japage = resdata;
    });
  }

  public loadConfig(): void {
    this.http.get(this.rest.gethost() + 'lcd/config/get').subscribe((resdata) => {
      this.Jaconfig = resdata;
    });
  }

  public next(): void {
    this.selectedPage++;
    this.getSite();
  }

  public last(): void {
    if (this.selectedPage > 1) {
      this.selectedPage--;
    }
    this.getSite();
  }

  public toPage(pg: number): void {
    this.selectedPage = pg;
    this.getSite();
  }

  public save(): void {
    this.http.post(this.rest.gethost() + 'pages/save', JSON.stringify(this.Japage)).subscribe((resdata: any) => {
      if (resdata.save) {
        console.log('saved');
      } else {
        //TODO: Meldung Fehler
        console.log('fehler');
      }
    });
  }

  private getSite(): void {
    if (this.selectedPage > 2147483647 || this.selectedPage < 1) {
      this.selectedPage = 1;
    }

    this.http.get(this.rest.gethost() + 'pages/get/' + this.selectedPage).subscribe((resdata: Japage) => {
      this.Japage = resdata;

      if (this.Japage.id == 0) {
        this.newPage();
      }
    });
  }

  public selectPage(): void {
    this.http.get(this.rest.gethost() + 'pages/select/' + this.selectedPage).subscribe((resdata) => {
      console.log(resdata);
    });
  }

  public deletePage(): void {
    this.http.delete(this.rest.gethost() + 'pages/delete/' + this.selectedPage).subscribe((resdata) => {
      this.getSite();
    });
  }

  private newPage() {
    this.Japage = {};
    this.Japage.id = this.selectedPage;
    this.Japage.name = 'Seite ' + this.selectedPage;
    this.Japage.lines = [];
    for (let index = 0; index < this.Jaconfig.height; index++) {
      this.Japage.lines.push({
        data: '',
        line: index,
        option: 48,
      });
    }
  }

  public trackByIndex(index: number, obj: any): any {
    return index;
  }

  public saveAndSelectPage(): void {
    this.save();
    setTimeout(() => {
      this.selectPage();
    }, 1000);
  }

  public openDialogGoToPage(): void {
    var goToLine: number = this.selectedPage;

    const dialogRef = this.dialog.open(JasmartyPagesComponentGoToDialog, {
      width: '250px',
      height: '250px',
      data: goToLine,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.toPage(Math.round(result));
      }
    });
  }
}

@Component({
  selector: 'app-jasmartyPages-goToDialog',
  templateUrl: './jasmartyPages.goToDialog.html',
})
export class JasmartyPagesComponentGoToDialog {
  constructor(public dialogRef: MatDialogRef<JasmartyPagesComponentGoToDialog>, @Inject(MAT_DIALOG_DATA) public data: number) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
