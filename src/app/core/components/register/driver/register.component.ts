import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'victoria-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterDriverComponent implements OnInit {
  registerDriverForm: FormGroup;
  showNextStep: boolean;
  constructor(private fb: FormBuilder) { 
    this.showNextStep = false;
  }

  ngOnInit() {

    this.registerDriverForm = this.fb.group({
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
    console.warn(this.registerDriverForm.value);
  }

  nextStep(){
    this.showNextStep = true;
  }
  prevStep(){
    this.showNextStep = false;
  }

}
