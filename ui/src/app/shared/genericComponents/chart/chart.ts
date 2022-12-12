import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ModalController, PopoverController } from "@ionic/angular";
import { TranslateService } from "@ngx-translate/core";
import { DefaultTypes } from "../../service/defaulttypes";
import { Edge, Service } from "../../shared";
import { ChartOptionsPopoverComponent } from "./chartoptions/popover/popover.component";

@Component({
  selector: 'oe-chart',
  templateUrl: './chart.html',
})
export class ChartComponent implements OnInit {

  public edge: Edge | null = null;
  @Input() public title: string = '';
  @Input() public showPhases: boolean;
  @Input() public showTotal: boolean;
  @Output() public setShowPhases: EventEmitter<boolean> = new EventEmitter();
  @Output() public setShowTotal: EventEmitter<boolean> = new EventEmitter();
  @Input() public isPopoverNeeded: boolean = false;

  // Manually trigger ChangeDetection through Inputchange
  @Input() private period: DefaultTypes.PeriodString;

  constructor(
    protected service: Service,
    private route: ActivatedRoute,
    public popoverCtrl: PopoverController,
    protected translate: TranslateService,
    protected modalCtr: ModalController,
    private ref: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then(edge => {
      this.edge = edge;
    })
  }

  ngOnChanges() {
    this.checkIfPopoverNeeded()
  }

  private checkIfPopoverNeeded() {
    if (this.service.periodString == DefaultTypes.PeriodString.MONTH || (this.service.periodString == DefaultTypes.PeriodString.YEAR)) {
      this.isPopoverNeeded = false;
    } else {
      this.isPopoverNeeded = true;
    }
  }

  async presentPopover(ev: any) {
    const popover = await this.popoverCtrl.create({
      component: ChartOptionsPopoverComponent,
      event: ev,
      componentProps: {
        showPhases: this.showPhases,
        showTotal: this.showTotal
      },
    });

    await popover.present();
    popover.onDidDismiss().then((data) => {
      this.showPhases = data.role == 'Phases' ? data.data : this.showPhases;
      this.showTotal = data.role == 'Total' ? data.data : this.showTotal;
      this.setShowPhases.emit(this.showPhases)
      this.setShowTotal.emit(this.showTotal)
    });
    await popover.present();
  }
}