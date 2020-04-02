import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterCustomerComponent } from './customer/register.component';
import { RegisterDriverComponent } from './driver/register.component';

const routes: Routes = [
  { path: '', component: RegisterCustomerComponent },
  { path: 'customer', component: RegisterCustomerComponent },
  { path: 'driver', component: RegisterDriverComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegisterRoutingModule { }
