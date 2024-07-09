import java.util.Properties
import java.io.FileInputStream

plugins {

    id ("kotlin-android")
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")


}


android {

    namespace = "com.example.modularapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.modularapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))
        }
        val apiKey = properties.getProperty("apiKey") ?: "default_api_key"

        buildConfigField("String","API_KEY", apiKey)
        vectorDrawables {
            useSupportLibrary = true
        }
        kapt {
            arguments{
                arg("room.schemaLocation","$projectDir/schemas")
            }
        }
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
            abiFilters.add("armeabi-v7a")
        }
        splits {
            abi {
                isEnable  = true
                reset()

                include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
                isUniversalApk  = true
            }
        }
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
    buildFeatures {
        compose = true
        buildConfig = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("com.google.truth:truth:1.1.3")

    implementation("com.mpatric:mp3agic:0.9.1")


    implementation (libs.androidx.room.ktx)
    kapt (libs.androidx.room.compiler)//figure out how to change this to use ksp later, was getting build errors so reverted

    implementation (libs.androidx.hilt.navigation.compose)

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.javax.inject)
    implementation(libs.media3.datasource.okhttp)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)



    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.compose)

    implementation(libs.gson)
    implementation(libs.libraryv)
    implementation(libs.ffmpeg)
    //implementation(libs.aria2c)
    implementation (libs.rxjava2.rxjava)
    implementation (libs.rxjava2.rxandroid)
    implementation(project(":network"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation (libs.androidx.material.icons.extended)
    implementation (libs.androidx.lifecycle.viewmodel.compose)


    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}