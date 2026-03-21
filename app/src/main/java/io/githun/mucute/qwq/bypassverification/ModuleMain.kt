package io.githun.mucute.qwq.bypassverification

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

class ModuleMain : XposedModule() {

    companion object {
        const val TAG = "BypassVerification"
    }

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        super.onModuleLoaded(param)
        Log.d(TAG, "Module loaded: ${param.processName}")
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        Log.d(TAG, "Package loaded: ${param.packageName}")
    }

    override fun onPackageReady(param: XposedModuleInterface.PackageReadyParam) {
        Log.d(TAG, "Package ready: ${param.packageName}")

        if (!param.isFirstPackage) return

        val activityClass = Class.forName("android.app.Activity")
        val onCreateMethod = activityClass.getDeclaredMethod("onCreate", Bundle::class.java)

        val googlePlayStoreClass = Class.forName("com.mojang.minecraftpe.store.googleplay.GooglePlayStore", true, param.classLoader)
        val hasVerifiedLicenseMethod = googlePlayStoreClass.getDeclaredMethod("hasVerifiedLicense")

        hook(onCreateMethod)
            .intercept {
                val activity = it.thisObject as Activity
                if (activity.javaClass.name != "com.mojang.minecraftpe.MainActivity") return@intercept it.proceed()

                Toast.makeText(activity, "${TAG}: $activity", Toast.LENGTH_SHORT)
                    .show()

                return@intercept it.proceed()
            }

        hook(hasVerifiedLicenseMethod)
            .intercept {
                return@intercept true
            }

    }

    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        Log.d(TAG, "System service starting")
    }

}