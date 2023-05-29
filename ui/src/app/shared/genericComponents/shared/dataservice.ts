import { Injectable } from "@angular/core";
import { BehaviorSubject, Subject } from "rxjs";

import { ChannelAddress, Edge } from "../../shared";

@Injectable()
export abstract class DataService {

  protected edge: Edge | null = null;
  protected stopOnDestroy: Subject<void> = new Subject<void>();

  /** Used to retrieve values */
  public currentValue: BehaviorSubject<{ allComponents: {}, thisComponent: {} }> = new BehaviorSubject({ allComponents: {}, thisComponent: {} });

  /**
   * Gets the values from passed channelAddresses
   * 
   * @param channelAddress the channelAddresses to be subscribed
   * @param edge the edge
   * @param componentId the componentId
   */
  public abstract getValues(channelAddress: ChannelAddress[], edge: Edge, componentId?: string)
}