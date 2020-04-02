import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterRoutingModule } from './register-routing.module';
import { RegisterCustomerComponent } from './customer/register.component';
import { RegisterDriverComponent } from './driver/register.component';
import { SocialMediaModule } from '../social-media/social-media.module';

@NgModule({
  declarations: [RegisterCustomerComponent, RegisterDriverComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RegisterRoutingModule,
    SocialMediaModule
  ]
})
export class RegisterModule { }
