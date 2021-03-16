import { ActivatedRoute } from '@angular/router';
import { FixActivePowerModalComponent } from './modal/modal.component';
import { Component, Input } from '@angular/core';
import { Edge, EdgeConfig, Service } from '../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { compileComponentFromMetadata } from '@angular/compiler';

@Component({
  selector: FixActivePowerComponent.SELECTOR,
  templateUrl: './fixactivepower.component.html'
})
export class FixActivePowerComponent {

  @Input() private componentId: string | null = null;


  private static readonly SELECTOR = "fixactivepower";

  private edge: Edge = null;
  public component: EdgeConfig.Component | null = null;
  public chargeState: string;
  public chargeStateValue: number;
  state: any;

  constructor(
    private route: ActivatedRoute,
    public modalCtrl: ModalController,
    public service: Service,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then(edge => {
      this.edge = edge;
      this.service.getConfig().then(config => {
        this.component = config.getComponent(this.componentId);
        if (this.component.properties.power >= 0) {
          this.chargeState = 'dischargePower';
          this.chargeStateValue = this.component.properties.power
        } else if (this.component.properties.power < 0) {
          this.chargeState = 'chargePower';
          this.chargeStateValue = this.component.properties.power * -1;
        }
        if (this.component.properties.mode == 'MANUAL_ON') {
          this.state = 'on'
        } else if (this.component.properties.mode == 'MANUAL_OFF') {
          this.state = 'off'
        } else {
          this.state = ' <ion-icon name="help-outline"></ion-icon>'
        }
      })
    })
  }

  async presentModal() {
    const modal = await this.modalCtrl.create({
      component: FixActivePowerModalComponent,
      componentProps: {
        component: this.component,
        edge: this.edge,
      }
    });
    return await modal.present();
  }
}
