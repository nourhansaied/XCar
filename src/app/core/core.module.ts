import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ModalComponent } from '../shared/components/modal/modal.component';
import { SharedModule } from '../shared/shared.module';
import { StaticPagesModule } from '../modules/static-pages/static-pages.module';

@NgModule({
  declarations: [HeaderComponent, FooterComponent],
  imports: [CommonModule,  ReactiveFormsModule, RouterModule, SharedModule,StaticPagesModule],
  exports: [HeaderComponent, FooterComponent]
})
export class CoreModule {}
