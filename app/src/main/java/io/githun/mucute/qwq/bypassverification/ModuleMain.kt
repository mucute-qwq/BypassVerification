package io.githun.mucute.qwq.bypassverification

import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

class ModuleMain(base: XposedInterface, param: XposedModuleInterface.ModuleLoadedParam) :
    XposedModule(base, param) {

    class Hooker : XposedInterface.Hooker {

        companion object {

            @JvmStatic
            fun before(callback: XposedInterface.BeforeHookCallback) {
                callback.returnAndSkip(true)
            }

        }

    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        super.onPackageLoaded(param)
        if (param.isFirstPackage) {
            val googlePlayStoreClass = param.classLoader.loadClass("com.mojang.minecraftpe.store.googleplay.GooglePlayStore")
            val hasVerifiedLicense = googlePlayStoreClass.getDeclaredMethod("hasVerifiedLicense")
            hasVerifiedLicense.isAccessible = true

            hook(hasVerifiedLicense, Hooker::class.java)
        }
    }

}