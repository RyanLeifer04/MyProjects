import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { AlertService } from 'src/app/services';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit, OnDestroy{
  private subscription!: Subscription;
  alert: any;

  constructor(private alertService: AlertService){}

  ngOnInit(){
    this.subscription = this.alertService.onAlert()
      .subscribe(alert => {
        switch (alert?.type){
          case 'success': alert.cssClass = 'alert alert-success'; break;
          case 'error': alert.cssClass = 'alert alert-danger'; break;
        }
        this.alert =alert;
      });
  }

  ngOnDestroy(){ this.subscription.unsubscribe(); }
}
