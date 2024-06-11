buildscript {
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
    }
    dependencies {
        classpath(libs.build.gradle)
        classpath(libs.protobuf.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}