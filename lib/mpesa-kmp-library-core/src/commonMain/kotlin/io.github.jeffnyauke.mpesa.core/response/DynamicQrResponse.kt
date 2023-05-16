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
 * Dynamic QR response
 *
 * @property responseCode Code representing the status of the response.
 *      "00" : Successful,
 *      "05" : Error Occurred,
 *      "39" :Invalid Account Exception,
 *      "51" : Invalid Merchant Exception,
 *      "91" : Connection Timeout Exception.
 *  Sample: 00
 * @property responseDescription This is a response describing the status of the transaction. A string with the message
 * "Success or Generation has failed". Sample: Success
 * @property qrCode QR Code Image Data/String/Image Sample: Alpha-numeric string containing the QR code
 */
@Serializable
public data class DynamicQrResponse internal constructor(
    @SerialName("ResponseCode") val responseCode: String,
    @SerialName("ResponseDescription") val responseDescription: String,
    @SerialName("QRCode") val qrCode: String,
)
