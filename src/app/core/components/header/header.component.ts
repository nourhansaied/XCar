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
    this._translate.get("login").subscribe(res => (this.modalTitle = res));
    this.subscriptionData = {};
    this.subscriptionData["about-us"] = this._translate
      .get("about-us")
      .subscribe(res => {
        this.aboutUsTitle = res;
      });
    this.subscriptionData["help"] = this._translate
      .get("help")
      .subscribe(res => {
        this.helpTitle = res;
      });
    this.subscriptionData["news"] = this._translate
      .get("news")
      .subscribe(res => {
        this.newsTitle = res;
      });
    this.subscriptionData["polices"] = this._translate
      .get("polices")
      .subscribe(res => {
        this.apoliciesTitle = res;
      });
    this.subscriptionData["contactUs"] = this._translate
      .get("contactUs")
      .subscribe(res => {
        this.contactUsTitle = res;
      });
  }

  ngOnInit() {}

  toggleMenu() {
    this.openMenu = !this.openMenu;
  }
  changeLanguage() {
    let lang = this._switchLang.getCurrentLanguage();
    if (lang == "ar") {
      this._switchLang.setCurrentLanguage("en");
      this._translate.use("en");
    } else {
      this._switchLang.setCurrentLanguage("ar");
      this._translate.use("ar");
    }
  }

  public open(template: TemplateRef<any>, classes?: string) {
    this.sharedSer.openModal(template, `${classes}`);
  }

  
}
