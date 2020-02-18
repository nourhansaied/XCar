import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppRoutingModule } from "./app-routing.module";
import { TranslateModule } from "@ngx-translate/core";
import { TooltipModule } from "ngx-bootstrap/tooltip";

import { AppComponent } from "./app.component";
import { SharedModule } from './core/shared.module';
import { HomeModule } from './modules/home/home.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TranslateModule.forRoot(),
    TooltipModule,
    SharedModule,
    HomeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
