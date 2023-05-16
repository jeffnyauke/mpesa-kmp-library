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

package io.github.jeffnyauke.mpesa.core

import io.github.jeffnyauke.mpesa.core.constant.Endpoints.ACCESS_TOKEN
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.ACCOUNT_BALANCE
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.B2C
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.C2B
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.C2B_REGISTER
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.DYNAMIC_QR
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.STK_PUSH
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.STK_PUSH_QUERY
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.TRANSACTION_REVERSAL
import io.github.jeffnyauke.mpesa.core.constant.Endpoints.TRANSACTION_STATUS
import io.github.jeffnyauke.mpesa.core.constant.Environment.PRODUCTION
import io.github.jeffnyauke.mpesa.core.constant.Environment.SANDBOX
import io.github.jeffnyauke.mpesa.core.network.bodyOrThrow
import io.github.jeffnyauke.mpesa.core.network.provideHttpClient
import io.github.jeffnyauke.mpesa.core.request.AccountBalanceRequest
import io.github.jeffnyauke.mpesa.core.request.B2cCommandId
import io.github.jeffnyauke.mpesa.core.request.B2cRequest
import io.github.jeffnyauke.mpesa.core.request.C2bCommandId
import io.github.jeffnyauke.mpesa.core.request.C2bRegisterRequest
import io.github.jeffnyauke.mpesa.core.request.C2bRegisterTransactionType
import io.github.jeffnyauke.mpesa.core.request.C2bRequest
import io.github.jeffnyauke.mpesa.core.request.DynamicQrRequest
import io.github.jeffnyauke.mpesa.core.request.DynamicQrTransactionType
import io.github.jeffnyauke.mpesa.core.request.OrganizationType
import io.github.jeffnyauke.mpesa.core.request.ReversalRequest
import io.github.jeffnyauke.mpesa.core.request.StkPushQueryRequest
import io.github.jeffnyauke.mpesa.core.request.StkPushRequest
import io.github.jeffnyauke.mpesa.core.request.StkPushTransactionType
import io.github.jeffnyauke.mpesa.core.request.TransactionStatusRequest
import io.github.jeffnyauke.mpesa.core.response.AccountBalanceResponse
import io.github.jeffnyauke.mpesa.core.response.AuthorizationResponse
import io.github.jeffnyauke.mpesa.core.response.B2cResponse
import io.github.jeffnyauke.mpesa.core.response.C2bResponse
import io.github.jeffnyauke.mpesa.core.response.DynamicQrResponse
import io.github.jeffnyauke.mpesa.core.response.StkPushQueryResponse
import io.github.jeffnyauke.mpesa.core.response.StkPushResponse
import io.github.jeffnyauke.mpesa.core.response.TransactionReversalResponse
import io.github.jeffnyauke.mpesa.core.response.TransactionStatusResponse
import io.github.jeffnyauke.mpesa.core.util.getDarajaTimestamp
import io.github.jeffnyauke.mpesa.core.util.getEncodedCredentials
import io.github.jeffnyauke.mpesa.core.util.getEncodedPassword
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders.Authorization
import io.github.jeffnyauke.mpesa.core.constant.CommandId.ACCOUNT_BALANCE as COMMAND_ID_ACCOUNT_BALANCE
import io.github.jeffnyauke.mpesa.core.constant.CommandId.TRANSACTION_REVERSAL as COMMAND_ID_TRANSACTION_REVERSAL
import io.github.jeffnyauke.mpesa.core.constant.CommandId.TRANSACTION_STATUS_QUERY as COMMAND_ID_TRANSACTION_STATUS_QUERY

/**
 * ███╗░░░███╗░░░░░░██████╗░███████╗░██████╗░█████╗░
 * ████╗░████║░░░░░░██╔══██╗██╔════╝██╔════╝██╔══██╗
 * ██╔████╔██║█████╗██████╔╝█████╗░░╚█████╗░███████║
 * ██║╚██╔╝██║╚════╝██╔═══╝░██╔══╝░░░╚═══██╗██╔══██║
 * ██║░╚═╝░██║░░░░░░██║░░░░░███████╗██████╔╝██║░░██║
 * ╚═╝░░░░░╚═╝░░░░░░╚═╝░░░░░╚══════╝╚═════╝░╚═╝░░╚═╝
 *
 * The client allows you to perform various M-Pesa transactions through the Daraja v2.0 API.
 *
 * You can get your consumer key and secret from the [official Daraja website](https://developer.safaricom.co.ke).
 *
 * @property appKey Daraja API consumer key.
 * @property appSecret Daraja API consumer secret.
 * @property isProduction Daraja API application environment.
 * @constructor Creates an instance of the [Mpesa] client.
 */
