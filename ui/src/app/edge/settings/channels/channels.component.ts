import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChannelAddress, Edge, EdgeConfig, Service, Websocket } from '../../../shared/shared';
import { IGNORE_NATURES } from '../component/shared/shared';
import { SetChannelVaLueRequest } from 'src/app/shared/jsonrpc/request/setChannelValueRequest';

@Component({
  selector: ChannelsComponent.SELECTOR,
  templateUrl: './channels.component.html'
})
export class ChannelsComponent {

  private static readonly SELECTOR = "channels";

  public edge: Edge | null = null;
  public config: EdgeConfig | null = null;
  public subscribedChannels: ChannelAddress[] = [];

  constructor(
    private service: Service,
    private websocket: Websocket,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent("Channels" /* TODO translate */, this.route).then(edge => {
      this.edge = edge;
    });
    this.service.getConfig().then(config => {
      this.config = config;
    })
  }

  subscribeChannel(componentId: string, channelId: string) {
    this.subscribedChannels.forEach((item, index) => {
      if (item.componentId === componentId && item.channelId === channelId) {
        // had already been in the list
        return;
      }
    });

    let address = new ChannelAddress(componentId, channelId);
    this.subscribedChannels.push(address);

    if (this.config) {
      let channelConfig = this.config.getChannel(address);
      if (channelConfig) {
        if (channelConfig.accessMode == "WO") {
          // do not subscribe Write-Only Channels
          return;
        }
      }
    }

    if (this.edge) {
      this.edge.subscribeChannels(this.websocket, ChannelsComponent.SELECTOR, this.subscribedChannels);
    }
  }

  unsubscribeChannel(address: ChannelAddress) {
    this.subscribedChannels.forEach((item, index) => {
      if (item.componentId === address.componentId && item.channelId === address.channelId) {
        this.subscribedChannels.splice(index, 1);
      }
    });
  }

  setChannelValue(address: ChannelAddress, value: any) {
    if (this.edge && this.service.websocket != null) {
      this.edge.sendRequest(
        this.service.websocket,
        new SetChannelVaLueRequest({
          componentId: address.componentId,
          channelId: address.channelId,
          value: value
        })
      ).then(response => {
        this.service.toast("Successfully set " + address.toString() + " to [" + value + "]", "success");
      }).catch(reason => {
        this.service.toast("Error setting " + address.toString() + " to [" + value + "]", 'danger');
      });
    }
  }

  ngOnDestroy() {
    if (this.edge != null) {
      this.edge.unsubscribeChannels(this.websocket, ChannelsComponent.SELECTOR);
    }
  }

}