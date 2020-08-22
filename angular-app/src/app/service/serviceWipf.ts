import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})

export class ServiceWipf {

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

}
