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
        // llama.cpp + whisper.cpp Android prebuilts (to be vendored in /android/libs at on-site)
        flatDir { dirs("libs") }
    }
}
rootProject.name = "dayloop"
include(":app")
