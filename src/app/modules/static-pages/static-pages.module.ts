import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AboutUsComponent } from './about-us/about-us.component';
import { HelpComponent } from './help/help.component';
import { NewsComponent } from './news/news.component';
import { PoliciesComponent } from './policies/policies.component';
import { ContactUsComponent } from './contact-us/contact-us.component';

@NgModule({
  declarations: [AboutUsComponent, HelpComponent, NewsComponent, PoliciesComponent, ContactUsComponent],
  imports: [
    CommonModule
  ],
  exports:[AboutUsComponent,HelpComponent, NewsComponent, PoliciesComponent, ContactUsComponent]
})
export class StaticPagesModule { }
