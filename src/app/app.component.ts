import { Component, Renderer } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { SwitchLangService } from "./core/services/switch-lang.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  title = "XCar";
  arabicLang: boolean = false;

  constructor(
    private _translate: TranslateService,
    private _switchLang: SwitchLangService,
    private renderer: Renderer,
  ) {
    let targetClass: boolean;
    let targetDir;
    this._translate.use(this._switchLang.getCurrentLanguage());
    if (this._switchLang.getCurrentLanguage() == "ar") {
      this.arabicLang = true;
      targetClass = true;
      targetDir = 'rtl';
    } else {
      this.arabicLang = false;
      targetDir = 'ltr';
      targetClass = false;
    }
    this.renderer.setElementClass(document.body, 'rtl', targetClass);
    this.renderer.setElementAttribute(document.body, 'dir', targetDir);
    this.renderer.setElementAttribute(document.body, 'id', targetDir);
  }

  ngOnInit() {
    let cashedLang = window.localStorage.getItem('SelectedLanguage');
    if (cashedLang) {
      this._translate.setDefaultLang(`${cashedLang}`);
      this._switchLang.setCurrentLanguage(`${cashedLang}`);
    } else {
      this._translate.setDefaultLang("ar");
      this._switchLang.setCurrentLanguage("ar");
    }

  }
}
