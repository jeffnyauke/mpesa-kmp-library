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
 * This is a unique command that specifies B2C transaction type.
 */
public enum class B2cCommandId {
    /**
     * This supports sending money to both registered and unregistered M-Pesa customers.
     *
     * @constructor Set [B2cCommandId] to salary payment.
     */
    SalaryPayment,

    /**
     * This is a normal business to customer payment,  supports only M-Pesa registered customers.
     *
     * @constructor Set [B2cCommandId] to business payment.
     */
    BusinessPayment,

    /**
     * This is a promotional payment to customers.
     *
     * @constructor Set [B2cCommandId] to promotion payment.
     */
    PromotionPayment,
}
