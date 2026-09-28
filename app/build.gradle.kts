plugins {
    alias(libs.plugins.android.application)
    id("androidx.navigation.safeargs") version "2.9.8"
}

android {
    namespace = "com.example.prueba"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.prueba"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Room (base de datos)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // Fragments
    implementation("androidx.fragment:fragment:1.6.2")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

//    // ConstraintLayout
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}