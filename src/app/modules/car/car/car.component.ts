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
  steps: any = [{
    id: 1,
    title: 'البيانات الاساسية'
  },
  {
    id: 2,
    title: 'صور السيارة او الموتوسكل'
  },
  {
    id: 3,
    title: ' بيانات المالك'
  }
  ];
  newRequest: FormGroup;
  basURL = baseURL.baseURL
  constructor() {
    this.newRequest = new FormGroup({
      adTypeId: new FormControl('', [Validators.required]),
      modelId: new FormControl('', [Validators.required]),
      shapeTypeId: new FormControl('', [Validators.required]),
      gearboxId: new FormControl('', [Validators.required]),
      cc: new FormControl('', [Validators.required]),
      km: new FormControl('', [Validators.required]),
      price: new FormControl('', [Validators.required]),
      year: new FormControl('', [Validators.required]),
      governorateId: new FormControl('', [Validators.required]),
      notes: new FormControl('', [Validators.required]),
      region: new FormControl('', [Validators.required]),
      optionsIds: new FormControl('', [Validators.required])

    });
  }
  // AdTypeId - ModelId - ShapeTypeId - GearboxId - GovernorateId - Cc -Km - Price - Year - Notes - Region - Mobile - OptionsIds [Array]

  ngOnInit() { }

}
