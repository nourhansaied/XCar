import { Component, OnInit, TemplateRef } from "@angular/core";
import { SwitchLangService } from "../../services/switch-lang.service";
import { TranslateService } from "@ngx-translate/core";
import { Router } from "@angular/router";
import { SharedService } from "src/app/shared/services/shared.service";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent implements OnInit {
  isArabicLanguage: boolean = false;
  modalTitle: string;
  openMenu: boolean = false;
  subscriptionData: object;
  aboutUsTitle: string;
  contactUsTitle: string;
  apoliciesTitle: string;
  newsTitle: string;
  helpTitle: string;
  constructor(
    private _translate: TranslateService,
    private _switchLang: SwitchLangService,
    private router: Router,
    private sharedSer: SharedService
  ) {

  }

  ngOnInit() {
    console.log('asdasd')
  }
  changeLanguage() {
    console.log('asdasd')
    let lang = this._switchLang.getCurrentLanguage();
    console.log(lang)
    if (lang == "ar") {
      this._switchLang.setCurrentLanguage("en");
      this._translate.use("en");
    } else {
      this._switchLang.setCurrentLanguage("ar");
      this._translate.use("ar");
    }
  }

  // public open(template: TemplateRef<any>, classes?: string) {
  //   this.sharedSer.openModal(template, `${classes}`);
  // }


}
