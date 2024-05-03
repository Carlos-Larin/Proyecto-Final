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
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    // Dependencias de Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation ("com.google.firebase:firebase-messaging")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation("com.google.firebase:firebase-appcheck")
    implementation  ("com.google.firebase:firebase-appcheck-safetynet:16.0.0")// Agrega esta línea para App Check
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:16.1.1") // Agrega esta línea para App Check
}
