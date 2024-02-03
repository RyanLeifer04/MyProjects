import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { Needs } from 'src/app/models';

@Injectable({
  providedIn: 'root',
})
export class NeedsService {
  private needsSubject: BehaviorSubject<Needs | null>;
  public needs: Observable<Needs | null>;

  constructor(private router: Router, private http: HttpClient) {
    this.needsSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('need')!));
    this.needs = this.needsSubject.asObservable();
  }

  public get needsValue() { return this.needsSubject.value; }

  register(needs: Needs) {
    return this.http.post(`/cupboard/need`, needs);
  }

  getAll() {
    return this.http.get<Needs[]>(`/cupboard/need`);
  }

  searchUnfulfilled(searchTerm: string) {
    return this.http.get<Needs[]>(`/cupboard/need/unfulfilled/search?name=${searchTerm}`);
  }

  getById(id: string) {
    return this.http.get<Needs>(`/cupboard/need/${id}`);
  }

  update(id: number, params: any) 
  {
    return this.http.put(`/cupboard/need/${id}`, params)
      .pipe(map(x => {
        // update stored user if the logged in user updated their own record
        if (id == this.needsValue?.id) {
          // update local storage
          const needs = { ...this.needsValue, ...params };
          localStorage.setItem('needs', JSON.stringify(needs));

          // publish updated user to subscribers
          this.needsSubject.next(needs);
        }
        return x;
      }));
  }

  delete(id: number) {
    return this.http.delete(`/cupboard/need/${id}`)
  }
}
