plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.shinjaehun.winternotesroomcompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shinjaehun.winternotesroomcompose"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // hiltViewModel()
    implementation(libs.hilt.navigation.compose)

    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // coil
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    // datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // rounded image
    implementation("com.makeramen:roundedimageview:2.3.0")


    // for sdp, ssp(근데 이거 없어도 되는거 아냐?)
//    runtimeOnly("com.intuit.sdp:sdp-android:1.1.0")
//    runtimeOnly("com.intuit.ssp:ssp-android:1.1.0")

    // compose ui debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // unit testing
    // 테스트 런타임
//    testImplementation("junit:junit:4.13.2") // 지가 이거 올리라고 했으면서 버전 충돌하니까 지랄은...
    testImplementation(libs.junit)
    // 코루틴 테스트
    testImplementation(libs.kotlinx.coroutines.test) // 최신 버전
    // MockK (Mockito 대신 사용 가능)
    testImplementation(libs.mockk) // 최신 버전
    // AndroidX Core Testing (LiveData/StateFlow 관련)
    testImplementation(libs.androidx.core.testing)

    // ui testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)


}