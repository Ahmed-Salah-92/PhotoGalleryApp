// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Kotlin Ksp plugin
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    //  Hilt dependency Injection
    id("com.google.dagger.hilt.android") version "2.57" apply false
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"
    // RoomDB plugin
    id("androidx.room") version "2.7.2" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        // Navigation Safe Args Gradle Plugin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.2")
    }
}