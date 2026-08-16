import { Component, HostListener, OnInit } from '@angular/core';
import { ServiceRest } from 'src/app/service/serviceRest';
import { MatDialog } from '@angular/material/dialog';
import { DialogWartenComponent } from 'src/app/dialog/main.dialog';

@Component({
  selector: 'app-debugSeite',
  templateUrl: './debugSeite.component.html',
  styleUrls: ['./debugSeite.component.less'],
  standalone: false
})
export class DebugSeiteComponent implements OnInit {
  constructor(private rest: ServiceRest, public dialog: MatDialog) { }

  public sSQL_IN: string = "";
  public sSQL_OUT: string = "";
  public serverTime: number = 0;
  public clientTime: number = 0;
  public isKeyboardVisible: boolean = false;

  public testData = [
    { name: "ABC", value: 2 },
    { name: "Etwas", value: 4 },
    { name: "bbb", value: 2 },
    { name: "xyz", value: 0 },
    { name: "20.20.20", value: 10 }
  ];

  public barOption: any;
  public pieOption: any;
  public advancedPieOption: any;
  public pieGridOption: any;

  ngOnInit() {
    this.loadUhr();
    this.buildChartOptions();
  }

  private buildChartOptions(): void {
    const names = this.testData.map((d) => d.name);
    const values = this.testData.map((d) => d.value);

    this.barOption = {
      title: { text: 'Test Bar' },
      tooltip: {},
      xAxis: { type: 'category', data: names },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: values }]
    };

    this.pieOption = {
      title: { text: 'Test Pie' },
      tooltip: { trigger: 'item' },
      legend: { top: '5%' },
      series: [{ type: 'pie', radius: '50%', data: this.testData.map(d => ({ name: d.name, value: d.value })) }]
    };

    this.advancedPieOption = this.pieOption;

    this.pieGridOption = this.pieOption;
  }

  public loadUhr(): void {
    this.getTime();
    this.clientTime = Date.now();
  }

  public getTime(): void {
    this.rest.get('wipf/time').then((resdata: any) => {
      this.serverTime = resdata.time;
    });
  }

  public startDailyTask(): void {
    this.rest.post('debug/dailyTask', {}).then((resdata: any) => {
      this.serverTime = resdata.time;
    });
  }

  public warten(): void {
    const warten = this.dialog.open(DialogWartenComponent, {});
  }

  @HostListener('window:focusin', ['$event'])
  onFocusIn(event: FocusEvent): void {
    this.isKeyboardVisible = true;
  }

  @HostListener('window:focusout', ['$event'])
  nFocusOut(event: FocusEvent): void {
    this.isKeyboardVisible = false;
  }

}
