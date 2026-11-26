plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

val generatedVersionDir = layout.buildDirectory.dir("generated/version")

val generateVersion = tasks.register("generateVersion") {
    inputs.property("version", rootProject.version)
    outputs.dir(generatedVersionDir)
    doLast {
        val file = generatedVersionDir.get().file("KurozoraKitVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package kurozorakit.core

            /**
             * Library version, injected from the Gradle build at compile time.
             */
            internal const val KUROZORA_KIT_VERSION: String = "${rootProject.version}"
            """.trimIndent() + "\n"
        )
    }
}

kotlin.sourceSets.main {
    kotlin.srcDir(generatedVersionDir)
}

tasks.named("compileKotlin") {
    dependsOn(generateVersion)
}

// Dokka and the sources jar read the generated source directory, so they must
// depend on the task that creates it. Task names are matched case-insensitively
// because Dokka generates names like `dokkaHtml` and `dokkaJavadocJar`.
tasks.matching {
    it.name.startsWith("dokka", ignoreCase = true) || it.name.contains("sourcesJar", ignoreCase = true)
}.configureEach {
    dependsOn(generateVersion)
}

dependencies {
    api(project(":domain"))
    api(project(":api"))
    api(project(":shared"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.dateTime)
    implementation(Deps.Ktor.core)
    implementation(Deps.Ktor.mock)

    testImplementation(Deps.Test.coroutinesTest)
}
