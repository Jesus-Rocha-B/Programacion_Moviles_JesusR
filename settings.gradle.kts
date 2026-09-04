pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Laboratorio02"

include(":Rocha")
include(":estacionamiento")
// Excluimos Semana02 para evitar conflictos con su Version Catalog local y configuraciones de Android
