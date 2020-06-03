import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CarComponent } from "./car/car.component";
import { CarRoutingModule } from "./car.routing";

@NgModule({
  declarations: [CarComponent],
  imports: [CommonModule, CarRoutingModule],
})
export class CarModule {}
