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
  }

  getCurrentLanguage() {
    var selectedLanguage = localStorage.getItem(this.SelectedLanguageKEY);
    if (selectedLanguage == undefined) return "en";
    return selectedLanguage;
  }
}
