import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NeedsService } from 'src/app/services';
import { BasketService } from 'src/app/services/basket.service';
import { NotificationsService } from 'src/app/services/notifications.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css'],
})
export class ListComponent implements OnInit {
  needs?: any[];
  form!: FormGroup;
  constructor(
    private needService: NeedsService,
    private basketService: BasketService,
    private notificationsService: NotificationsService,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit() {
    this.needService.searchUnfulfilled('')
      .pipe(first()).subscribe(needs => this.needs = needs);
    this.form = this.formBuilder.group({
      search_term: [''],
    });
  }

  addToBasket(id: string) {
    this.basketService.addNeedToBasket(Number(id));
  }

  isInBasket(id: string) {
    let need = this.basketService.getNeedFromBasket(Number(id));
    return need != null;
  }

  subscribe(id: string) {
    this.notificationsService.subscribeToNeed(Number(id));
  }

  searchNeeds() {
    this.needService.searchUnfulfilled(this.form.controls['search_term'].value)
      .pipe(first()).subscribe(needs => this.needs = needs);
  }
}
