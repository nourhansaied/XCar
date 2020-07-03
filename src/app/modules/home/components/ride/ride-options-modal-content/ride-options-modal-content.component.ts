import { Component, OnInit, Input } from '@angular/core';
import { SharedService } from 'src/app/shared/services/shared.service';

@Component({
  selector: 'victoria-ride-options-modal-content',
  templateUrl: './ride-options-modal-content.component.html',
  styleUrls: ['./ride-options-modal-content.component.scss']
})
export class RideOptionsModalContentComponent implements OnInit {

  selectedRide: string;
  rideOptions: object;
  @Input() title: string;

  constructor(private sharedSer: SharedService) { }
  ngOnInit() {
    this.rideOptions = {};
    this.sharedSer
      .getRideSelection()
      .subscribe(option => (this.selectedRide = option));
  }

  public updateRideSelection(rideOption) {
    this.sharedSer.setRideSelection(rideOption);
  }

}
