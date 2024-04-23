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
                username = "jp_3leodokqr664k9udcb4srf4jmv"
                // Add password if required
                // password = "your_password_here"
            }
        }
    }
}

rootProject.name = "Sample Sam Cloud Project"
include(":app")
 