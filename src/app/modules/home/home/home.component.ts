import { Component, OnInit } from "@angular/core";
import { Constant } from "../../../shared/constant";
import { RideOptions } from "src/app/shared/models/ride-options";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  rideOptions = [];
  constructor() {}

  ngOnInit() {
    this.rideOptions = Constant.rideOptions;
  }
}
