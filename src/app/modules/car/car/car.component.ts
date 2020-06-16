import { Component, OnInit, Input } from "@angular/core";
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { baseURL, Endpoint } from 'src/app/shared/enums';
import { ApiService } from 'src/app/shared/services/api.service';

@Component({
  selector: "xcar-car",
  templateUrl: "./car.component.html",
  styleUrls: ["./car.component.scss"],
})
export class CarComponent implements OnInit {
  active: number = 1;
  loginForm: FormGroup;
  basURL = baseURL.baseURL
  steps: string[] = ["1", "2", "3"];
  constructor(
    private _ApiService: ApiService
  ) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() { }
  onSubmit(form: FormGroup) {

    if (form.valid) {
      const body = {
        email: form.value.email,
        password: form.value.password
      }
      this._ApiService.post({
        endpoint: this.basURL + Endpoint.login,
        body
      }).subscribe(
        res => {
          console.log(res)
        },
        err => {
          console.log(err)
        }
      );
    }



  }
}
