pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") {
            credentials {
                username = "jp_glrv4g3500cbr1opbi6uulj0mr"
            }
        }
    }
}

rootProject.name = "Sample Sam Cloud Project"
include(":app")
 