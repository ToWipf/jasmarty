import { Component, OnInit, Inject } from '@angular/core';
import { Jaconfig, Japage } from 'src/app/datatypes';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { DialogVariablenHilfeComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-jasmartyPages',
  templateUrl: './jasmartyPages.component.html',
  styleUrls: ['./jasmartyPages.component.less'],
})
export class JasmartyPagesComponent implements OnInit {
  constructor(public dialog: MatDialog, private rest: ServiceRest) { }

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
    this.rest.get('pages/current').then((resdata: Japage) => {
      this.selectedPage = resdata.id;
      this.Japage = resdata;
    });
  }

  public loadConfig(): void {
    this.rest.get('lcd/config/get').then((resdata) => {
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
    this.rest.post('pages/save', JSON.stringify(this.Japage)).then((resdata: any) => {
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

    this.rest.get('pages/get/' + this.selectedPage).then((resdata: Japage) => {
      this.Japage = resdata;

      if (this.Japage.id == 0) {
        this.newPage();
      }
    });
  }

  public selectPage(): void {
    this.rest.get('pages/select/' + this.selectedPage).then((resdata) => {
    });
  }

  public deletePage(): void {
    this.rest.delete('pages/delete/' + this.selectedPage).then((resdata) => {
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
      minWidth: '200px',
      minHeight: '150px',
      data: goToLine,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.toPage(Math.round(result));
      }
    });
  }

  public openariablenHilfeDialog(): void {
    this.dialog.open(DialogVariablenHilfeComponent, { minWidth: '200px', height: '300px' });
  }
}

@Component({
  selector: 'app-jasmartyPages-goToDialog',
  templateUrl: './jasmartyPages.goToDialog.html',
})
export class JasmartyPagesComponentGoToDialog {
  constructor(public dialogRef: MatDialogRef<JasmartyPagesComponentGoToDialog>, @Inject(MAT_DIALOG_DATA) public data: number) { }

  public onNoClick(): void {
    this.dialogRef.close();
  }
}
