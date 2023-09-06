import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import { LoginReq } from 'src/app/Modules/LoginReq';
import {map, Observable} from 'rxjs';
import {LoginResp} from "../../Modules/LoginResp";
import {SignupRequest} from "../../Modules/SignupRequest";
@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private http:HttpClient) { }
  authenticate(U:LoginReq) {
    return this.http.post<LoginResp>("http://localhost:8089/SpringMVC/api/auth/signin", U)
    /*  .pipe(
        map(userData => {
          localStorage.setItem("id", et.idEt);
          let tokenStr = "Bearer " + userData.token;

          return userData;
        })
      );*/
  }


  logOut() {
    localStorage.removeItem("id");
    localStorage.removeItem("token");
  }


  IsLoggedIn(){
    return !!localStorage.getItem('id');
  }



  register(userData:SignupRequest): Observable<any> {
    const url = `http://localhost:8089/SpringMVC/api/auth/signup`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(url, userData, { headers });
  }
}
