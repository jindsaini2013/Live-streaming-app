plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.live_streaming"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.live_streaming"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    buildTypes {
        release {
            // Enable code shrinking and obfuscation
            isMinifyEnabled = true
            // Enable resource shrinking to remove unused resources
            isShrinkResources = true
            // Use default ProGuard rules plus your custom rules file
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Disable debugging for release build
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.coordinatorlayout)
    implementation(libs.fragment)

    // VLC for streaming
    implementation(libs.libvlc)

    // Navigation components
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Lifecycle components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Room database
    implementation(libs.room.runtime)
    implementation(libs.firebase.auth)
    annotationProcessor(libs.room.compiler)

    // UI components
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.viewpager2)

    // Image loading
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // HTTP Client for API calls
    implementation(libs.okhttp)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
