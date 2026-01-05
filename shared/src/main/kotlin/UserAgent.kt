package kurozorakit.shared

class UserAgent (
    private val appName: String = "",
    private val appVersion: String = "",
    private val appID: String = "",
    private val platformName: String = "",
    private val platformVersion: String = "",
) {

    fun getAppName(): String{
        return appName
    }

    fun getAppVersion(): String {
        return appVersion
    }
    fun getAppID(): String {
        return appID
    }

    fun getPlatformName(): String {
        return platformName
    }

    fun getPlatformVersion(): String {
        return platformVersion
    }
}