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
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from "@angular/common/http";
import { PipeModule } from './shared/pipes/pipe.module';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ModalComponent } from './shared/components/modal/modal.component';
import { FormsModule } from '@angular/forms';
import { CustomHttpInterceptor } from './shared/services/http-interceptor';
@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    FormsModule,
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
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomHttpInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [ModalComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
