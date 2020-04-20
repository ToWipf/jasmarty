import { Injectable } from '@angular/core';

@Injectable()
export class Globals {
   version:string = "0.400";
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
  options?: string;
  lines?: string;
}

export interface buttonaction {
  id?: number;
  button?: number;
  active?: boolean;
  action?: string;
}
