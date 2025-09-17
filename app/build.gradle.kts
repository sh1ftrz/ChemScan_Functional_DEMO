plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chemapp30"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.chemapp30"
        minSdk = 24
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

    // ใช้ Java 11 (รองรับ Room และฟีเจอร์ใหม่ๆ)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ====== AndroidX / UI พื้นฐาน ======
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ====== Room Database ======
    // Runtime + Compiler (สำคัญสำหรับให้ Room สร้าง AppDatabase_Impl)
    implementation("androidx.room:room-runtime:2.7.0-alpha05")
    annotationProcessor("androidx.room:room-compiler:2.7.0-alpha05")

    // ===== ZXing (QR Scanner) =====
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.5.3")

    // ====== Test ======
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
