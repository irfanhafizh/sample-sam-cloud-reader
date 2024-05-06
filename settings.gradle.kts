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
                username = "jp_b8plm0ddtf7a0kbahnn5gs6ft4"
            }
        }
    }
}

rootProject.name = "Sample Sam Cloud Project"
include(":app")
 