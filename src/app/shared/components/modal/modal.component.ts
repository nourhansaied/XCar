import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  ViewChild,
  TemplateRef,
  Input
} from "@angular/core";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { SharedService } from "../../services/shared.service";
import { Constant } from "../../constant";

@Component({
  selector: "victoria-modal",
  templateUrl: "./modal.component.html",
  styleUrls: ["./modal.component.scss"]
})
export class ModalComponent implements OnInit {
  selectedRide: string;
  rideOptions: object;
  @Input() title: string;
  
  constructor(private sharedSer: SharedService) {}

  ngOnInit() {
    this.rideOptions = Constant.rideOptions;
    this.sharedSer
      .getRideSelection()
      .subscribe(option => (this.selectedRide = option));
  }

  public updateRideSelection(rideOption) {
    this.sharedSer.setRideSelection(rideOption);
  }

  public close() {
    this.sharedSer.closeModal();
  }
}
