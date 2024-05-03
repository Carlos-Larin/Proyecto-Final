plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.ugb.controlesbasicos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ugb.controlesbasicos"
        minSdk = 26
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {

    // Otras dependencias
    implementation (libs.appcompat)
    implementation (libs.material)
    implementation (libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.gridlayout)
    testImplementation (libs.junit)
    androidTestImplementation (libs.ext.junit)
    androidTestImplementation (libs.espresso.core)

    // Dependencias de Firebase
    implementation (platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-messaging")
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-storage")
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-database")
    implementation (libs.firebase.ui.storage)
    //noinspection UseTomlInstead
    implementation (libs.firebase.appcheck.playintegrity) // App Check Play Integrity
}

