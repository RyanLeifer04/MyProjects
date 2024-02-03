import { Component, OnInit } from '@angular/core';

import { NotificationsService } from 'src/app/services/notifications.service';
import { NeedsService } from 'src/app/services';
import { Notification } from 'src/app/models/notification';
import { Needs } from 'src/app/models/needs';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  notifications: Notification[] = [];
  needs: Needs[] = []; // corresponding needs for each notification

  subscriptions: Needs[] = [];

  constructor(private notificationsService: NotificationsService,
    private needsService: NeedsService) { }

  async ngOnInit() {
    // load notifications
    this.notifications = await this.notificationsService.getNotifications();
    this.needs = [];

    // load corresponding needs
    for (let notification of this.notifications) {
      this.needsService.getById(notification.needID!).subscribe(need => {
        this.needs?.push(need);
      });
    }

    // load subscriptions
    let subscriptionIDs = await this.notificationsService.getSubscriptions();
    this.subscriptions = [];
    for (let id of subscriptionIDs) {
      this.needsService.getById(String(id)).subscribe(need => {
        this.subscriptions?.push(need);
      });
    }
  }

  removeNotification(notificationID: string) {
    this.notificationsService.removeNotification(Number(notificationID));
    this.ngOnInit();
  }

  unsubscribe(needID: number) {
    this.notificationsService.unsubscribeFromNeed(Number(needID));
    this.ngOnInit();
  }
}
