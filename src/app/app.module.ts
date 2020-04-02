import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppRoutingModule } from "./app-routing.module";
import { TooltipModule } from "ngx-bootstrap/tooltip";

import { AppComponent } from "./app.component";
import { CoreModule } from "./core/core.module";
import { HomeModule } from "./modules/home/home.module";
import { SharedModule } from "./shared/shared.module";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { HttpClientModule, HttpClient } from "@angular/common/http";
import { PipeModule } from './shared/pipes/pipe.module';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ModalComponent } from './shared/components/modal/modal.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    TooltipModule,
    CoreModule,
    SharedModule,
    HomeModule,
    PipeModule,
    ModalModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [ModalComponent]
})
export class AppModule {}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
