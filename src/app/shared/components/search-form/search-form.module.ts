import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchFormComponent } from './search-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [SearchFormComponent],
  imports: [
    CommonModule,
    TranslateModule,
    ReactiveFormsModule,
    NgSelectModule,
    ModalModule.forRoot()
  ],
  exports: [SearchFormComponent]
})
export class SearchFormModule { }
