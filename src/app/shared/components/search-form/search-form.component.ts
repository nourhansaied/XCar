import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { baseURL, Endpoint } from 'src/app/enums';

@Component({
  selector: 'XCar-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.scss']
})
export class SearchFormComponent implements OnInit {
  search: FormGroup;
  brands: any = [];
  models: any = [];
  governments: any = [];
  basUrl = baseURL.baseURL;
  constructor(private _apiService: ApiService, ) {
    this.search = new FormGroup({
      adTypeId: new FormControl(null, [Validators.required]), // done EP
      carBrand: new FormControl(null, [Validators.required]), // done EP
      modelId: new FormControl(null, [Validators.required]), // done EP ... get models
      governorateId: new FormControl(null, [Validators.required]),
    });

  }

  ngOnInit() {
    this._apiService.get({
      endpoint: this.basUrl + Endpoint.carBrands
    }).subscribe(res => this.brands = res);
    this._apiService.get({
      endpoint: baseURL.baseURL + Endpoint.government
    }).subscribe(governmentsList => {
      if (governmentsList) {
        this.governments = governmentsList;
        this.search.get('governorateId').setValue(1)
      }

    });
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
  onSubmit(form) {

  }
}
