import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root",
})

// Vault Userverwaltung
export class ServiceRest {

  public gethost(): string {
    return "http://localhost:8080/";
  }
  
}
