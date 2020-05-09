import { Injectable } from "@angular/core";

@Injectable()
export class Globals {
  version: string = "0.56";
}

export interface jaconfig {
  port?: string;
  width?: number;
  height?: number;
  refreshrate?: number;
  baudrate?: number;
}

export interface japage {
  id?: number;
  name?: string;
  lines?: [{ line?: number; data?: string; option?: number }?];
}

export interface buttonaction {
  id?: number;
  button?: number;
  active?: boolean;
  action?: string;
}

export interface displayLcd {
  width?: number;
  height?: number;
  display?: [{ line: number; data: string }];
}

export interface japageForList {
  id?: number;
  name?: string;
}
