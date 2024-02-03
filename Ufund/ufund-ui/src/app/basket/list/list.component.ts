import { Component, OnInit } from '@angular/core';

import { BasketService } from 'src/app/services/basket.service';
import { Needs } from 'src/app/models/needs';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  needs: { [needID: number]: Needs } = {};
  fundingAmounts: { [needID: number]: number } = {};

  constructor(private basketService: BasketService) { }

  ngOnInit() {
    this.basketService.getBasketNeeds().subscribe(needs => {
      for (let need of needs) {
        this.needs[need.id!] = need;
        this.fundingAmounts[need.id!] = 0;
      }
    });
  }

  removeFromBasket(needID: number) {
    this.basketService.removeNeedFromBasket(Number(needID));
    this.ngOnInit();
  }

  async checkout() {
    await this.basketService.checkout(this.fundingAmounts);
    this.ngOnInit();
  }

  validateFundingAmount(needID: number, target: EventTarget | null) {
    // clear on invalid input
    if (!target) {
      this.fundingAmounts[needID] = 0;
      return;
    }

    // parse input
    let amount = Number((target as HTMLInputElement).value);
    let max = this.needs[needID].amount_needed! - this.needs[needID].amount_in_stock!;

    // validate input
    if (amount < 0) {
      amount = 0;
    }
    else if (amount > max) {
      amount = max;
    }
    this.fundingAmounts[needID] = amount;
  }

  getIDs(): number[] {
    return Object.keys(this.needs).map(Number);
  }
}
