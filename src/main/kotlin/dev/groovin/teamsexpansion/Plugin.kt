package dev.groovin.teamsexpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

// I added this because ChatColor is deprecated but this code still works
const val COLOR_CHAR = '\u00A7'

class TeamsExpansion : PlaceholderExpansion() {
    override fun canRegister(): Boolean = true
    override fun getName(): String = "TeamsExpansion"
    override fun getIdentifier(): String = "team"
    override fun getAuthor(): String = "Groovin-Dev"
    override fun getVersion(): String = "0.3-SNAPSHOT"
    override fun getPlaceholders(): List<String> = listOf("color", "prefix", "suffix", "name", "count", "count_<team>").map { "%${identifier}_$it%" }

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        val teamName = params.substringAfter("_")
        val team = if (params.startsWith("count_")) {
            scoreboard.getTeam(teamName)
        } else {
            player?.let { scoreboard.getEntryTeam(it.name) } ?: scoreboard.getTeam(teamName)
        }

        if (team == null) {
            return ""
        }

        return when {
            params.startsWith("color") -> team.getTeamColor()
            params.startsWith("prefix") -> team.getTeamPrefix()
            params.startsWith("suffix") -> team.getTeamSuffix()
            params == "count" -> team.getTeamCount()
            params.startsWith("count_") -> team.getTeamCount()
            params == "name" -> team.name
            else -> ""
        }
    }

    companion object {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}

private fun Team.getTeamColor(): String = runCatching { color().toString() }.getOrDefault("")

private fun Team.getTeamPrefix(): String = LegacyComponentSerializer.legacy(COLOR_CHAR).serialize(prefix())

private fun Team.getTeamSuffix(): String = LegacyComponentSerializer.legacy(COLOR_CHAR).serialize(suffix())

private fun Team.getTeamCount(): String = entries.size.toString()