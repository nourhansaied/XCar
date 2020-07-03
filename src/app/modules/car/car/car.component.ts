import { Component, OnInit, Input } from "@angular/core";
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { baseURL, Endpoint } from 'src/app/enums';
import { ApiService } from 'src/app/shared/services/api.service';

@Component({
  selector: "xcar-car",
  templateUrl: "./car.component.html",
  styleUrls: ["./car.component.scss"],
})
export class CarComponent implements OnInit {
  active: number = 1;

  steps: string[] = ["1", "2", "3"];
  constructor(

  ) {

  }

  ngOnInit() { }

}
