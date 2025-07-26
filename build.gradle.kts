// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Kotlin Ksp plugin
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    // Kotlin serialization safe arg plugin
    kotlin("plugin.serialization") version "2.0.21"
    // RoomDB plugin
    id("androidx.room") version "2.7.0" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        // Navigation Safe Args Gradle Plugin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.0")
    }
}