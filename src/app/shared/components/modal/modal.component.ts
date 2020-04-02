import { Component,
  OnInit,
  Output,
  EventEmitter,
  ViewChild,
  TemplateRef,
  Input
} from "@angular/core";
import { SharedService } from "../../services/shared.service";

@Component({
  selector: "victoria-modal",
  templateUrl: "./modal.component.html",
  styleUrls: ["./modal.component.scss"]
})
export class ModalComponent implements OnInit {

  @Input() modalTitle: string;

  constructor(private sharedSer: SharedService) {}

  ngOnInit() {  }

  public close() {
    this.sharedSer.closeModal();
  }
}
