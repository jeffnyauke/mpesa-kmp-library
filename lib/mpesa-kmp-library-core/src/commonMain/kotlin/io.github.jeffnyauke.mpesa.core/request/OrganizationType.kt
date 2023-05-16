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
 * Type of organization receiving the transaction.
 *
 * @property identifier The unique identifier for the organization.
 */
public enum class OrganizationType(internal val identifier: String) {
    /**
     * MSISDN.
     *
     * @constructor Set [OrganizationType] to MSISDN.
     */
    BuyGoods("1"),

    /**
     * Till Number.
     *
     * @constructor Set [OrganizationType] to Till Number.
     */
    TillNumber("2"),

    /**
     * Organization shortcode.
     *
     * @constructor Set [OrganizationType] to Organization shortcode.
     */
    Shortcode("4"),
}
