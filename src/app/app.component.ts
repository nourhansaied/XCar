import { Component } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { SwitchLangService } from "./_shared-components/services/switch-lang.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  title = "VictoriaWebApp";
  arabicLang: boolean = false;

  constructor(
    private _translate: TranslateService,
    private _switchLang: SwitchLangService
  ) {
    this._translate.setDefaultLang("en");
    this._translate.use(this._switchLang.getCurrentLanguage());

    if (this._switchLang.getCurrentLanguage() == "ar") {
      this.arabicLang = true;
    } else {
      this.arabicLang = false;
    }
  }

  ngOnInit() {
    this._translate.setDefaultLang("en");
  }
}
