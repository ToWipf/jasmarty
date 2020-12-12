import { Injectable } from '@angular/core';

@Injectable()
export class Globals {
  version: string = '0.95';
}

export interface Jaconfig {
  port?: string;
  width?: number;
  height?: number;
  refreshrate?: number;
  baudrate?: number;
}

export interface Japage {
  id?: number;
  name?: string;
  lines?: [{ line?: number; data?: string; option?: number }?];
}

export interface Buttonaction {
  id?: number;
  button?: number;
  active?: boolean;
  action?: string;
}

export interface DisplayLcd {
  width?: number;
  height?: number;
  display?: [{ line: number; data: string }];
}

export interface JapageForList {
  id?: number;
  name?: string;
}

export interface TodoEntry {
  id?: number;
  data?: string;
  active?: string;
  editby?: string;
  date?: number;
  remind?: string;
  chatid?: number;
}

export interface FilmEntry {
  id?: number;
  titel?: string;
  art?: string;
  editby?: string;
  date?: number;
  bewertung?: number;
  gesehenDate?: number;
  infotext?: string;
}

export interface CryptItem {
  key?: string;
  data?: string;
}

export interface Telegram {
  id?: number;
  mid?: number;
  data?: string;
  active?: string;
  editby?: string;
  date?: number;
  from?: string;
  chatid?: number;
  type?: string;
  message?: string;
  antwort?: string;
  options?: string;
}
