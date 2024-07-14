import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  private typeUser: number = 0;
  constructor() { }

  getTypeUser(): number{
    return this.typeUser;
  }

  setTypeUser(type: number):void{
    this.typeUser = type;
  }
}
