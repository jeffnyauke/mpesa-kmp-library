/*
 * Copyright 2023 Jeffrey Nyauke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jeffnyauke.mpesa.core.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * STK push query acknowledgement response
 *
 * @property merchantRequestId This is a global unique Identifier for any submitted payment request.
 * Sample: 16813-1590513-1
 * @property checkoutRequestId This is a global unique identifier of the processed checkout transaction request.
 * Sample: ws_CO_DMZ_123212312_2342347678234
 * @property responseCode This is a Numeric status code that indicates the status of the transaction submission. 0 means
 * successful submission and any other code means an error occurred. Sample: 0
 * @property resultDec Result description is a message from the API that gives the status of the request processing,
 * usually maps to a specific ResultCode value. It can be a Success processing message or an error description message.
 * Sample: 0: The service request is processed successfully., 1032: Request cancelled by user
 * @property responseDescription Response description is an acknowledgment message from the API that gives the status of
 * the request submission usually maps to a specific ResponseCode value. It can be a Success submission message or an
 * error description. Sample: The service request has failed, The service request has been accepted successfully
 * @property resultCode This is a numeric status code that indicates the status of the transaction processing. 0 means
 * successful processing and any other code means an error occurred or the transaction failed. Sample: 0, 1032
 */
@Serializable
public data class StkPushQueryResponse internal constructor(
    @SerialName("MerchantRequestID") val merchantRequestId: String,
    @SerialName("CheckoutRequestID") val checkoutRequestId: String,
    @SerialName("ResponseCode") val responseCode: String,
    @SerialName("ResultDesc") val resultDec: String,
    @SerialName("ResponseDescription") val responseDescription: String,
    @SerialName("ResultCode") val resultCode: String,
)
