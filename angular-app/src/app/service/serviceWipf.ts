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
   * Nummer zu String mit führender Null
   * 
   * @param num 
   * @param size 
   * @returns 
   */
  public pad(num, size): any {
    num = num.toString();
    while (num.length < size) num = "0" + num;
    return num;
  }

  /**
   * 
   * @param str 
   * @param key 
   * @returns 
   */
  public crypt(str: string, key: string): string {
    var bf = new Blowfish(key);
    return bf.encrypt(str);
  }

  /**
   * 
   * @param str 
   * @param key 
   * @returns 
   */
  public decrypt(str: string, key: string): string {
    var bf = new Blowfish(key);
    return bf.trimZeros(bf.decrypt(str));
  }

}
