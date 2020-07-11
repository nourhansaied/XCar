import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { Router } from '@angular/router';
import { ModalDirective, BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { SharedService } from 'src/app/shared/services/shared.service';

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  @ViewChild(ModalDirective) modal: ModalDirective;
  modalRef: BsModalRef;
  constructor(private modalService: BsModalService, private _router: Router, private _SharedService: SharedService) {
  }

  AddAds() {
    this._router.navigate(['/car'])
  }
  public open(template: TemplateRef<any>, classes?: string) {
    this._SharedService.openModal(template, `${classes}`);
    this.modalRef = this.modalService.show(
      template,
      Object.assign({}, { class: 'gray modal-lg' })
    );
  }
  ngOnInit() {
  }
}
