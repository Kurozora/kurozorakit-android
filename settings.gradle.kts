plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "kurozorakit"

include("cache")
include("core")
include("api")
include("data")
include("shared")
