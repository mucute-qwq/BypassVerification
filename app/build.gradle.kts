plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.githun.mucute.qwq.bypassverification"
}

dependencies {
    compileOnly(libs.libxposed.api)
}