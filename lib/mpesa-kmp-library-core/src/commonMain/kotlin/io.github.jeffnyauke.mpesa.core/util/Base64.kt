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

package io.github.jeffnyauke.mpesa.core.util

import io.ktor.util.encodeBase64
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun getEncodedCredentials(appKey: String, appSecret: String): String {
    return ("$appKey:$appSecret").encodeBase64()
}

@JvmSynthetic
internal fun getEncodedPassword(businessShortCode: String, passKey: String, timeStamp: String): String {
    return buildString {
        append(businessShortCode)
        append(passKey)
        append(timeStamp)
    }.encodeBase64()
}
