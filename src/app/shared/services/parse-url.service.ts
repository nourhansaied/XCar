import { Injectable } from '@angular/core';
import { PRIMARY_OUTLET, Router, UrlSegmentGroup, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ParseUrlService {

  constructor(private router: Router) { }

  getRouteSegments = () => {
    const url = this.router.url;
    const tree: UrlTree = this.router.parseUrl(url);
    const g: UrlSegmentGroup = tree.root.children[PRIMARY_OUTLET];
    if (g) return g.segments;
  }

  getlanguageCode = () => {
    const segs = this.getRouteSegments();
    return segs && segs.length ? segs[0].path : 'en';
  }
}
