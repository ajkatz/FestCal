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

rootProject.name = "FestCal"
include(":app")
include(":schedule-core")
include(":colors")
include(":platform")
project(":colors").projectDir = file("../../AndroidKotlinCommon/colors")
project(":platform").projectDir = file("../../AndroidKotlinCommon/platform")