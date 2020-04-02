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

  openMenu:boolean = false;

  constructor(
    private _translate: TranslateService,
    private _switchLang: SwitchLangService,
    private router: Router,
    private sharedSer: SharedService
  ) {}

  ngOnInit() {

  }

  toggleMenu(){
    this.openMenu = !this.openMenu;
  }
  changeLanguage() {
    let lang = this._switchLang.getCurrentLanguage();
    if(lang == 'ar'){
      this._switchLang.setCurrentLanguage('en');
      this._translate.use('en')
    }else{
      this._switchLang.setCurrentLanguage('ar');
      this._translate.use('ar')
    }

  }

  public open(template: TemplateRef<any>) {
    this.sharedSer.openModal(template,'modal-lg ride-option-modal');
  }
}
