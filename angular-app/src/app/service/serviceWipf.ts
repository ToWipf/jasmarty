import { resolve } from '@angular/compiler-cli/src/ngtsc/file_system';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})

export class ServiceWipf {

  /**
   * 
   * @param oldObj 
   * @returns 
   */
  public deepCopy(oldObj: any) {
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
  public delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

}
