import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Kotlin Kapt plugin
    id("kotlin-kapt")
    // Kotlin Symbol Processing plugin
    id("com.google.devtools.ksp")
    // Safe args plugin Kotlin-Only
    id("androidx.navigation.safeargs.kotlin")
}

// Load secrets.properties file
val secretPropertiesFile = rootProject.file("secrets.properties") // Fixed path to the correct location
val secretsProperties = Properties()
if (secretPropertiesFile.exists())
    secretsProperties.load(secretPropertiesFile.inputStream())
else    // If the secrets.properties file does not exist, log a warning
    println("Warning!!: secrets.properties file not found. Using empty properties.")

android {
    namespace = "com.ragdoll.photogalleryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ragdoll.photogalleryapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Access the property
        val pexelsApiKey = secretsProperties.getProperty("PEXELS_API_KEY")
            ?: "" // Default to empty string if not found
        buildConfigField("String", "PEXELS_API_KEY", "\"$pexelsApiKey\"")

    }

    buildTypes {
        debug {
            // Debug-specific configurations
            // You can also define it per build type if needed
            // val pexelsApiKeyDebug = secretsProperties.getProperty("PEXELS_API_KEY_DEBUG") ?: pexelsApiKey
            // resValue("string", "pexels_api_key", "\"$pexelsApiKeyDebug\"") // Using resValue
            // buildConfigField("String", "PEXELS_API_KEY", "\"$pexelsApiKeyDebug\"")  // Using buildConfigField
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // For release, you might also want to ensure the API key is set
            // val pexelsApiKeyRelease = secretsProperties.getProperty("PEXELS_API_KEY_RELEASE") ?: pexelsApiKey
            // buildConfigField("String", "PEXELS_API_KEY", "\"$pexelsApiKeyRelease\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")

    // Retrofit & Network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room Database
    implementation("androidx.room:room-paging:2.7.0")
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")

    // Image Loading
    implementation("io.coil-kt:coil:2.7.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Dependency Injection
    implementation("io.insert-koin:koin-android:4.1.0")

    // Swipe To Refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}