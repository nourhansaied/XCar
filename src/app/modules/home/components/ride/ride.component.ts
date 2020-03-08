import { Component, OnInit, TemplateRef, Input} from "@angular/core";
import { SharedService } from "src/app/shared/services/shared.service";

@Component({
  selector: "victoria-ride",
  templateUrl: "./ride.component.html",
  styleUrls: ["./ride.component.scss"]
})
export class RideComponent implements OnInit {
  @Input() name: string;
  @Input() title: string;
  @Input() desc: string;
  constructor(
    private sharedSer: SharedService
  ) {}

  ngOnInit() {}

  public open(template: TemplateRef<any>, rideOption) {
    this.sharedSer.openModal(template,'modal-xl ride-option-modal');
    this.sharedSer.setRideSelection(rideOption);
  }

}
