package kurozorakit.core.mock

import io.ktor.http.Parameters
import kurozorakit.api.Platform

/**
 * Snapshot of a request intercepted by [KurozoraMockEngine], useful for assertions and debugging.
 *
 * @property method HTTP verb (e.g., `GET`, `POST`, `DELETE`).
 * @property path Normalized API path relative to `/v1/` (e.g., `anime/1/cast`).
 * @property queryParameters Query string parameters sent on the request.
 * @property formParameters Parsed `application/x-www-form-urlencoded` form fields, if any.
 * @property textBody Raw request body text for JSON/text payloads, if any.
 * @property headers HTTP request headers.
 * @property isAuthenticated `true` if a `Bearer` token was present in `Authorization`.
 */
data class MockRequestContext(
    val method: String,
    val path: String,
    val queryParameters: Parameters,
    val formParameters: Map<String, List<String>>,
    val textBody: String?,
    val headers: Map<String, List<String>>,
    val isAuthenticated: Boolean,
) {
    /** Convenience accessor for the first value of a form field. */
    fun formParam(name: String): String? = formParameters[name]?.firstOrNull()
}

/**
 * Response returned by a [MockRouteHandler].
 *
 * @property statusCode HTTP status code (default `200`).
 * @property body JSON or text response body (ignored when [binaryBody] is non-null).
 * @property contentType MIME type header (default `"application/json"`).
 * @property binaryBody Optional raw binary payload (for theme downloads, `.ics` exports, etc.).
 */
data class MockRouteResponse(
    val statusCode: Int = 200,
    val body: String = "{}",
    val contentType: String = "application/json",
    val binaryBody: ByteArray? = null,
)

/**
 * Custom route override handler invoked by [KurozoraMockEngine] before default simulation routes.
 */
fun interface MockRouteHandler {
    /** Produces a [MockRouteResponse] for the given [request] and mutable [state]. */
    suspend fun handle(request: MockRequestContext, state: KurozoraMockState): MockRouteResponse
}

/**
 * Configuration DSL for [kurozorakit.core.KurozoraKit.Builder.mockMode].
 *
 * Enables simulating realistic latency, chaos/error injection, HTTP status overrides per route,
 * authentication enforcement, 2FA sign-in flows, and custom per-route mock responses.
 */
class KurozoraMockConfig {
    /** Artificial base network delay in milliseconds applied to every simulated request. */
    var latencyMs: Long = 0L

    /** Optional random additional delay (`0..jitterMs`) to simulate real-world network variance. */
    var jitterMs: Long = 0L

    /** Probability (`0.0..1.0`) of randomly failing a request with HTTP `500` for resilience testing. */
    var errorRate: Double = 0.0

    /**
     * When `true`, requests to `me` and authenticated mutations require an `Authorization: Bearer ...`
     * header and return HTTP `401` otherwise (allowing you to test sign-in & `onSessionExpired` flows).
     */
    var requireAuthForMeEndpoints: Boolean = false

    /**
     * When `true`, `auth().signIn(...)` returns [kurozorakit.data.models.misc.SignInResult.RequiresTwoFactor]
     * with a mock challenge token that can be resolved via `auth().submitTwoFactorChallenge(...)`.
     */
    var requireTwoFactorOnSignIn: Boolean = false

    /**
     * Optional pre-configured [KurozoraMockState] instance. If omitted, a fresh seeded state is created.
     */
    var state: KurozoraMockState? = null

    /** Path substring -> HTTP status code overrides evaluated before normal mock routing. */
    val statusOverrides: MutableMap<String, Int> = mutableMapOf()

    /** Path substring -> custom [MockRouteHandler] overrides evaluated before normal mock routing. */
    val customHandlers: MutableMap<String, MockRouteHandler> = mutableMapOf()

    /**
     * Forces any request whose relative path contains [pathContains] to respond with [statusCode].
     * Useful for testing 401/403/404/429/500 UI states.
     */
    fun overrideStatus(pathContains: String, statusCode: Int) {
        statusOverrides[pathContains] = statusCode
    }

    /**
     * Registers a custom [handler] for any request whose relative path contains [pathContains].
     */
    fun onRoute(pathContains: String, handler: MockRouteHandler) {
        customHandlers[pathContains] = handler
    }
}

/**
 * Default [Platform] metadata used automatically when [kurozorakit.core.KurozoraKit.Builder.mockMode]
 * is enabled without an explicit `platform(...)` call.
 */
object MockPlatform : Platform {
    override val platform: String = "Android"
    override val platformVersion: String = "15"
    override val deviceVendor: String = "KurozoraMock"
    override val deviceModel: String = "Simulator"
}
