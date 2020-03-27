import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AboutUsComponent } from './about-us/about-us.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [AboutUsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{
      path: "", component: AboutUsComponent
    }])
  ],
  exports: [AboutUsComponent]
})
export class AboutUsModule { }
