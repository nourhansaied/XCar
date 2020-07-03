import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl, FormBuilder } from '@angular/forms';
import { ApiService } from 'src/app/shared/services/api.service';
import { baseURL, Endpoint } from 'src/app/enums';
import { MustMatch } from '../validators/passwordMatch';
import { Router } from '@angular/router';
@Component({
  selector: 'victoria-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  basURL = baseURL.baseURL;
  governments: any;
  id: number;
  constructor(private _ApiService: ApiService, private formBuilder: FormBuilder, private router: Router) {
    this.signupForm = this.formBuilder.group({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required]),
      password: new FormControl('', [Validators.required]),
      confirmPassword: ['', Validators.required],
      mobile: new FormControl(null, [Validators.required]),
      governorateId: new FormControl(null, [Validators.required])
    },
      { validator: MustMatch('password', 'confirmPassword') });
  }


  get f() { return this.signupForm.controls; }



  ngOnInit() {
    this._ApiService.get({
      endpoint: baseURL.baseURL + Endpoint.government
    }).subscribe(governmentsList => {
      this.governments = governmentsList;
      this.signupForm.get('governorateId').setValue(1)
    })
  }

  onSubmit(form: FormGroup) {
    if (form.valid) {
      delete form.value.confirmPassword;
      this._ApiService.post({
        endpoint: this.basURL + Endpoint.signup,
        body: form.value
      }).subscribe(
        res => {
          this.router.navigate(['/login'])
        },
        err => {
          console.log(err)
        }
      );
    }

  }

}
