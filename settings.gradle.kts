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
    }
}

rootProject.name = "Abarrote2"
include(":app")
include(":data")
include(":repository")
include(":network")
include(":domain-model")
include(":domain-usecases")
include(":domain-impl")
include(":domain-repository")
