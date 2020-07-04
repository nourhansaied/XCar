import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CarComponent } from "./car/car.component";
import { CarRoutingModule } from "./car.routing";
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'src/app/shared/shared.module';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
  declarations: [CarComponent],
  imports: [
    CommonModule,
    NgSelectModule,
    CarRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    ModalModule.forRoot()
  ],
})
export class CarModule { }
