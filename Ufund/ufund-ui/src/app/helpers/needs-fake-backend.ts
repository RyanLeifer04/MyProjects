
import { Injectable } from '@angular/core';
import { HttpRequest, HttpResponse, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { delay, materialize, dematerialize } from 'rxjs/operators';
import { Needs } from 'src/app/models';

const usersKey = 'angular-tutorial';
let needs = [
    { name: 'Cherry', needId: 0, amountNeeded: '15', amountUnit: 'pounds' },
    { name: 'Apple', needId: 1, amountNeeded: '5', amountUnit: 'kilograms' },
    { name: 'Water', needId: 2, amountNeeded: '1', amountUnit: 'gallon' },
]

@Injectable()
export class NeedsFakeBackendInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const { url, method, headers, body } = request;

        return handleRoute();

        function handleRoute() {
            switch (true) {
                case url.endsWith('needs/register') && method === 'POST':
                    return register();
                case url.endsWith('/needs') && method === 'GET':
                    return getNeeds();
                case url.match(/\/needs\/\d+$/) && method === 'GET':
                    return getNeedById();
                case url.match(/\/needs\/\d+$/) && method === 'PUT':
                    return updateNeed();
                case url.match(/\/needs\/\d+$/) && method === 'DELETE':
                    return deleteNeed();
                default:
                    // pass through any requests not handled above
                    return next.handle(request);
            }
        }

        // route functions

        function register() {
            const need = body

            if (needs.find(x => x.name === need.name)) {
                return error('name"' + need.name + '" is already taken');
            }
            need.needId = needs.length ? Math.max(...needs.map(x => x.needId)) + 1 : 1;
            needs.push(need);
            localStorage.setItem(usersKey, JSON.stringify(needs));
            return ok();
        }

        function getNeeds() {
            if (!isLoggedIn()) return unauthorized();
            return ok(needs.map(x => basicDetails(x)));
        }

        function getNeedById() {
            if (!isLoggedIn()) return unauthorized();

            const need = needs.find(x => x.needId === idFromUrl());
            return ok(basicDetails(need));
        }

        function updateNeed() {
            if (!isLoggedIn()) return unauthorized();

            let params = body as Needs;
            let need = needs.find(x => x.needId === idFromUrl()) || {} as Needs;

            // update and save user
            Object.assign(need, params);
            localStorage.setItem(usersKey, JSON.stringify(needs));

            return ok();
        }

        function deleteNeed() {
            if (!isLoggedIn()) return unauthorized();

            needs = needs.filter(x => x.needId !== idFromUrl());
            localStorage.setItem(usersKey, JSON.stringify(needs));
            return ok();
        }

        // helper functions

        function ok(body?: any) {
            return of(new HttpResponse({ status: 200, body }))
                .pipe(delay(500)); // delay observable to simulate server api call
        }

        function error(message: string) {
            return throwError(() => ({ error: { message } }))
                .pipe(materialize(), delay(500), dematerialize()); // call materialize and dematerialize to ensure delay even if an error is thrown (https://github.com/Reactive-Extensions/RxJS/issues/648);
        }

        function unauthorized() {
            return throwError(() => ({ status: 401, error: { message: 'Unauthorized' } }))
                .pipe(materialize(), delay(500), dematerialize());
        }

        function basicDetails(needs: any) {
            const { name, needId, amountNeeded, amountUnit } = needs;
            return { name, needId, amountNeeded, amountUnit };
        }

        function isLoggedIn() {
            return headers.get('Authorization') === 'Bearer fake-jwt-token';
        }

        function idFromUrl() {
            const urlParts = url.split('/');
            return parseInt(urlParts[urlParts.length - 1]);
        }
    }
}

export const needsfakeBackendProvider = {
    // use fake backend in place of Http service for backend-less development
    provide: HTTP_INTERCEPTORS,
    useClass: NeedsFakeBackendInterceptor,
    multi: true
};
