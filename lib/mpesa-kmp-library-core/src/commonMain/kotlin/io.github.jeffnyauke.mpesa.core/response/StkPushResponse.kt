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
 * STK push acknowledgement response
 *
 * @property merchantRequestId This is a global unique Identifier for any submitted payment request.
 * @property checkoutRequestId This is a global unique identifier of the processed checkout transaction request.
 * @property responseCode This is a Numeric status code that indicates the status of the transaction submission. 0 means
 * successful submission and any other code means an error occurred.
 * @property responseDescription Response description is an acknowledgment message from the API that gives the status of
 * the request submission. It usually maps to a specific ResponseCode value. It can be a Success submission message or
 * an error description.
 * @property customerMessage This is a message that your system can display to the customer as an acknowledgment of the
 * payment request submission.
 */
@Serializable
public data class StkPushResponse internal constructor(
    @SerialName("MerchantRequestID") val merchantRequestId: String,
    @SerialName("CheckoutRequestID") val checkoutRequestId: String,
    @SerialName("ResponseCode") val responseCode: String,
    @SerialName("ResponseDescription") val responseDescription: String,
    @SerialName("CustomerMessage") val customerMessage: String,
)