// @JsExport
public class Mpesa(
    private val appKey: String,
    private val appSecret: String,
    private val isProduction: Boolean = false,
) {
    private val client = provideHttpClient(if (isProduction) PRODUCTION else SANDBOX)

    /**
     * Authorize - Gives you a time bound access token to call allowed APIs.
     *
     * This API uses the [appKey] and [appSecret] (consumer keys) to generate the tokens for authenticating your API
     * calls. This is the first API you will engage with within the set of APIs available because all the other APIs
     * require authentication information from this API to work.
     * @return successful response [AuthorizationResponse] which contains the token.
     */
    private suspend fun authorize(): AuthorizationResponse {
        val credentials = getEncodedCredentials(appKey, appSecret)
        return client.get(ACCESS_TOKEN) {
            header(Authorization, "Basic $credentials")
        }.bodyOrThrow()
    }

    /**
     * Dynamic QR - Generates a dynamic M-PESA QR Code.
     *
     * Use this API to generate a Dynamic QR which enables Safaricom M-PESA customers who have My Safaricom App or
     * M-PESA app, to scan a QR (Quick Response) code, to capture till number and amount then authorize to pay for goods
     * and services at select LIPA NA M-PESA (LNM) merchant outlets.
     *
     * @param merchantName Name of the Company/M-Pesa Merchant Name. Sample: Safaricom LTD
     * @param refNo Transaction Reference. Sample: rf38f04
     * @param amount The total amount for the sale/transaction. Sample: 20000
     * @param transactionType [DynamicQrTransactionType] Transaction Type. Sample: [DynamicQrTransactionType.BuyGoods]
     * @param cpi Credit Party Identifier. Can be a Mobile Number, Business Number, Agent Till, PayBill or Business
     * number, or Merchant Buy Goods. Sample: 17408
     * @return successful response [DynamicQrResponse]
     */
    public suspend fun dynamicQr(
        merchantName: String,
        refNo: String,
        amount: String,
        transactionType: DynamicQrTransactionType,
        cpi: String,
    ): DynamicQrResponse = authorized { _, accessToken ->
        val requestObject = DynamicQrRequest(merchantName, refNo, amount, transactionType.code, cpi)
        client.post(DYNAMIC_QR) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * STK Push - Lipa na M-Pesa Online API also known as M-PESA express (STK Push) is a Merchant/Business initiated
     * C2B (Customer to Business) Payment.
     *
     * Once you, our merchant integrate with the API, you will be able to send a payment prompt on the customer's phone
     * (Popularly known as STK Push Prompt) to your customer's M-PESA registered phone number requesting them to enter
     * their M-PESA pin to authorize and complete payment.
     *
     * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to 6-digit account number)
     * used to identify an organization and receive the transaction. Sample: Shortcode (5 to 7 digits) e.g. 654321
     * @param passKey If you need a passkey for mpesa online payments solutions (specifically express stk push) simply
     * email m-pesabusiness@safaricom.co.ke and they will send it to you.
     * @param transactionType [StkPushTransactionType] This is the transaction type that is used to identify the
     * transaction when sending the request to M-PESA. Sample: [StkPushTransactionType.CustomerBuyGoodsOnline]
     * @param amount This is the Amount transacted normally a numeric value. Money that customer pays to the Shortcode.
     * Only whole numbers are supported. Sample: 10
     * @param phoneNumber The Mobile Number to receive the STK Pin Prompt. This number can be the same as PartyA value
     * above. MSISDN (12 digits Mobile Number). Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX
     * @param partyA The phone number sending money. The parameter expected is a Valid Safaricom Mobile Number that is
     * M-PESA registered in the format 254XXXXXXXXX. Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX
     * @param partyB The organization receiving the funds. The parameter expected is a 5 to 6 digit as defined on the
     * Shortcode description above. This can be the same as BusinessShortCode value above. Sample: Shortcode
     * (5-7 digits)
     * @param callBackUrl A CallBack URL is a valid secure URL that is used to receive notifications from M-Pesa API.
     * It is the endpoint to which the results will be sent by M-Pesa API. Sample: https://ip or domain:port/path,
     * e.g: https://mydomain.com/path, https://0.0.0.0:9090/path
     * @param accountReference This is an Alpha-Numeric parameter that is defined by your system as an Identifier of
     * the transaction for CustomerPayBillOnline transaction type. Along with the business name, this value is also
     * displayed to the customer in the STK Pin Prompt message. Maximum of 12 characters. Sample: Any combinations of
     * letters and numbers
     * @param transactionDesc This is any additional information/comment that can be sent along with the request from
     * your system. Maximum of 13 Characters. Sample: Any string between 1 and 13 characters.
     * @return successful acknowledgement response [StkPushResponse]
     */
    public suspend fun stkPush(
        businessShortCode: String,
        passKey: String,
        transactionType: StkPushTransactionType,
        amount: String,
        phoneNumber: String,
        partyA: String,
        partyB: String,
        callBackUrl: String,
        accountReference: String,
        transactionDesc: String,
    ): StkPushResponse = authorized { timestamp, accessToken ->
        val requestObject = StkPushRequest(
            businessShortCode,
            getEncodedPassword(businessShortCode, passKey, timestamp),
            timestamp,
            transactionType.name,
            amount,
            phoneNumber,
            partyA,
            partyB,
            callBackUrl,
            accountReference,
            transactionDesc,
        )
        client.post(STK_PUSH) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * STK Push query - Check the status of a Lipa Na M-Pesa Online Payment.
     *
     * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to 7-digit account number)
     * used to identify an organization and receive the transaction. Sample: Shortcode (5 to 7 digits) e.g. 654321
     * @param passKey If you need a passkey for mpesa online payments solutions (specifically express STK push) simply
     * email m-pesabusiness@safaricom.co.ke and they will send it to you.
     * @param checkoutRequestId This is a global unique identifier of the processed checkout transaction request.
     * Sample: ws_CO_DMZ_123212312_2342347678234
     * @return successful response [StkPushQueryResponse]
     */
    public suspend fun stkPushQuery(
        businessShortCode: String,
        passKey: String,
        checkoutRequestId: String,
    ): StkPushQueryResponse = authorized { timestamp, accessToken ->
        val requestObject = StkPushQueryRequest(
            businessShortCode,
            getEncodedPassword(businessShortCode, passKey, timestamp),
            timestamp,
            checkoutRequestId,
        )
        client.post(STK_PUSH_QUERY) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * C2B register
     *
     * @param validationUrl This is the URL that receives the validation request from the API upon payment submission.
     * The validation URL is only called if the external validation on the registered shortcode is enabled. (By default
     * External Validation is disabled).
     * @param confirmationUrl This is the URL that receives the confirmation request from API upon payment completion.
     * @param responseType [C2bRegisterTransactionType] This parameter specifies what is to happen if for any reason the
     * validation URL is not reachable. Note that, this is the default action value that determines what M-PESA will do
     * in the scenario that your endpoint is unreachable or is unable to respond on time. Sample :
     * [C2bRegisterTransactionType.Completed]
     * @param shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the organization.
     * Sample: 123456
     * @return successful acknowledgement response [C2bResponse]
     */
    public suspend fun c2bRegister(
        validationUrl: String,
        confirmationUrl: String,
        responseType: C2bRegisterTransactionType,
        shortCode: String,
    ): C2bResponse = authorized { _, accessToken ->
        val requestObject = C2bRegisterRequest(validationUrl, confirmationUrl, responseType.name, shortCode)
        client.post(C2B_REGISTER) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * C2B - Transact between a phone number to an M-Pesa short code registered on M-Pesa
     *
     * @param commandId [C2bCommandId] This is a unique command that specifies B2C transaction type.
     * @param amount The amount of money being sent to the short code.
     * @param msisdn This is the mobile number of the customer making the payment. Sample: MSISDN (12 digits Mobile
     * Number) e.g. 254XXXXXXXXX
     * @param billRefNumber This is the account number for which the customer is making the payment. This is only
     * applicable to Customer PayBill Transactions. Sample: An alphanumeric value of up to 20 characters.
     * @param shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the organization.
     * Sample: 123456
     * @return successful acknowledgement response [C2bResponse]
     */
    public suspend fun c2b(
        commandId: C2bCommandId,
        amount: String,
        msisdn: String,
        billRefNumber: String,
        shortCode: String,
    ): B2cResponse = authorized { _, accessToken ->
        val requestObject = C2bRequest(commandId.name, amount, msisdn, billRefNumber, shortCode)
        client.post(C2B) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * B2C - Transact between an M-Pesa short code to a phone number registered on M-Pesa
     *
     * @param securityCredential This is the value obtained after encrypting the API initiator password. The password on
     * Sandbox has been provisioned on the simulator. However, on production the password is created when the user is
     * being created on the M-PESA organization portal.
     * @param initiatorName This is an API user created by the Business Administrator of the M-PESA Bulk disbursement
     * account that is active and authorized to initiate B2C transactions via API. Sample: initiator_1, John_Doe,
     * John Doe
     * @param commandId [B2cCommandId] This is a unique command that specifies B2C transaction type.
     * @param amount The amount of money being sent to the customer.
     * @param partyA This is the B2C organization shortcode from which the money is to be sent.
     * @param partyB This is the customer mobile number  to receive the amount. - The number should have the country
     * code (254) without the plus sign.
     * @param remarks Any additional information to be associated with the transaction.
     * @param queueTimeOutURL This is the URL to be specified in your request that will be used by API Proxy to send
     * notification in case the payment request is timed out while awaiting processing in the queue.
     * @param resultURL This is the URL to be specified in your request that will be used by M-Pesa to send notification
     * upon processing of the payment request.
     * @param occasion Any additional information to be associated with the transaction.
     * @return successful acknowledgement response [B2cResponse]
     */
    public suspend fun b2c(
        securityCredential: String,
        initiatorName: String,
        commandId: B2cCommandId,
        amount: String,
        partyA: String,
        partyB: String,
        remarks: String,
        queueTimeOutURL: String,
        resultURL: String,
        occasion: String,
    ): B2cResponse = authorized { _, accessToken ->
        val requestObject = B2cRequest(
            initiatorName,
            securityCredential,
            commandId.name,
            amount.toFloat(),
            partyA,
            partyB,
            remarks,
            queueTimeOutURL,
            resultURL,
            occasion,
        )
        client.post(B2C) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * Transaction status - Check the status of a transaction
     *
     * @param partyA Organization/MSISDN receiving the transaction. Sample: Shortcode (6-9 digits) MSISDN (12 Digits)
     * @param organizationType Type of organization receiving the transaction. Sample: [OrganizationType.BuyGoods]
     * @param remarks Comments that are sent along with the transaction. Sample: A sequence of characters up to 100
     * @param initiator The name of the initiator initiating the request. Sample: This is the credential/username used
     * to authenticate the transaction request
     * @param securityCredential Encrypted credential of the user getting transaction status. Sample: Encrypted
     * password for the initiator to authenticate the transaction request
     * @param queueTimeOutURL The path that stores information of timeout transaction. Sample: https://ip:port/path or
     * domain:port/path
     * @param resultURL The path that stores information of a transaction. Sample: https://ip:port/path or
     * domain:port/path
     * @param transactionID Unique identifier to identify a transaction on Mpesa. Sample: LXXXXXX1234
     * @param occasion Optional parameter. Sample: A sequence of characters up to 100
     * @param originatorConversationId This is a global unique identifier for the transaction request returned by the
     * API proxy upon successful request submission. If you don’t have the M-PESA transaction ID you can use this to
     * query. Sample: AG_20190826_0000777ab7d848b9e721
     * @return successful acknowledgement response [TransactionStatusResponse]
     */
    public suspend fun transactionStatus(
        partyA: String,
        organizationType: OrganizationType,
        remarks: String,
        initiator: String,
        securityCredential: String,
        queueTimeOutURL: String,
        resultURL: String,
        transactionID: String,
        occasion: String,
        originatorConversationId: String,
    ): TransactionStatusResponse = authorized { _, accessToken ->
        val requestObject = TransactionStatusRequest(
            COMMAND_ID_TRANSACTION_STATUS_QUERY,
            partyA,
            organizationType.identifier,
            remarks,
            initiator,
            securityCredential,
            queueTimeOutURL,
            resultURL,
            transactionID,
            occasion,
            originatorConversationId,
        )
        client.post(TRANSACTION_STATUS) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * Account balance - Enquire the balance on an M-Pesa BuyGoods (Till Number)
     *
     * The Account Balance API is used to request the account balance of a short code. This can be used for both B2C,
     * buy goods and pay bill accounts.
     *
     * @param partyA The shortcode of the organization querying for the account balance. Sample: 60072
     * @param organizationType Type of organization receiving the transaction. Sample:
     * [OrganizationType.Shortcode] or [OrganizationType.TillNumber]
     * @param remarks Comments that are sent along with the transaction. Sample: tests
     * @param initiator This is the credential/username used to authenticate the transaction request. Sample: Testapi772
     * @param securityCredential Base64 encoded string of the M-PESA short code and password, which is encrypted using
     * M-PESA public key and validates the transaction on M-PESA Core system. It indicates the Encrypted credential of
     * the initiator getting the account balance. Its value must match the inputted value of the parameter
     * IdentifierType.
     * @param queueTimeOutURL The end-point that receives a timeout message.
     * @param resultUrl It indicates the destination URL which Daraja should send the result message to.
     * @return successful acknowledgement response [AccountBalanceResponse]
     */
    public suspend fun accountBalance(
        partyA: String,
        organizationType: OrganizationType,
        remarks: String,
        initiator: String,
        securityCredential: String,
        queueTimeOutURL: String,
        resultUrl: String,
    ): AccountBalanceResponse = authorized { _, accessToken ->
        val requestObject = AccountBalanceRequest(
            COMMAND_ID_ACCOUNT_BALANCE,
            partyA,
            organizationType.identifier,
            remarks,
            initiator,
            securityCredential,
            queueTimeOutURL,
            resultUrl,
        )
        client.post(ACCOUNT_BALANCE) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    /**
     * Reversal - Reverses a C2B M-Pesa transaction.
     *
     * Once a customer pays and there is a need to reverse the transaction, the organization will use this API to
     * reverse the amount.
     *
     * @param receiverParty The organization that receives the transaction. Sample: Shortcode (6-9 digits)
     * @param receiverOrganizationType organizationType Type of organization receiving the transaction. Sample:
     * [OrganizationType.TillNumber]
     * @param remarks Comments that are sent along with the transaction.
     * @param initiator The name of the initiator to initiate the request.
     * @param securityCredential Encrypted Credential of the user getting transaction amount.
     * @param queueTimeOutUrl The path that stores information of the time-out transaction.
     * @param resultUrl The path that stores information about the transaction.
     * @param transactionId Organization Receiving the funds.
     * @param occasion Optional Parameter. Sample: A sequence of characters up to 100
     * @return successful response [TransactionReversalResponse]
     */
    public suspend fun transactionReversal(
        receiverParty: String,
        receiverOrganizationType: OrganizationType,
        remarks: String,
        initiator: String,
        securityCredential: String,
        queueTimeOutUrl: String,
        resultUrl: String,
        transactionId: String,
        occasion: String,
    ): TransactionReversalResponse = authorized { _, accessToken ->
        val requestObject = ReversalRequest(
            COMMAND_ID_TRANSACTION_REVERSAL,
            receiverParty,
            receiverOrganizationType.identifier,
            remarks,
            initiator,
            securityCredential,
            queueTimeOutUrl,
            resultUrl,
            transactionId,
            occasion,
        )
        client.post(TRANSACTION_REVERSAL) {
            header(Authorization, "Bearer $accessToken")
            setBody(requestObject)
        }.bodyOrThrow()
    }

    private suspend fun <T : Any> authorized(apiCall: suspend (String, String) -> T): T {
        return apiCall.invoke(getDarajaTimestamp(), authorize().accessToken)
    }
}
