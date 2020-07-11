import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { TranslateModule } from "@ngx-translate/core"
import { RouterModule } from '@angular/router';
import { SearchFormModule } from 'src/app/shared/components/search-form/search-form.module';
import { ModalModule } from 'ngx-bootstrap/modal';
@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    TranslateModule,
    SearchFormModule,
    RouterModule.forChild([{
      path: '',
      component: HomeComponent
    }]),
    ModalModule
  ]
})
export class HomeModule { }
