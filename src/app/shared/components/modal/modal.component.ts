import { Component,
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

  @Input() modalTitle: string;
  @Input() staticPages: boolean;

  constructor(private sharedSer: SharedService) {}

  ngOnInit() {  }

  public close() {
    this.sharedSer.closeModal();
  }
}
