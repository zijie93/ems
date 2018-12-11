import { JsonrpcRequest, JsonrpcResponse, JsonrpcResponseSuccess } from "../base";

/**
 * Wraps a JSON-RPC Response for a EdgeRpcRequest.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": UUID,
 *   "params": {
 *     "payload": JsonrpcRequest
 *   }
 * }
 * </pre>
 */
export class EdgeRpcResponse extends JsonrpcResponseSuccess {

    public constructor(
        public readonly id: string,
        public readonly payload: JsonrpcRequest
    ) {
        super(id, { payload: payload });
    }

}