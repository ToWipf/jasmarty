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
  
}
