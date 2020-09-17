import { JsonrpcResponseSuccess } from "../base";

export interface Cumulated {
    [channelAddress: string]: [number] | null
}

/**
 * Wraps a JSON-RPC Response for a queryHistoricTimeseriesEnergy.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": UUID,
 *   "result": {
 *     "data": Cumulated
 *     }
 * }
 * </pre>
 */
export class queryHistoricTimeseriesEnergyPerPeriodResponse extends JsonrpcResponseSuccess {

    public constructor(
        public readonly id: string,
        public readonly result: {
            data: Cumulated;
        }
    ) {
        super(id, result);
    }
}