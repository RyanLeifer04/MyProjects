import { Component } from '@angular/core';

import { User } from 'src/app/models';
import { AccountService } from 'src/app/services';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  user: User | null;

  constructor(private accountService: AccountService){
    this.user = this.accountService.userValue;
  }
}
