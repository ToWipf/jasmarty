import { Injectable } from '@angular/core';
import { Blowfish } from 'javascript-blowfish';

@Injectable({
  providedIn: 'root',
})

export class ServiceWipf {

  /**
   * 
   * @param oldObj 
   * @returns 
   */
  public deepCopy(oldObj: any): any {
    var newObj = oldObj;
    if (oldObj && typeof oldObj === 'object') {
      if (oldObj instanceof Date) {
        return new Date(oldObj.getTime());
      }
      newObj = Object.prototype.toString.call(oldObj) === '[object Array]' ? [] : {};
      for (var i in oldObj) {
        newObj[i] = this.deepCopy(oldObj[i]);
      }
    }
    return newObj;
  }

  /**
   * 
   * @param ms 
   * @returns 
   */
  public delay(ms: number): any {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  /**
   * Nummer zu string mit führender Null
   * 
   * @param num 
   * @param size 
   * @returns 
   */
  public pad(num: number | string, size: number): string {
    let value = num.toString();
    while (value.length < size) value = "0" + value;
    return value;
  }

  /**
   * 
   * @param str 
   * @param key 
   * @returns 
   */
  public crypt(str: string, key: string): string {
    if (str && key) {
      const bf = new Blowfish(key);
      return bf.encrypt(str);
    }
    return '';
  }

  /**
   * 
   * @param str 
   * @param key 
   * @returns 
   */
  public decrypt(str: string | null, key: string): string {
    if (str && key) {
      const bf = new Blowfish(key);
      return bf.trimZeros(bf.decrypt(str));
    }
    return '';
  }

  /**
   * prüfen auf type Nummer
   * 
   * @param n 
   * @returns 
   */
  public isNumber(n: any): boolean {
    return /^-?[\d.]+(?:e-?\d+)?$/.test(n);
  }

  /**
   * 
   * @param str 
   * @returns 
   */
  public startsWithNumber(str: string): boolean {
    return /^\d/.test(str);
  }

  /**
   * 
   * @returns 
   */
  public generateId() {
    return Math.random().toString(36).substring(2, 15) + '-' + Date.now();
  }


}
