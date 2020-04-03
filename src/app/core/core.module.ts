import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { PipeModule } from '../shared/pipes/pipe.module';
import { TranslateModule } from '@ngx-translate/core';
import { LoginComponent } from './components/login/login.component';
import { SocialMediaModule } from './components/social-media/social-media.module';

@NgModule({
  declarations: [HeaderComponent, FooterComponent, LoginComponent],
  imports: [CommonModule,  ReactiveFormsModule, RouterModule, SharedModule, TranslateModule, PipeModule, SocialMediaModule],
  exports: [HeaderComponent, FooterComponent]
})
export class CoreModule {}
