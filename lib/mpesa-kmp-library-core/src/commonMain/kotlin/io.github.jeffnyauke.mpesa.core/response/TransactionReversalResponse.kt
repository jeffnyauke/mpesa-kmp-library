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
 * Transaction status response
 *
 * @property conversationId The unique request ID returned my M-PESA for each request made. Sample: Alpha-Numeric
 * String of less than 20 characters eg: 1236-7134259-1
 * @property originatorConversationId The unique request ID for tracking a transaction. Sample: Alpha Numeric String of
 * less than 20 characters eg: AG_20210709_1234409f86436c583e3f
 * @property responseCode The numeric status code indicates the status transaction processing. 0 means success and any
 * other code means an error occurred or the transaction failed. Success submission message or an error description.
 * Sample: 0 or 1 or 2001 or 21
 * @property responseDescription Response description message. Sample: Accept the service request  successfully
 */
@Serializable
public data class TransactionReversalResponse internal constructor(
    @SerialName("ConversationID") val conversationId: String,
    @SerialName("OriginatorConversationID") val originatorConversationId: String,
    @SerialName("ResponseCode") val responseCode: String,
    @SerialName("ResponseDescription") val responseDescription: String,
)
