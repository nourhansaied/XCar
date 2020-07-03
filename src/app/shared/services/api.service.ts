import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { pluck, tap, debounceTime } from 'rxjs/operators';
import { Response, URLSearchParams } from '@angular/http';
import { Endpoint } from '../../enums';
import { ParseUrlService } from './parse-url.service';
import { Observable } from 'rxjs/Rx';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private url: string;
  private httpHeaders: HttpHeaders = new HttpHeaders();
  appLanaguage;
  constructor(private http: HttpClient) {
    this.appLanaguage = 'en';
    this.httpHeaders = new HttpHeaders()
      .set('Accept-Language', this.appLanaguage);
  }

  public setUrl(api: string) {
    this.url = api;
  }

  convertObjectToUrlParams(object: Object) {
    if (!object) return '';
    const params = new URLSearchParams()
    for (const key in object) {
      params.set(key, object[key])
    }
    return '?' + params
  }

  resolveUrlParams(url: Endpoint, params: Object) {
    if (!params) return url;
    const resolved = url.split('/').map((component) => {
      if (component[0] === ':') {
        const key = component.slice(1)
        if (params.hasOwnProperty(key)) return params[key];
        throw `Param ':${key}' value was not found`
      } else {
        return component
      }
    })
    return resolved.join('/')
  }

  get(options) {
    this.httpHeaders['Accept-Language'] = new HttpHeaders()
      .set('Accept-Language', 'en');
    let endPoint = this.resolveUrlParams(options.endpoint, options.params)
    const params = this.getQueryParams(options.queryParams)
    const request = this.http.get(endPoint,
      {
        headers: this.httpHeaders,
        params
      }
    )
    return options.extractData ? request.pipe(pluck('data')) : request
  }

  post(options, header?) {
    let headers = header ? header : ''
    const request = this.http.post(options.endpoint + this.convertObjectToUrlParams(options.queryParams), options.body, { headers: headers })
    return request
  }

  put(options) {
    const headers = new HttpHeaders({ 'Accept-Language': this.appLanaguage })
    const request = this.http.put(options.endpoint + this.convertObjectToUrlParams(options.queryParams), options.body, { headers: headers })
    return request
  }

  public delete(params: any = {}): Observable<any> {
    const head = new HttpHeaders({
      'Authorization': `Bearer ${params}`
    });
    return this.http.delete(this.url, {
      headers: head
    }).map((res: Response) => res)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }
  public getQueryParams(queryParams: any) {
    let params = new HttpParams();
    for (let key in queryParams) {
      params = params.append(key, queryParams[key])
    }
    return params;
  }

}
