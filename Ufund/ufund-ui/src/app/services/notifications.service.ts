import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { AccountService } from 'src/app/services';
import { Notification } from '../models/notification';
import { firstValueFrom, lastValueFrom } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class NotificationsService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private accountService: AccountService,
    private router: Router,
    private http: HttpClient
  ) { }

  getNotifications() {
    let url = `/notifications/${+this.accountService.userValue?.id!}`;
    console.log("GET " + url);
    return lastValueFrom(this.http.get<Notification[]>(url));
  }

  removeNotification(notificationID: number) {
    let url = `/notifications/${+this.accountService.userValue?.id!}/${notificationID}`;
    console.log("DELETE " + url);
    this.http.delete(url).subscribe()
  }

  getSubscriptions() {
    let url = `/notifications/${+this.accountService.userValue?.id!}/subscriptions`;
    console.log("GET " + url);
    return lastValueFrom(this.http.get<number[]>(url));
  }

  subscribeToNeed(needID: number) {
    let url = `/notifications/${+this.accountService.userValue?.id!}/subscriptions/${needID}`;
    console.log("POST " + url);
    this.http.post(url, null).subscribe();
  }

  unsubscribeFromNeed(needID: number) {
    let url = `/notifications/${+this.accountService.userValue?.id!}/subscriptions/${needID}`;
    console.log("DELETE " + url);
    this.http.delete(url).subscribe();
  }

  pushNotification(needID: string, message: string) 
  {
    let url = `/notifications/push/${needID}`;
    console.log("POST " + url)
    return this.http.post<Notification>(url, message);
  }
}
