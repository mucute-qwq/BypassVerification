import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    id("build-logic.root-project")
    alias(libs.plugins.android.application) apply false
}

fun Project.configureApplicationExtension() {
    extensions.findByType<ApplicationExtension>()?.apply {
        configureCommonExtension()

        defaultConfig.apply {
            applicationId = namespace

            targetSdk {
                version = release(Versions.targetSdkVersion)
            }

            versionCode = Versions.versionCode
            versionName = Versions.versionName
        }

        signingConfigs {
            create("shared") {
                storeFile = file("../buildKey.jks")
                storePassword = "123456"
                keyAlias = "BypassVerification"
                keyPassword = "123456"
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                signingConfig = signingConfigs["shared"]
                proguardFiles("proguard-rules.pro")
            }
            getByName("debug") {
                isMinifyEnabled = false
                signingConfig = signingConfigs["shared"]
                proguardFiles("proguard-rules.pro")
            }
        }

        packaging {
            resources {
                excludes += setOf("DebugProbesKt.bin")
            }
        }
    }
}

fun Project.configureCommonExtension() {
    extensions.findByType(CommonExtension::class)?.apply {
        compileSdk {
            version = release(Versions.compileSdkVersion)
        }

        buildToolsVersion = Versions.buildToolsVersion

        defaultConfig.apply {
            minSdk {
                version = release(Versions.minSdkVersion)
            }
        }

        compileOptions.apply {
            sourceCompatibility = Versions.javaVersion
            targetCompatibility = Versions.javaVersion
        }
    }
}

fun Project.configureKotlinAndroidExtension() {
    extensions.findByType(KotlinAndroidProjectExtension::class)?.apply {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xcontext-parameters"
                )
            )
        }
        jvmToolchain(Versions.jvmToolchain)
    }
}

subprojects {
    plugins.withId("com.android.application") {
        configureApplicationExtension()
    }
    plugins.withId("com.android.library") {
        configureCommonExtension()
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        configureKotlinAndroidExtension()
    }
}