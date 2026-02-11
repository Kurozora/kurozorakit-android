package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Defines the intended operation during an OAuth authentication flow.
 *
 * This enum helps the application determine which post-authentication
 * logic to execute after a successful handshake with an external
 * provider (like Apple or Google).
 *
 * @property value The raw string identifier sent to the authentication server.
 */
@Serializable
enum class OAuthAction(val value: String) {
    /** * Standard login for existing users.
     * Directs the flow to the main dashboard or previous screen.
     */
    signIn("signIn"),

    /** * Onboarding flow for new users.
     * Directs the flow to account customization, such as choosing
     * a username, setting interests, or uploading a profile picture.
     */
    setupAccount("setupAccount");
}