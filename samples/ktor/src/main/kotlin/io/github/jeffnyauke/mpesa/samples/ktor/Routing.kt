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

package io.github.jeffnyauke.mpesa.samples.ktor

import io.github.jeffnyauke.mpesa.core.Mpesa
import io.github.jeffnyauke.mpesa.core.request.B2cCommandId.SalaryPayment
import io.github.jeffnyauke.mpesa.core.request.C2bCommandId
import io.github.jeffnyauke.mpesa.core.request.C2bRegisterTransactionType
import io.github.jeffnyauke.mpesa.core.request.DynamicQrTransactionType
import io.github.jeffnyauke.mpesa.core.request.OrganizationType
import io.github.jeffnyauke.mpesa.core.request.StkPushTransactionType.CustomerPayBillOnline
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    val mpesa = Mpesa("86smaD2TEnlXLVp9yOGvBiA9Znd3iHh3", "utbzOaE5a0LZFGB2")
    routing {
        get("/dynamicQr") {
            val response = mpesa.dynamicQr(
                "TEST SUPERMARKET",
                "Invoice Test",
                "10",
                DynamicQrTransactionType.BuyGoods,
                "373132",
            )
            call.respond(response)
        }

        get("/stk") {
            val response = mpesa.stkPush(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                CustomerPayBillOnline,
                "1",
                "254708374149",
                "254708374149",
                "174379",
                "https://mydomain.com/path",
                "CompanyXLTD",
                "Payment of X",
            )
            call.respond(response)
        }

        get("/stkPushQuery") {
            val response = mpesa.stkPushQuery(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                "ws_CO_07052023010832149708374149",
            )
            call.respond(response)
        }

        get("/c2bRegister") {
            val response = mpesa.c2bRegister(
                "https://mydomain.com/validation",
                "https://mydomain.com/confirmation",
                C2bRegisterTransactionType.Completed,
                "600978",
            )
            call.respond(response)
        }

        get("/c2b") {
            val response = mpesa.c2b(
                C2bCommandId.CustomerPayBillOnline,
                "1",
                "254708374149",
                "fdddg",
                "600584",
            )
            call.respond(response)
        }

        get("/b2c") {
            val response = mpesa.b2c(
                "NpkdHQZmUJZFu9YO/NxDDWD5xfWn1car4HDHCW6NUvL1KGC5AmxMgXHR5G++5G6R/t7WeJPPHKe7qpaZiNE4mesBBCW8WrFVYw8S+ExvJeuixG6MBOKz8DVvgeQcgn/VihZeyaD+MGJE1AGmUcVGDyR2OzLq5x+XzZhIXUJxzKMLgnVoLIzIGciYNVy0HBLCv0FTIN8BVUbzQAP113PzIXGcr3RFs2UlY6JI0P+zbrPapaP/MmQbXK0ZDoH2pggDBWhOuhriCYFbC4ecR2KyIkn1fC4HOAeyri89WNoBdU1rjRRyZQ2SbNg2ylr/YO8YuPK6s2zZSHm10D9dcYs1tQ==",
                "testapi",
                SalaryPayment,
                "1",
                "600988",
                "254708374149",
                "Test remarks",
                "https://mydomain.com/b2c/queue",
                "https://mydomain.com/b2c/result",
                "Test occasion",
            )
            call.respond(response)
        }

        get("/transactionStatus") {
            val response = mpesa.transactionStatus(
                "600987",
                OrganizationType.TillNumber,
                "status",
                "testapi",
                "Ku1az7fyAtMWyEEfcVMwbvfYyeXaL6KlIoF+Uk4s/gDksLnBjc3Q0tY1xs861tS6DfuMF/QmX+blYsOlf4bTq38U9TgNPe/5UfRKRfXsv5qa5Q7Zjq9znE+pXp0ylEe2rfGyfjxhj+9qHprwN6dBjALpI0Ybl08Qxuw0xwmKaP0/o/oQpbWTBJIn97Y8QrkCU6rr2X7yOJBdiPH+jBw0V4lUT6TM0wwea1XpfxrM7wwNs9NEr65Zb41yFnGVwqTyAyskLbYOP73pXwBWN5pMs3WHcG+Ga3EKtALjZW43gM3G3+R51qZYwhsy09mhtiTebEIn68VGDNlcCNQgVh4qew==",
                "https://mydomain.com/TransactionStatus/queue/",
                "https://mydomain.com/TransactionStatus/result/",
                "OEI2AK4Q16",
                "Test occasion",
                "",
            )
            call.respond(response)
        }

        get("/accountBalance") {
            val response = mpesa.accountBalance(
                "600981",
                OrganizationType.TillNumber,
                "Test remarks",
                "testapi",
                "ipnaagxLiFq6gXOfC13sI9s11bXgF+1BNCsGKnwJsq8HuJ6WbhyVk8LbJ8tsutgBO6onYb6L1zlvpVftvsFZr/ul7Lnr6GRhjSAN/Gnrd/y1Wa3aaLCfDhQF1Rj2hcagR3/+7Bb2S8vXFHFPQ647lQPGUIjmYD/n7IGMBnoNBLZOGx6NK02d4iv4NBDT8BvLXetm6FGaazAPp86dosOfa+jpuRkK715vfIg2n5klhzrWHAPzi4zgEi39lrB4zwjFUos7e76A6OaklE4+w5a0l4K+yI5ZVsTHGiY5Q/YfXis95kFlZkxSk5f8n+ehAcYX30mXklPAifEM0k2j1deOOQ==",
                "https://mydomain.com/AccountBalance/queue/",
                "https://mydomain.com/AccountBalance/result/",
            )
            call.respond(response)
        }

        get("/transactionReversal") {
            val response = mpesa.transactionReversal(
                "600987",
                OrganizationType.TillNumber,
                "Test remarks",
                "testapi",
                "o2RS+OQFb9WZAtnqurzZB8CR91sJe/AD9JK4/lPmySDisjtWnqWTtKd+15dLtDFv4ASs17uxjiMN/qRR2Qx3Ei0Qx9RLH77AmqIy6OIpPk1G8GLJA5qe3smh6JbzHuSD5OCsfkQceW+u58ytkB6TvLYgsrKyS64XKTqjqpUsqJE/MFvdKxpZ+VoJi/3M2h3fgfQmzyZWTgsnuAYUziiOvTz2v0bQ72XAp5JjqVuPs7DYxhvRM07lBoGI8+lggyj0JRyCX+Q5yQam6f4FbYrI9EF6jGGC3o5GUZ0sgKDPP+gxwBiyryNj7bFHioG4X6bKH2c3/s67cTG/LCfk5bomyg==",
                "https://mydomain.com/Reversal/queue/",
                "https://mydomain.com/Reversal/result/",
                "OEI2AK4Q16",
                "Test occasion",
            )
            call.respond(response)
        }

        post("/callback") {
            println(call.receiveText())
            call.respond(CallBackResponse(0, "The service was accepted successfully", "1234567890"))
        }
    }
}

@Serializable
data class CallBackResponse(val ResultCode: Int, val ResultDesc: String, val ThirdPartyTransID: String)
