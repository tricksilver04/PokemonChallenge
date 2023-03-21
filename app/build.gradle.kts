import extensions.androidTestImplementations
import extensions.implementations
import extensions.kapts
import extensions.testImplementations

plugins {
    id(Dependencies.AppPlugins.application)
    id(Dependencies.AppPlugins.jetbrainsKotlinAndroid)
    id(Dependencies.AppPlugins.kotlinKapt)
    id(Dependencies.AppPlugins.hiltAndroid)
    id(Dependencies.AppPlugins.navigationSafeargsKotlin)
}

android {
    namespace = Configurations.namespace
    compileSdk = Configurations.compileSdk

    defaultConfig {
        applicationId = Configurations.applicationId
        minSdk = Configurations.minSdk
        targetSdk = Configurations.targetSdk
        versionCode = Configurations.versionCode
        versionName = Configurations.versionName

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementations(Dependencies.appDependencies)
    kapts(Dependencies.appKaptDependencies)

    testImplementations(Dependencies.testDependencies)
    androidTestImplementations(Dependencies.androidTestDependencies)
}

kapt {
    correctErrorTypes = true
}