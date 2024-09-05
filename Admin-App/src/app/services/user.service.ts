import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {firstValueFrom, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  //baseURL = `${environment.backendURL}/usr`;
  baseURL = `http://parking-uaem.ddns.net:8080/usr`;

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

  findUserByToken(data:any): Observable<any> {
    const headers = this.headers;
    return this.http.post(`${this.baseURL}/findUserByToken`, data, {headers});
  }

  async validateUsrByToken(token: string): Promise<boolean> {
    const data: { name?: string } = { name: token };
    try {
      const response = await firstValueFrom(this.findUserByToken(data));
      console.log(response.datos);
      if (response.datos.code === 200) {
        this.setDataOnLocalStorage(response);
        return true;
      } else {
        return false
      }
    } catch (error) {
      console.log(error)
      return false;
    }
  }

  private setDataOnLocalStorage(response:any){
    localStorage.setItem('responseUser', JSON.stringify(response));
    const keys = Object.keys(response.datos);
    const key = parseInt(keys[0]);
    const userData = response.datos[key];
    localStorage.setItem('typeusr', userData.typeusr);
    localStorage.setItem('cveusr', String(key));
  }


}
