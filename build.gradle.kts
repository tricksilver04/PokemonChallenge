// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(Dependencies.GradlePlugin.navigationSafeArgsGradle)
        classpath(Dependencies.GradlePlugin.hiltAndroid)
    }
}
plugins {
    id(Dependencies.GradlePlugin.application).version(Versions.application).apply(false)
    id(Dependencies.GradlePlugin.androidLibrary).version(Versions.androidLibrary).apply(false)
    id(Dependencies.GradlePlugin.kotlinAndroid).version(Versions.kotlinAndroid).apply(false)
}