import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
@Injectable({
  providedIn: "root"
})
export class SwitchLangService {

  SelectedLanguageKEY = 'SelectedLanguage';
  constructor(public router: Router) {}

  setCurrentLanguage(language) {
    localStorage.setItem(this.SelectedLanguageKEY, language);
    let targetClass: boolean;
    let targetDir;
    
    if (language == "ar") {
      targetClass = true;
        targetDir = 'rtl';
    } else {
      targetDir = 'ltr';
      targetClass = false;
    }
    document.getElementsByTagName('body')[0].setAttribute('dir',targetDir)
    document.getElementsByTagName('body')[0].setAttribute('id',targetDir)
  }


  public getCurrentLanguage():string {
    let selectedLanguage = window.localStorage.getItem(this.SelectedLanguageKEY);
    if(!selectedLanguage) return "en";
    return selectedLanguage;
  }
}
