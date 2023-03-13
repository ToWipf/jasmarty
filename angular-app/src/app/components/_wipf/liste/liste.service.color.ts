import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})

export class ListeServiceColor {
  constructor() { }

  public idToColor(id: number): string {
    if (id === 1)
      return "#fc5f53";
    else if (id === 2)
      return "#fcc653";
    else if (id === 3)
      return "#99fc53";
    else if (id === 4)
      return "#53fca2";
    else if (id === 5)
      return "#53ddfc";
    else if (id === 6)
      return "#5e53fc";
    else if (id === 7)
      return "#be53fc";
    else if (id === 8)
      return "#fc53be";
    else if (id === 9)
      return "gray";
    else if (id === -1)
      return "green";
    else
      return "#d9d9d9";
  }
}
