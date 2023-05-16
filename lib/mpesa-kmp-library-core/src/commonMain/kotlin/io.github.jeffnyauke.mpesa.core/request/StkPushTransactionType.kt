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

/**
 * This is the transaction type that is used to identify the transaction when sending the request to M-PESA.
 */
public enum class StkPushTransactionType {
    /**
     * Customer pay bill online.
     *
     * @constructor Set [StkPushTransactionType] to PayBill.
     */
    CustomerPayBillOnline,

    /**
     * Customer buy goods online.
     *
     * @constructor Set [StkPushTransactionType] to Buy Goods.
     */
    CustomerBuyGoodsOnline,
}
