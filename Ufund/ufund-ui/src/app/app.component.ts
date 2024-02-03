import { Component } from '@angular/core';

import { AccountService } from './services';
import { User } from './models';
import { environment } from '../environments/environment';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ufund-ui';

  user?: User | null;

  constructor(private accountService: AccountService){
    this.accountService.user.subscribe(x => this.user = x);
    //console.log(environment.apiUrl);
  }

  logout(){
    this.accountService.logout();
  }
}
