package kurozorakit.shared

/**
 * Represents the structured user agent information for the client application.
 *
 * This class is used to identify the client making requests to the Kurozora API.
 * The information is typically formatted into a single string and sent via the
 * `User-Agent` HTTP header to assist in debugging, analytics, and platform-specific logic.
 *
 * @property appName The display name of the client application (e.g., "KurozoraApp").
 * @property appVersion The semantic version of the application (e.g., "2.1.0").
 * @property appID The unique bundle identifier or package name (e.g., "com.kurozora.android").
 * @property platformName The name of the operating system (e.g., "Android", "iOS").
 * @property platformVersion The version of the operating system (e.g., "14.0").
 */
class UserAgent (
    private val appName: String = "",
    private val appVersion: String = "",
    private val appID: String = "",
    private val platformName: String = "",
    private val platformVersion: String = "",
) {

    /**
     * Retrieves the name of the application.
     * @return The application name string.
     */
    fun getAppName(): String {
        return appName
    }

    /**
     * Retrieves the version of the application.
     * @return The application version string.
     */
    fun getAppVersion(): String {
        return appVersion
    }

    /**
     * Retrieves the unique application identifier (Bundle ID / Package Name).
     * @return The application ID string.
     */
    fun getAppID(): String {
        return appID
    }

    /**
     * Retrieves the name of the operating system platform.
     * @return The platform name string.
     */
    fun getPlatformName(): String {
        return platformName
    }

    /**
     * Retrieves the version of the operating system platform.
     * @return The platform version string.
     */
    fun getPlatformVersion(): String {
        return platformVersion
    }
}