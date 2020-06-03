import { Component, OnInit, Input } from "@angular/core";

@Component({
  selector: "victoria-car",
  templateUrl: "./car.component.html",
  styleUrls: ["./car.component.scss"],
})
export class CarComponent implements OnInit {
  active: number = 1;
  steps: string[] = ["1", "2", "3"];
  constructor() {}

  ngOnInit() {}
}
