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
 * This parameter specifies what is to happen if for any reason the validation URL is not reachable. Note that, this is
 * the default action value that determines what M-PESA will do in the scenario that your endpoint is unreachable or is
 * unable to respond on time.
 */
public enum class C2bRegisterTransactionType {
    /**
     * M-pesa will automatically complete your transaction.
     *
     * @constructor Set [C2bRegisterTransactionType] to complete.
     */
    Completed,

    /**
     * M-PESA will automatically cancel the transaction, in the event M-PESA is unable to reach your Validation URL
     *
     * @constructor Set [C2bRegisterTransactionType] to cancel.
     */
    Cancelled,
}
