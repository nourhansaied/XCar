import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { HomeRoutingModule } from './home-routing.module';
import { RideComponent } from './components/ride/ride.component';
import { ModalModule, BsModalRef } from "ngx-bootstrap/modal";
import { SharedModule } from 'src/app/shared/shared.module';
import { ModalComponent } from 'src/app/shared/components/modal/modal.component';
import { TranslateModule } from "@ngx-translate/core"
import { PipeModule } from 'src/app/shared/pipes/pipe.module';

@NgModule({
  declarations: [HomeComponent, RideComponent, ModalComponent],
  imports: [
    CommonModule,
    SharedModule,
    HomeRoutingModule,
    TranslateModule,
    ModalModule.forRoot(),
    PipeModule
  ]
})
export class HomeModule { }
