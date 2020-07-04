import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { baseURL, Endpoint } from 'src/app/enums';
import { ApiService } from 'src/app/shared/services/api.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'victoria-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  basURL = baseURL.baseURL
  constructor(private _ApiService: ApiService,
    private router: Router,
    private auth: AuthService) {
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

          if (res) {
            this.auth.userAuth.subscribe(auth => {

              if (auth && auth.isAuthenticated) {

              } else {
                console.log(auth)
                this.auth.setUserAfterLogin(res);
                window.localStorage.setItem('token', res['token'])

              }

            })
            // this.router.navigate(['/me'])
          }
        },
        err => {
          console.log(err)
        }
      );
    }



  }

}
