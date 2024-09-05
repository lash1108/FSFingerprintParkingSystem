import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegistryService {
  //baseURL = `${environment.backendURL}/registry`;
  baseURL = `http://parking-uaem.ddns.net:8080/registry`;

  headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  setNewRegistry(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/setNewRegistryByToken`, data, {
      headers,
    });
  }

  unsetRegistry(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/unsetNewRegistryByToken`, data, {
      headers,
    });
  }

  findRegistryByToken(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/findRegistryByToken`, data, {
      headers,
    });
  }
}
