plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Kotlin simple processing (KSP) plugin
    id("com.google.devtools.ksp")
    // Hilt dependency Injection plugin
    id("com.google.dagger.hilt.android")
    // Kotlin Kapt plugin
    id("kotlin-kapt")
    // Safe args plugin Kotlin-Only
    id("androidx.navigation.safeargs.kotlin")
    // Room Database plugin
    id("androidx.room")
}

android {
    namespace = "com.ragdoll.photogalleryapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ragdoll.photogalleryapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        jvmToolchain(17)
        compilerOptions {
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }

//    kotlin {
//        jvmToolchain(11)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11) // Or JVM_11, JVM_17 etc.
//            // You can add other compiler options here if needed
//            optIn.add("kotlin.RequiresOptIn")
//        }
//    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

ksp {
    arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
    arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
    arg("dagger.fastInit", "ENABLED")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

dependencies {
    // Project dependencies
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    // Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    // OkHttp Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")
    // Serialized Name
    implementation("com.google.code.gson:gson:2.13.1")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Hilt Dependency Injection
    implementation("com.google.dagger:hilt-android:2.57")
    // Hilt KSP compiler
    ksp("com.google.dagger:hilt-compiler:2.57")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
    // Annotation processor
    kapt("androidx.lifecycle:lifecycle-compiler:2.9.2")

    // SwipeRefreshLayout (pull down swipe-to-refresh)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-beta01")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:2.9.2")
    implementation("androidx.navigation:navigation-ui:2.9.2")

    // Room Database
    implementation("androidx.room:room-runtime:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")

    // Paging 3 library
    implementation("androidx.paging:paging-runtime:3.3.6")

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}