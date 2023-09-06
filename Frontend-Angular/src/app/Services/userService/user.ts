import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class User {



  constructor(private http: HttpClient) { }

  public GetUsesrByRole(role:string):Observable<any> {
    return this.http.get<any>("http://localhost:8089/SpringMVC/api/user/all/"+role);
  }


  public DeleteUser(id:number):Observable<any> {
    return this.http.delete<any>("http://localhost:8089/SpringMVC/api/user/delete/"+id);
  }


  private baseUrl = 'http://localhost:8089/SpringMVC/api/user';
  updateBuyer(userId: number, userData: any): Observable<any> {
    const url = `${this.baseUrl}/updateClient/${userId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put(url, userData, { headers });
  }

  updateOperator(userId: number, userData: any): Observable<any> {
    const url = `${this.baseUrl}/updateOperatuer/${userId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put(url, userData, { headers });
  }

  updatesupplier(userId: number, userData: any): Observable<any> {
    const url = `${this.baseUrl}/updateCompany/${userId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put(url, userData, { headers });
  }



}









