import { JsonrpcNotification } from "../base";
import { UUID } from "angular2-uuid";

/**
 * Represents a JSON-RPC Notification for sending the current data of all
 * subscribed Channels.
 *  
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "method": "currentData",
 *   "params": {
 *     [channelAddress]: string | number
 *   }
 * }
 * </pre>
 */
export class CurrentDataNotification extends JsonrpcNotification {

    public static readonly METHOD: string = "currentData";

    public constructor(
        public readonly params: { [channelAddress: string]: string | number }
    ) {
        super(CurrentDataNotification.METHOD, params);
    }

}