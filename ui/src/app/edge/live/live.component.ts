import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataService } from 'src/app/shared/genericComponents/shared/dataservice';
import { LiveDataService } from 'src/app/shared/genericComponents/shared/livedataservice';
import { SubscribeEdgesRequest } from 'src/app/shared/jsonrpc/request/subscribeEdgesRequest';
import { Edge, EdgeConfig, Service, Utils, Widgets } from 'src/app/shared/shared';
import { AdvertWidgets } from 'src/app/shared/type/widget';
import { environment } from 'src/environments';

@Component({
  selector: 'live',
  templateUrl: './live.component.html',
  providers: [
    {
      provide: DataService,
      useClass: LiveDataService
    }
  ]
})
export class LiveComponent {

  public edge: Edge = null
  public config: EdgeConfig = null;
  public widgets: Widgets = null;
  public advertWidgets: AdvertWidgets = null;

  constructor(
    private route: ActivatedRoute,
    public service: Service,
    protected utils: Utils,
  ) {
  }

  ionViewWillEnter() {
    this.service.setCurrentComponent('', this.route).then(edge => {

      if (environment.backend == 'OpenEMS Backend' && edge.isOnline) {
        this.service.websocket.sendRequest(new SubscribeEdgesRequest({ edges: [edge.id] }))
          .catch(error => console.warn(error))
      }
      this.edge = edge;
    });
    this.service.getConfig().then(config => {
      this.config = config;
      this.widgets = config.widgets;
      this.advertWidgets = config.advertWidgets;
    })
  }
}
