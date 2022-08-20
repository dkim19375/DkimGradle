package me.dkim19375.dkimgradle.enums

enum class PaperVersion(val groupID: String, val artifactID: String) {
    BELOW_1_9("org.github.paperspigot", "paperspigot-api"),
    BELOW_1_17("com.destroystokyo.paper", "paper-api"),
    REST("io.papermc.paper", "paper-api"),
}