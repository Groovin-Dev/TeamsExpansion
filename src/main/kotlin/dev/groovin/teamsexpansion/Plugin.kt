package dev.groovin.teamsexpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

const val COLOR_CHAR = '\u00A7'

class TeamsExpansion : PlaceholderExpansion() {
    override fun canRegister(): Boolean = true
    override fun getIdentifier(): String = "team"
    override fun getAuthor(): String = "Groovin-Dev"
    override fun getVersion(): String = javaClass.getResource("/version.txt")?.readText() ?: "Unknown"
    override fun getPlaceholders(): List<String> = generatePlaceholders()

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        val parts = params.split("_", limit = 2)
        val placeholder = parts.getOrNull(0).orEmpty()
        val teamName = parts.getOrNull(1).orEmpty()
        
        val team = getTeam(player, teamName)

        return when (placeholder) {
            "color" -> team?.getTeamColor().orEmpty()
            "prefix" -> team?.getTeamPrefix().orEmpty()
            "suffix" -> team?.getTeamSuffix().orEmpty()
            "count" -> team?.getTeamCount().orEmpty()
            "name" -> team?.name.orEmpty()
            "displayname" -> team?.getDisplayName.orEmpty()
            else -> ""
        }
    }

    private fun generatePlaceholders(): List<String> {
        val placeholderTypes = listOf("color", "prefix", "suffix", "count", "name", "displayname")
        val teams = scoreboard.teams.map { it.name }

        val placeholders = mutableListOf<String>()
        for (placeholderType in placeholderTypes) {
            placeholders.add("%${identifier}_$placeholderType%")
            for (team in teams) {
                placeholders.add("%${identifier}_${placeholderType}_$team%")
            }
        }
        return placeholders
    }

    private fun getTeam(player: OfflinePlayer?, teamName: String): Team? {
        return when {
            teamName.isNotEmpty() -> scoreboard.getTeam(teamName)
            player != null -> player.name?.let { scoreboard.getEntryTeam(it) }
            else -> null
        }
    }

    companion object {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}

private fun Team.getTeamColor(): String? = runCatching { color().toString() }.getOrNull()

private fun Team.getTeamPrefix(): String = prefix().let { LegacyComponentSerializer.legacy(COLOR_CHAR).serialize(it) }

private fun Team.getTeamSuffix(): String = suffix().let { LegacyComponentSerializer.legacy(COLOR_CHAR).serialize(it) }

private fun Team.getTeamCount(): String = entries.size.toString()

private val Team.getDisplayName: String
    get() = displayName().let { LegacyComponentSerializer.legacy(COLOR_CHAR).serialize(it) }