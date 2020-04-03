import { Component, OnInit } from "@angular/core";
import { Constant } from "../../../shared/constant";
import { RideOptions } from "src/app/shared/models/ride-options";
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  rideOptions = [];
  invotionTitle:string;
  inovationDescription:string;
  aboutTitle:string;
  aboutDescription:string

  constructor(private _translate : TranslateService) {
    this._translate.get('about-us-home').subscribe(res => {
      this.aboutTitle = res;
    })
    this._translate.get('about-us-desc').subscribe(res => {
      this.aboutDescription = res;
    })
    this._translate.get('victoria-inovition-title').subscribe(res => {
      this.invotionTitle = res;
    })
    this._translate.get('victoria-inovation-desc').subscribe(res => {
      this.inovationDescription = res;
    })

  }

  ngOnInit() {
    this.rideOptions = Constant.rideOptions;
  }
}
