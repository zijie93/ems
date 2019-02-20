import { JsonrpcResponseSuccess } from "../base";

/**
 * Wraps a JSON-RPC Response for a QueryHistoricTimeseriesDataRequest.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": UUID,
 *   "result": {
 *     "timestamps": [
 *       '2011-12-03T10:15:30Z',...
 *     ],
 *     "data": {
 *       "componentId/channelId": [
 *         value1, value2,...
 *       ]
 *     }
 *   }
 * }
 * </pre>
 */
export class QuerykWhResponse extends JsonrpcResponseSuccess {

    public constructor(
        public readonly id: string,
        public readonly result: {
            data: { [channelAddress: string]: string | number }
        }
    ) {
        super(id, result);
    }
}