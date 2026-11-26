package kurozorakit.data.repositories

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.Platform
import kurozorakit.api.RetryConfig
import kurozorakit.shared.UserAgent

internal data class RecordedRequest(
    val method: String,
    val fullUrl: String,
    val path: String,
    val queryParameters: Parameters,
    val contentType: String?,
    val formParameters: Map<String, List<String>>,
    val textBody: String?,
    val isMultipart: Boolean,
)

internal object RepositoryTestHarness {
    const val BASE_URL = "https://api.kurozora.app/v1"
    val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    val binaryHeaders = headersOf(HttpHeaders.ContentType, "application/octet-stream")

    val testPlatform = object : Platform {
        override val platform: String = "Android"
        override val platformVersion: String = "15"
        override val deviceVendor: String = "Google"
        override val deviceModel: String = "Pixel"
    }

    fun record(request: HttpRequestData): RecordedRequest {
        val body: OutgoingContent = request.body
        val formMap = when (body) {
            is FormDataContent -> body.formData.entries().associate { it.key to it.value }
            else -> emptyMap()
        }
        val textBody = when (body) {
            is TextContent -> body.text
            is ByteArrayContent -> body.bytes().decodeToString()
            else -> null
        }
        val contentType = body.contentType?.toString()
            ?: request.headers[HttpHeaders.ContentType]
        return RecordedRequest(
            method = request.method.value,
            fullUrl = request.url.toString(),
            path = request.url.encodedPath,
            queryParameters = request.url.parameters,
            contentType = contentType,
            formParameters = formMap,
            textBody = textBody,
            isMultipart = body is MultiPartFormDataContent,
        )
    }

    fun createClient(
        recorded: MutableList<RecordedRequest>,
        responder: suspend MockRequestHandleScope.(HttpRequestData) -> io.ktor.client.request.HttpResponseData,
    ): KurozoraApiClient {
        val engine = MockEngine { request ->
            recorded += record(request)
            responder(request)
        }
        return KurozoraApiClient(
            baseUrl = BASE_URL,
            apiKey = "test-api-key",
            platform = testPlatform,
            userAgent = UserAgent(),
            retryConfig = RetryConfig(maxRetries = 0),
            engine = engine,
        )
    }

    fun MockRequestHandleScope.respondJson(json: String, status: HttpStatusCode = HttpStatusCode.OK) =
        respond(content = json, status = status, headers = jsonHeaders)

    fun MockRequestHandleScope.respondBytes(bytes: ByteArray, status: HttpStatusCode = HttpStatusCode.OK) =
        respond(content = bytes, status = status, headers = binaryHeaders)
}
