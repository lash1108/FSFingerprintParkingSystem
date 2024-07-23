import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseURL = `${environment.backendURL}/usr`;
  headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) {
  }

  validLogin(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/validLogin`, data, {headers});
  }

  validEmail(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/validEmail`, data, {headers});
  }

  createOrUpdateUsr(data: any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/createOrUpdateUsr`, data, {headers});
  }

  validUsrByToken(data:any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/findUserByToken`, data, {headers});
  }
}
