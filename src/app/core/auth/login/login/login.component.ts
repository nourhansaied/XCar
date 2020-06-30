import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { baseURL, Endpoint } from 'src/app/shared/enums';
import { ApiService } from 'src/app/shared/services/api.service';

@Component({
  selector: 'victoria-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  basURL = baseURL.baseURL
  constructor(private _ApiService: ApiService) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() {
  }
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
