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

export interface MedienEntry {
  id?: number;
  titel?: string;
  art?: string;
  editby?: string;
  date?: number;
  bewertung?: number;
  gesehenDate?: number;
  infotext?: string;
  typ?: string
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
  id?: number;
  username?: string;
  password?: string;
  role?: string;
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

export interface DaylogDay {
  id?: number;
  userid?: number;
  date?: string;
  tagestext?: string;
  extrafeld_wochentag?: string; 
}

export interface DaylogEvent {
  id?: number;
  dateid?: number;
  typ?: string;
  text?: string;
}

export interface DynpageEntry {
  id?: number;
  html?: string;
  script?: string;
  style?: string;
  rechte?: string;
  live?: boolean;
}

export interface DaylogType {
  id?: number;
  type?: string;
  art?: string;
}

export interface TelegramUserCache {
  chatid?: number;
  msg?: string;
  usercache?: string;
  counter?: number;
}

export interface RndEvent {
  id?: number;
  eventtext?: string;
  active?: boolean;
}

export interface KeyValEntry {
  key?: string;
  val?: string;
}

export interface ListeEntry {
  id?: number;
  data?: string;
  typeid?: number;
  date?: string;
}

export interface ListeType {
  id?: number;
  typename?: string;
}

export interface DialogInfoContent {
  infotext?: string;
  infotext2?: string;
}
