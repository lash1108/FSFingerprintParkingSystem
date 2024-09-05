import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EstacionamientoService {
  //baseURL = `${environment.backendURL}/est`;
  baseURL = `http://parking-uaem.ddns.net:8080/est`;
  headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) { }

  createOrUpdateEst(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/CreateOrUpdateEst`, data, {headers});
  }

  findEstacionamientos(data:any):Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/findEstacionamientos`, data, {headers});
  }

}
