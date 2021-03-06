export interface Jaconfig {
  port?: string;
  width?: number;
  height?: number;
  refreshrate?: number;
  baudrate?: number;
  type?: string;
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

export interface WipfUser {
  username?: string;
  password?: string;
  role?: string;
  telegramid?: number;
}

export interface Lcd12864PageDescription {
  id?: number;
  name?: string;
  timeouttime?: number;
  static?: boolean[][];
  dynamic?: Lcd12864PageDescriptionDynamic[];
}

export interface Lcd12864PageDescriptionDynamic {
  type?: string;
  font?: string;
  data?: string;
  x?: string;
  y?: string;
}
