import com.android.build.api.dsl.LibraryExtension
import com.griffin.plugin.configureLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

/**
 * 模块的插件
 *
 * @author 高国峰
 * @date 2023/9/16-11:48
 */
class LibraryPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            // 获取libs版本管理
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            // 插件管理器
            with(pluginManager){
                apply(libs.getPlugin("android-library"))
                apply(libs.getPlugin("android-kotlin-ksp"))
                apply(libs.getPlugin("jetbrains-kotlin-android"))
                apply(libs.getPlugin("jetbrains-kotlin-serialization"))
            }

            extensions.configure<LibraryExtension>{
                configureLibrary(libs,this)
            }
        }
    }

    private fun VersionCatalog.getPlugin(name: String) = this.findPlugin(name).get().get().pluginId
}