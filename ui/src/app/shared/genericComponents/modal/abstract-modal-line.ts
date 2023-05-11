import { ChangeDetectorRef, Directive, Inject, Input, OnChanges, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { ModalController } from "@ionic/angular";
import { TranslateService } from "@ngx-translate/core";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { ChannelAddress, CurrentData, Edge, EdgeConfig, Service, Utils, Websocket } from "src/app/shared/shared";
import { v4 as uuidv4 } from 'uuid';
import { Role } from "../../type/role";

@Directive()
export abstract class AbstractModalLine implements OnInit, OnDestroy, OnChanges {

    /** FormGroup */
    @Input() public formGroup: FormGroup;

    /** component */
    @Input() public component: EdgeConfig.Component = null;

    /** FormGroup ControlName */
    @Input() public controlName: string;

    /**
    * Use `converter` to convert/map a CurrentData value to another value, e.g. an Enum number to a text.
    * 
    * @param value the value from CurrentData
    * @returns converter function
    */
    @Input() public converter = (value: any): string => { return value }

    /**
    * Use `converter` to convert/map a CurrentData value to another value, e.g. an Enum number to a text.
    * 
    * @param value the value from CurrentData
    * @returns converter function
    */
    @Input() public filter = (value: any): boolean => { return true }

    /** Name for parameter, displayed on the left side*/
    @Input() public name: string;

    @Input() public nameSuffix = (value: any): string => { return "" }
    @Input() public value: number | string;
    @Input() public roleIsAtLeast?: Role = Role.GUEST;
    protected show: boolean = true;

    /** Channel defines the channel, you need for this line */
    @Input()
    set channelAddress(channelAddress: string) {
        if (channelAddress) {
            this.subscribe(ChannelAddress.fromString(channelAddress));
        }
    }

    /** Selector needed for Subscribe (Identifier) */
    private selector: string = uuidv4()

    /** 
     * displayValue is the displayed @Input value in html
     */
    public displayValue: string = null;
    public displayName: string = null;

    /** Checks if any value of this line can be seen => hides line if false
     * 
     * @deprecated can be remove in future when live-view is refactored with formlyfield
     */
    protected isAllowedToBeSeen: boolean = true;
    public edge: Edge = null;
    public config: EdgeConfig = null;
    public stopOnDestroy: Subject<void> = new Subject<void>();

    protected readonly Role = Role;
    protected readonly Utils = Utils;

    constructor(
        @Inject(Websocket) protected websocket: Websocket,
        @Inject(ActivatedRoute) protected route: ActivatedRoute,
        @Inject(Service) protected service: Service,
        @Inject(ModalController) protected modalCtrl: ModalController,
        @Inject(TranslateService) protected translate: TranslateService,
        @Inject(FormBuilder) public formBuilder: FormBuilder,
        private ref: ChangeDetectorRef
    ) {
        ref.detach();
        setInterval(() => {
            this.ref.detectChanges(); // manually trigger change detection
        }, 0);
    }

    ngOnChanges() {
        this.setValue(this.value)
    }

    ngOnInit() {
        this.service.setCurrentComponent('', this.route).then(edge => {
            this.service.getConfig().then(config => {
                // store important variables publically
                this.edge = edge;
                this.config = config;

                // get the channel addresses that should be subscribed
                let channelAddresses: ChannelAddress[] = this.getChannelAddresses();
                let channelIds = this.getChannelIds();
                for (let channelId of channelIds) {
                    channelAddresses.push(new ChannelAddress(this.component.id, channelId));
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
                        if (channelAddress.componentId === this.component.id) {
                            thisComponent[channelAddress.channelId] = currentData.channel[ca];
                        }
                    }
                    this.onCurrentData({ thisComponent: thisComponent, allComponents: allComponents });
                });
            })
        })
    }

    /** value defines value of the parameter, displayed on the right */
    protected setValue(value: number | string) {

        if (value != null && this.filter) {
            this.show = this.filter(value) ?? true;
        }

        if (this.nameSuffix && value != null) {
            this.displayName = this.name + this.nameSuffix(value)
        } else {
            this.displayName = this.name
        }

        if (this.converter) {
            this.displayValue = this.converter(value);
        }
    }

    /** Subscribe on HTML passed Channels */
    protected subscribe(channelAddress: ChannelAddress) {
        this.service.setCurrentComponent('', this.route).then(edge => {
            this.edge = edge;

            // Check if user is allowed to see these channel-values
            if (this.edge.roleIsAtLeast(this.roleIsAtLeast)) {
                this.isAllowedToBeSeen = true;
                edge.subscribeChannels(this.websocket, this.selector, [channelAddress]);

                // call onCurrentData() with latest data
                edge.currentData.pipe(takeUntil(this.stopOnDestroy)).subscribe(currentData => {
                    if (currentData.channel[channelAddress.toString()] != null) {
                        this.setValue(currentData.channel[channelAddress.toString()])
                    }
                });
            } else {
                this.isAllowedToBeSeen = false;
            }
        })

    }

    public ngOnDestroy() {
        // Unsubscribe from OpenEMS
        if (this.edge != null) {
            this.edge.unsubscribeChannels(this.websocket, this.selector);
        }

        // Unsubscribe from CurrentData subject
        this.stopOnDestroy.next();
        this.stopOnDestroy.complete();
    }
    /**
     * Called on every new data.
     * 
     * @param currentData new data for the subscribed Channel-Addresses
     */
    protected onCurrentData(currentData: CurrentData) {
    }

    /**
     * Gets the ChannelAddresses that should be subscribed.
     */
    protected getChannelAddresses(): ChannelAddress[] {
        return [];
    }
    protected getFormGroup(): FormGroup {
        return
    }
    /**
   * Gets the ChannelIds of the current Component that should be subscribed.
   */
    protected getChannelIds(): string[] {
        return [];
    }
}