plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinKSP)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.sostudio.apptemplate"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.sostudio.apptemplate"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Production API. The `staging` build type overrides this to point at a dev backend.
        buildConfigField("String", "API_HOST", "\"https://api.example.com/\"")

        // Sentry DSN — not committed. Set SENTRY_DSN in ~/.gradle/gradle.properties (or as an
        // env var). Empty is fine: SentryAndroid.init in AppTemplateApp no-ops on a blank DSN,
        // so the app still builds and runs for anyone without it configured.
        val sentryDsn = (project.findProperty("SENTRY_DSN") as String?) ?: System.getenv("SENTRY_DSN") ?: ""
        buildConfigField("String", "SENTRY_DSN", "\"$sentryDsn\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
        // Parallel dev environment: its own applicationId (`.staging` suffix) so it installs
        // alongside the prod build, pointed at an isolated dev backend. See README for the
        // matching `.staging`-suffixed client in google-services.json.
        create("staging") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            isDebuggable = true
            buildConfigField("String", "API_HOST", "\"https://dev-api.example.com/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Retrofit
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit)
    implementation(libs.retrofit.serializationConverter)

    //kotlinx serialization
    implementation(libs.kotlinx.serialization)

    //sandwich
    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)

    //Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)

    //Voyager
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenModel)
    implementation(libs.voyager.tabNavigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)

    //Security — encrypted auth-token storage
    implementation(libs.androidx.security.crypto)

    //okhttpprofiler - debug network inspector, gated at runtime by BuildConfig.DEBUG
    implementation(libs.okhttpprofiler)

    //Coil - avatar image loading
    implementation(libs.coil.compose)

    //Room - local single-user cache
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Custom Tabs for legal pages (FAQ/Privacy/Terms)
    implementation(libs.androidx.browser)

    //Push notifications — Firebase Cloud Messaging, matching iOS's FirebaseMessaging
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    //Lottie — Splash + shared LoadingView branded animation, matching iOS's Lottie usage
    implementation(libs.lottie.compose)

    //core-splashscreen — controls the OS-mandated Android 12+ Starting Window so it hands off
    //to our own Lottie splash immediately, instead of feeling like a second splash screen.
    implementation(libs.androidx.core.splashscreen)

    //Biometric app lock — androidx.biometric's BiometricPrompt is the direct analog of iOS's
    //LAContext; lifecycle-process backs BiometricLockManager's app-wide foreground/background
    //detection, matching iOS's scenePhase being app-wide too.
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.androidx.lifecycle.process)

    //Sentry — crash/error reporting, manual init in AppTemplateApp.
    implementation(libs.sentry.android)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

kotlin {
    jvmToolchain(11)
}
