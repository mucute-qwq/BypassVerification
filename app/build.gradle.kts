plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.githun.mucute.qwq.bypassverification"
}

dependencies {
    compileOnly(files("libs/libxposed-api-100.aar"))
}