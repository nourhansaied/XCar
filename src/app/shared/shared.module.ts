import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedService } from './services/shared.service';
import { ModalComponent } from './components/modal/modal.component';

@NgModule({
  declarations: [ModalComponent],
  imports: [
    CommonModule
  ],
  providers:[SharedService],
  exports: [ModalComponent]
})
export class SharedModule { }
