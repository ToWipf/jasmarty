import { View360Options } from "@egjs/ngx-view360";

export interface Jaconfig {
  port?: string;
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
  frage?: string;
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
  timeoutTime?: number;
  staticData?: boolean[][];
  dynamicData?: Lcd12864PageDescriptionDynamic[];
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
  typid?: string;
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
  color?: string;
  prio?: number;
}

export interface TelegramUserCache {
  chatid?: number;
  msg?: string;
  usercache?: string;
  counter?: number;
}

export interface TeleMsg {
  id?: number;
  frage?: number;
  antwort?: string;
}

export interface RndEvent {
  id?: number;
  eventtext?: string;
  active?: boolean;
}

export interface KeyValEntry {
  key?: string;
  value?: string;
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

export interface AuthKey {
  id?: number;
  key?: string;
  info?: string;
  access?: boolean;
}

export interface WipfImage360 {
  name?: string;
  pic?: Partial<View360Options>;
}

export interface CheckListeListe {
  id?: number;
  date?: string;
  listenname?: string;
  types?: number[];
}

export interface CheckListeType {
  id?: number;
  type?: string;
}

export interface CheckListeItem {
  id?: number;
  item?: string;
  type?: CheckListeType;
  prio?: number;
}

export interface CheckListeVerkn {
  id?: number;
  checked?: boolean;
  checkListeItem?: CheckListeItem;
  checkListeListe?: CheckListeListe;
}