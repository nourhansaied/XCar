import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/mergeMap';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';


@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {
    appLanaguage: any;
    constructor(
        private router: Router,
        private authService: AuthService,

    ) {
        this.appLanaguage = 'ar'

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        let authHeaders = {
            'Accept-Language': this.appLanaguage
        }

        if (!req.headers.has('Content-Type')) {
            authHeaders['Content-Type'] = 'application/json'
        }

        if (this.authService.currentUserAuth && this.authService.currentUserAuth.token) {
            authHeaders['Authorization'] = `Bearer ${this.authService.currentUserAuth.token}`;
        }
        let cashedToken = window.localStorage.getItem('token')
        if (cashedToken) {
            authHeaders['Authorization'] = `Bearer ${cashedToken}`;

        }
        let requestHeaders = new HttpHeaders(authHeaders)
        // Clone the request to add the new header.
        const authReq = req.clone({ headers: requestHeaders });
        // send the newly created request
        return next.handle(authReq)
            .catch((error, caught) => {
                //return the error to the method that called it
                return Observable.throw(error);
            }) as any;
    }
}
