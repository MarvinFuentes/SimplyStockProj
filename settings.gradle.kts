pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //Modern equivalent of older allprojects { repositories{...}} in build.gradle.kts (Project: SimplyStockProj)
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "SimplyStockProj"
include(":app")
 