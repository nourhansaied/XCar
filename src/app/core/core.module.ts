import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { PipeModule } from '../shared/pipes/pipe.module';
import { TranslateModule } from '@ngx-translate/core';
import { LoginComponent } from 'src/docs/driver/login/login.component';
import { SocialMediaComponent } from './components/social-media/social-media.component';

@NgModule({
  declarations: [HeaderComponent, FooterComponent, LoginComponent, SocialMediaComponent],
  imports: [CommonModule,  ReactiveFormsModule, RouterModule, SharedModule, TranslateModule, PipeModule],
  exports: [HeaderComponent, FooterComponent]
})
export class CoreModule {}
