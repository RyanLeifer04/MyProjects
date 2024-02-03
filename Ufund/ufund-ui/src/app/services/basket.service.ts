import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';

import { AccountService } from 'src/app/services';
import { Needs } from 'src/app/models';


@Injectable({
  providedIn: 'root',
})
export class BasketService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private accountService: AccountService,
    private router: Router,
    private http: HttpClient
  ) { }

  getBasketNeeds() {
    let url = `/basket/${+this.accountService.userValue?.id!}`;
    console.log("GET " + url);
    return this.http.get<Needs[]>(url);
  }

  addNeedToBasket(id: number) {
    let url = `/basket/${+this.accountService.userValue?.id!}`;
    console.log("POST " + url + " " + id);
    return this.http.post<Needs>(url, id, this.httpOptions).subscribe();
  }

  removeNeedFromBasket(needID: number) {
    let url = `/basket/${+this.accountService.userValue?.id!}/${needID}`;
    console.log("DELETE " + url);
    this.http.delete<Needs>(url).subscribe();
  }

  getNeedFromBasket(needID: number) {
    let url = `/basket/${+this.accountService.userValue?.id!}/${needID}`;
    console.log("GET " + url);
    return this.http.get<Needs>(url);
  }

  checkout(basket: { [needID: number]: number }) {
    let url = `/basket/${+this.accountService.userValue?.id!}/checkout`;
    console.log("POST " + url + " " + basket.toString());
    return lastValueFrom(this.http.post(url, basket, this.httpOptions));
  }
}
