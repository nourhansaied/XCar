import { Component, OnInit, Input, TemplateRef, ViewChild } from "@angular/core";
import { FormGroup, FormControl, Validators, PatternValidator } from '@angular/forms';
import { baseURL, Endpoint } from 'src/app/enums';
import { ApiService } from 'src/app/shared/services/api.service';
import { ConditionalExpr } from '@angular/compiler';
import { SharedService } from 'src/app/shared/services/shared.service';
import { ModalDirective } from 'ngx-bootstrap/modal';

@Component({
  selector: "xcar-car",
  templateUrl: "./car.component.html",
  styleUrls: ["./car.component.scss"],
})
export class CarComponent implements OnInit {
  @ViewChild(ModalDirective) modal: ModalDirective;
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
  basUrl = baseURL.baseURL;
  models: any = [];
  brands: any = [];
  shapTypes: any = [];
  gearboxTypes: any = [];
  governments: any = [];
  showPopup: boolean = false;
  currentStep: number = 1;
  carColor: any = [{
    id: 1,
    name: 'red'
  },
  {
    id: 2,
    name: 'green'
  }, {
    id: 3,
    name: 'blue'
  }, {
    id: 4,
    name: 'yellow'
  }, {
    id: 5,
    name: 'black'
  }, {
    id: 6,
    name: 'white'
  }, {
    id: 7,
    name: 'gray'
  }]
  constructor(private _apiService: ApiService, private _SharedService: SharedService) {
    this.newRequest = new FormGroup({
      adTypeId: new FormControl(null, [Validators.required]), // done EP
      carBrand: new FormControl(null, [Validators.required]), // done EP
      modelId: new FormControl(null, [Validators.required]), // done EP ... get models
      shapeTypeId: new FormControl(null, [Validators.required]), // done EP 
      gearboxId: new FormControl(null, [Validators.required]), //done EP
      carColor: new FormControl(null, [Validators.required]), //done ... will be from EP
      cc: new FormControl(null, [Validators.required]), // input
      km: new FormControl(null, [Validators.required]), // input
      price: new FormControl(null, [Validators.required]),// input
      year: new FormControl(null, [Validators.required]),// input
      governorateId: new FormControl(null, [Validators.required]),
      notes: new FormControl(null, [Validators.required]), // textarea
      region: new FormControl('egypt', [Validators.required]), // not have
      optionsIds: new FormControl([]), // not have
      mobile: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required])

    });

  }
  // AdTypeId - ModelId - ShapeTypeId - GearboxId - GovernorateId - Cc -Km - Price - Year - Notes - Region - Mobile - OptionsIds [Array]

  ngOnInit() {

    this._apiService.get({
      endpoint: this.basUrl + Endpoint.carBrands
    }).subscribe(res => this.brands = res);

    this._apiService.get({
      endpoint: this.basUrl + Endpoint.shapeTypes
    }).subscribe(res => this.shapTypes = res);

    this._apiService.get({
      endpoint: this.basUrl + Endpoint.geerBox
    }).subscribe(res => this.gearboxTypes = res);
    this._apiService.get({
      endpoint: baseURL.baseURL + Endpoint.government
    }).subscribe(governmentsList => {
      this.governments = governmentsList;
      this.newRequest.get('governorateId').setValue(1)
    });

    // this.newRequest.get('adTypeId').setValue(1)
  }

  onSubmit(form: FormGroup) {
    if (form.valid) {
      delete form.value['carBrand'];
      delete form.value['email'];
      form.value['price'] = Number(form.value['price'])
      this._apiService.post({
        endpoint: this.basUrl + Endpoint.newCarAds,
        body: form.value
      }).subscribe(res => {
        if (res) {
          this.modal.show()
        }
      })
    }
  }
  getModels(e) {
    let brandId = e.id;
    this._apiService.get({
      endpoint: this.basUrl + Endpoint.carModels,
      params: { id: brandId }
    }).subscribe(res => {
      if (res && res['models'].length > 0) {
        this.models = res['models']
      }
    });
  }

  nextStep() {
    ++this.currentStep;
    ++this.active;
  }
  prevStep() {
    --this.currentStep;
    --this.active;
  }

  public open(template: TemplateRef<any>, classes?: string) {
    this._SharedService.openModal(template, `${classes}`);
  }
}
