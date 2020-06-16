import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { pluck, tap, debounceTime } from 'rxjs/operators';
import { Response, URLSearchParams } from '@angular/http';
import { Endpoint } from '../enums';
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
  constructor(private http: HttpClient,
    private _parseUrlService: ParseUrlService) {
    // TODO: subscript for selected language
    // TODO: (menna) remove that to interceptor


    this.appLanaguage = 'en';
    this.httpHeaders = new HttpHeaders()
      .set('Accept-Language', this.appLanaguage);

  }

  /**
    * @name setUrl
    * @memberof QueryService
    * @description set url with the required api
    * @param {string} api
    */
  public setUrl(api: string) {
    // this.flightUrl = flightSearchUrl + api
    // this.url = uri + api;
    // if (api.includes('http://192.168.1.22:18087') || api.includes('http://192.168.1.22:18094') || api.includes('http://192.168.1.22:18080')) {
    //   this.url = api;
    // } else {
    //   this.url = uri + api;
    // }
    this.url = api;
    // console.log(api)
  }
  private updateFetchHeaders(language) {
    const headers = new HttpHeaders({ 'Accept-Language': 'en' })
  }

  private handleFetchError(error) {
    // TODO: handle fetch errors...
    console.log('%c[FETCH]\n', 'background:crimson; color:#fff', error)
  }

  private handlePostError(error) {
    // TODO: handle psot errors...
    console.log('%c[POST]\n', 'background:black; color:#fff', error)
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
    // const queryParams = this.convertObjectToUrlParams(options.queryParams);
    const params = this.getQueryParams(options.queryParams)
    const request = this.http.get(endPoint,
      {
        headers: this.httpHeaders,
        params
      }
    )
    // request.subscribe(null, this.handleFetchError)
    return options.extractData ? request.pipe(pluck('data')) : request
  }

  post(options, header?) {
    let headers = header ? header : ''
    console.log(headers);
    // const headers =
    const request = this.http.post(options.endpoint + this.convertObjectToUrlParams(options.queryParams), options.body, { headers: headers })
    // request.subscribe(null, this.handlePostError)
    return request
  }

  put(options) {
    const headers = new HttpHeaders({ 'Accept-Language': this.appLanaguage })
    const request = this.http.put(options.endpoint + this.convertObjectToUrlParams(options.queryParams), options.body, { headers: headers })
    // request.subscribe(null, this.handlePostError)
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

  // public deleteWithQueryParams(params: any = {}): Observable<any> {
  //   return this.http.delete(this.url)
  //     .map((res: Response) => res)
  //     .catch((error: any) => Observable.throw(error || 'Server error'));
  // }

  // public getLocal(path: string): Observable<any> {
  //   return this.http
  //     .get(`./assets/${path}`)
  //     .map((res: Response) => res)
  //     .catch((error: any) => Observable.throw(error || 'Server error'));
  // }



  public getQueryParams(queryParams: any) {
    let params = new HttpParams();
    for (let key in queryParams) {
      params = params.append(key, queryParams[key])
    }
    return params;
  }

}
