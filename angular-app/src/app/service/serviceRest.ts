import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root",
})

export class ServiceRest {

  private host: string = "http://localhost:8080/";

  public gethost(): string {
    return this.host;
  }

  public sethost(host: string): void {
    this.host = host;
  }

  public sethostExpect(): void {
    let sHref = window.location.href;
    let sTmp = sHref.substring(0, sHref.lastIndexOf("/"));
    this.host = sTmp.substring(0, sTmp.lastIndexOf("/") + 1);
  }
  
}
