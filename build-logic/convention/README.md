# Gradle 约定插件

Gradle 约定插件是重用构建配置的强大工具。它们允许您为项目或模块定义一组约定，然后将这些约定应用于其他项目或模块。这可以极大地简化构建管理并提高效率。

## 文件说明

- **[ApplicationPlugin.kt](src/main/kotlin/ApplicationPlugin.kt)：** 可独立运行模块的插件。
  - 引用方式：`id("griffin.application)`
- **[LibraryPlugin.kt](src/main/kotlin/LibraryPlugin.kt)：** 用于构建库模块的插件。
  - 引用方式：`id("griffin.library)`
- **[FeatureConventionPlugin.kt](src/main/kotlin/FeatureConventionPlugin.kt)** 用于构建业务模块的插件。
  - 引用方式：`id("griffin.feature)`
- **[HiltConventionPlugin.kt](src/main/kotlin/HiltConventionPlugin.kt)** 用于构建 Hilt 模块的插件。
  - 引用方式：`id("griffin.hilt)`