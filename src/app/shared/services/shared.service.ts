import { Injectable, TemplateRef } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { BsModalService, BsModalRef } from "ngx-bootstrap/modal";

@Injectable({
  providedIn: "root"
})
export class SharedService {
  rideOptions: string;
  private rideSelection: BehaviorSubject<string> = new BehaviorSubject<
    string
  >(this.rideOptions);
  modalRef: BsModalRef;
  constructor(private modalService: BsModalService) {}

  setRideSelection(options: string) {
    this.rideSelection.next(options);
  }
  getRideSelection(): BehaviorSubject<string> {
    return this.rideSelection;
  }

  public openModal(template: TemplateRef<any>, classes) {
    this.modalRef = this.modalService.show(template, {
      class: `${classes}`
    });
  }

  public closeModal() {
    this.modalRef.hide();
  }
}
