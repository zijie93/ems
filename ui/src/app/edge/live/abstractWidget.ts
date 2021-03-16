import { Component } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ModalController } from "@ionic/angular";
import { CurrentData } from "src/app/shared/edge/currentdata";
import { ChannelAddress, Edge, EdgeConfig, Service, Websocket } from "src/app/shared/shared";

@Component({
    selector: AbstractWidget.SELECTOR,
    templateUrl: 'abstractWidget.html'
})
export abstract class AbstractWidget {
    static SELECTOR: string = 'abstractWidget';
    public edge: Edge = null;
    public config: EdgeConfig = null;
    public currentData = CurrentData;

    constructor(
        public service: Service,
        private websocket: Websocket,
        private route: ActivatedRoute,
    ) { }
    /** getting the parameter from flatwidget for Subscribing 
     * @param selector getting the selector of each Widget
     * @param channelAddress getting the channels of each Widget
    */
    public subscribeOnChannels(selector: string, channelAddress: ChannelAddress[]) {
        this.service.getConfig().then(config => {
            this.config = config;
            this.service.setCurrentComponent('', this.route).then(edge => {
                this.edge = edge;
                /** subscribing on the passed selector and channelAddress */
                this.edge.subscribeChannels(this.websocket, selector, channelAddress);
                console.log("AbstractWidget: Channeladresses for " + selector + "-Widget: ", channelAddress);
            })
        });
    }
}
