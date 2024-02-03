import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { AccountService, NeedsService } from 'src/app/services';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css'],
})
export class ListComponent implements OnInit {
  needs?: any[];

  constructor(private needService: NeedsService, private accountService: AccountService) { }

  ngOnInit() {
    this.needService.getAll().pipe(first()).subscribe(needs => this.needs = needs);
  }

  deleteNeed(id: string) {
    const need = this.needs!.find(x => x.id === id);
    need.isDeleting = true;
    this.needService.delete(Number(id)).pipe(first()).subscribe(() => {
      this.needs = this.needs!.filter(x => x.id !== id)
    });
  }

}
