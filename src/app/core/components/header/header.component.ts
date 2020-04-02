import { Component, OnInit, TemplateRef } from "@angular/core";
import { SwitchLangService } from "../../services/switch-lang.service";
import { TranslateService } from "@ngx-translate/core";
import { Router } from '@angular/router';
import { SharedService } from 'src/app/shared/services/shared.service';

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent implements OnInit {
  isArabicLanguage: boolean = false;

  constructor(
    private _translate: TranslateService,
    private _switchLang: SwitchLangService,
    private router: Router,
    private sharedSer: SharedService
  ) {}

  ngOnInit() {
    if (this._translate.currentLang == "ar") this.isArabicLanguage = true;
    else this.isArabicLanguage = false;
  }

  changeLanguage(event, lang) {
    if (lang != this._translate.currentLang) {
      if (this._translate.currentLang == "en") {
        this._switchLang.setCurrentLanguage("ar");
      } else {
        this._switchLang.setCurrentLanguage("en");
      }
      window.location.href = this.router.url;
    }
  }

  public open(template: TemplateRef<any>) {
    this.sharedSer.openModal(template,'modal-lg ride-option-modal');
  }
}
