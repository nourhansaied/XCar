import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'victoria-about-us',
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.scss']
})
export class AboutUsComponent implements OnInit {
  @Input() theme;
  @Input() title;
  @Input() description;
  constructor() { }

  ngOnInit() {
  }

}
