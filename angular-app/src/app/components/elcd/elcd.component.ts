import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-elcd',
  templateUrl: './elcd.component.html',
  styleUrls: ['./elcd.component.less']
})
export class ElcdComponent implements OnInit {

  constructor(
    private http: HttpClient,
  ) { }

  public txt: string;
  public STATUS: string; // TODO: dialogfenster
  public online: string = 'ff';

  ngOnInit() {
    this.STATUS = "WAIT";
    this.http.get('http://192.168.2.10:8080/elcd/status/').subscribe((data) =>{
      console.log(data);
      this.online = data.toString();
      this.STATUS = data.toString();
    });
  }

  public sendMsg(): void {
    this.STATUS = "WAIT";
    this.http.put('http://192.168.2.10:8080/elcd/msg/' + this.txt, null).subscribe((data) =>{
      console.log(data);
      this.STATUS = data.toString();
    });
  }

  public cls(): void {
    this.STATUS = "WAIT";
    this.http.put('http://192.168.2.10:8080/elcd/cls', null).subscribe((data) => {
      console.log(data);
      this.STATUS = data.toString();
    });
  }
}
