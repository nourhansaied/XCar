import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { SharedModule } from "../shared/shared.module";
import { LoginComponent } from "./components/login/login.component";
import { SocialMediaModule } from "./components/social-media/social-media.module";
import { StaticPagesModule } from "../modules/static-pages/static-pages.module";
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [HeaderComponent, FooterComponent, LoginComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    SharedModule,
    SocialMediaModule,
    StaticPagesModule,
    BrowserAnimationsModule,
    BsDropdownModule.forRoot()
  ],
  exports: [HeaderComponent, FooterComponent]
})
export class CoreModule {}
