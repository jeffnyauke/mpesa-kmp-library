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

package io.github.jeffnyauke.mpesa.core.constant

internal object Endpoints {
    const val ACCESS_TOKEN = "/oauth/v1/generate?grant_type=client_credentials"
    const val DYNAMIC_QR = "/mpesa/qrcode/v1/generate"
    const val STK_PUSH = "/mpesa/stkpush/v1/processrequest"
    const val STK_PUSH_QUERY = "/mpesa/stkpushquery/v1/query"
    const val C2B_REGISTER = "/mpesa/c2b/v1/registerurl"
    const val C2B = "/mpesa/c2b/v1/registerurl"
    const val B2C = "/mpesa/b2c/v1/paymentrequest"
    const val TRANSACTION_STATUS = "/mpesa/transactionstatus/v1/query"
    const val ACCOUNT_BALANCE = "/mpesa/accountbalance/v1/query"
    const val TRANSACTION_REVERSAL = "/mpesa/reversal/v1/request"
}
