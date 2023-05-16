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

package io.github.jeffnyauke.mpesa.core.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ReversalRequest(
    @SerialName("CommandID") val commandId: String,
    @SerialName("ReceiverParty") val receiverParty: String,
    @SerialName("ReceiverIdentifierType") val receiverIdentifierType: String,
    @SerialName("Remarks") val remarks: String,
    @SerialName("Initiator") val initiator: String,
    @SerialName("SecurityCredential") val securityCredential: String,
    @SerialName("QueueTimeOutURL") val queueTimeOutUrl: String,
    @SerialName("ResultURL") val resultUrl: String,
    @SerialName("TransactionID") val transactionId: String,
    @SerialName("Occasion") val occasion: String,
)
