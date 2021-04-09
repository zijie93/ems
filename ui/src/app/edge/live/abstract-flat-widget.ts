import { Directive, Inject, Input, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ModalController } from "@ionic/angular";
import { UUID } from "angular2-uuid";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { ChannelAddress, Edge, EdgeConfig, Service, Websocket } from "src/app/shared/shared";

@Directive()
export abstract class AbstractFlatWidget implements OnInit, OnDestroy {

    /**
     * True after this.edge, this.config and this.component are set.
     */
    public isInitialized: boolean = false;

    private selector: string = UUID.UUID().toString();

    @Input()
    protected componentId: string;

    public edge: Edge = null;
    public config: EdgeConfig = null;
    public component: EdgeConfig.Component = null;

    private stopOnDestroy: Subject<void> = new Subject<void>();

    public outputChannel: string;

    constructor(
        @Inject(Websocket) protected websocket: Websocket,
        @Inject(ActivatedRoute) protected route: ActivatedRoute,
        @Inject(Service) protected service: Service,
        @Inject(ModalController) protected modalCtrl: ModalController
    ) {
    }

    public ngOnInit() {
        this.service.setCurrentComponent('', this.route).then(edge => {
            this.service.getConfig().then(config => {
                // store important variables publically
                this.edge = edge;
                this.config = config;
                this.component = config.components[this.componentId];

                // announce initialized
                this.isInitialized = true;

                // get the channel addresses that should be subscribed
                let channelAddresses: ChannelAddress[] = this.getChannelAddressess();
                let channelIds = this.getChannelIds();
                for (let channelId of channelIds) {
                    channelAddresses.push(new ChannelAddress(this.componentId, channelId));
                }
                if (channelAddresses.length != 0) {
                    this.edge.subscribeChannels(this.websocket, this.selector, channelAddresses);
                }

                // call onCurrentData() with latest data
                edge.currentData.pipe(takeUntil(this.stopOnDestroy)).subscribe(currentData => {
                    let allComponents = {};
                    let thisComponent = {};
                    for (let channelAddress of channelAddresses) {
                        let ca = channelAddress.toString();
                        allComponents[ca] = currentData.channel[ca];
                        if (channelAddress.componentId === this.componentId) {
                            thisComponent[channelAddress.channelId] = currentData.channel[ca];
                        }
                    }
                    this.onCurrentData(thisComponent, allComponents);
                });
            });

        });
    };

    public ngOnDestroy() {
        // Unsubscribe from OpenEMS
        this.edge.unsubscribeChannels(this.websocket, this.selector);

        // Unsubscribe from CurrentData subject
        this.stopOnDestroy.next();
        this.stopOnDestroy.complete();
    }

    /**
     * Called on every new data.
     * 
     * @param data new data for the subscribed Channel-Addresses
     */
    protected onCurrentData(thisComponent: { [channelId: string]: any }, allComponents: { [channelAddress: string]: any }) {

    }

    /**
     * Gets the ChannelAddresses that should be subscribed.
     */
    protected getChannelAddressess(): ChannelAddress[] {
        return [];
    }

    /**
     * Gets the ChannelIds of the current Component that should be subscribed.
     */
    protected getChannelIds(): string[] {
        return [];
    }
}