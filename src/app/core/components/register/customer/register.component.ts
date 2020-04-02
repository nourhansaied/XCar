import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'victoria-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterCustomerComponent implements OnInit {
  registerCustomerForm: FormGroup;
  showNextStep: boolean;
  constructor(private fb: FormBuilder) { 
    this.showNextStep = false;
  }

  ngOnInit() {

    this.registerCustomerForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      nationalID: [''],
      email: [''],
      password: [''],
      confirmPassword: [''],
      phoneNumber: [''],
      gender: [''],
      status: [''],
      loginStatus: [''],
      service: [''], 
      dirverStatus: [''],
      profilePic: ['']
    });

  }

  onSubmit() {
    // TODO: Use EventEmitter with form value
    console.warn(this.registerCustomerForm.value);
  }

  nextStep(){
    this.showNextStep = true;
  }
  prevStep(){
    this.showNextStep = false;
  }

}
