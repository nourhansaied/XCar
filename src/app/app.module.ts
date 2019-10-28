import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppRoutingModule } from "./app-routing.module";
import { TranslateModule } from "@ngx-translate/core";
import { TooltipModule } from "ngx-bootstrap/tooltip";
import { SharedModule } from "./_shared-components/shared.module";
import { HomeModule } from "./home-module/home.module";

import { AppComponent } from "./app.component";

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
