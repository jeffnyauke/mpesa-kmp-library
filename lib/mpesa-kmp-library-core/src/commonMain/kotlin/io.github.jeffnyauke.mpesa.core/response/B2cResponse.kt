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
 * B2C acknowledgement response
 *
 * @property conversationId This is a global unique identifier for the transaction request returned by the M-Pesa
 * upon successful request submission.
 * @property originatorConversationId This is a global unique identifier for the transaction request returned by the
 * API proxy upon successful request submission.
 * @property responseCode This is a Numeric status code that indicates the status of the transaction submission. 0 means
 * successful submission and any other code means an error occurred.
 * @property responseDescription This is the status of the request.
 */
@Serializable
public data class B2cResponse internal constructor(
    @SerialName("ConversationID") val conversationId: String,
    @SerialName("OriginatorConversationID") val originatorConversationId: String,
    @SerialName("ResponseCode") val responseCode: String,
    @SerialName("ResponseDescription") val responseDescription: String,
)
