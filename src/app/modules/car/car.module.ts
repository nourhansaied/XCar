import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CarComponent } from "./car/car.component";
import { CarRoutingModule } from "./car.routing";
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [CarComponent],
  imports: [CommonModule, CarRoutingModule, ReactiveFormsModule],
})
export class CarModule { }
