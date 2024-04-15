package dev.groovin.teamsexpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class TeamsExpansion : PlaceholderExpansion() {
    override fun canRegister(): Boolean = true
    override fun getName(): String = "TeamsExpansion"
    override fun getIdentifier(): String = "team"
    override fun getAuthor(): String = "Groovin-Dev"
    override fun getVersion(): String = "0.1-SNAPSHOT"
    override fun getPlaceholders(): List<String> = listOf("color", "prefix", "suffix", "name", "count", "count_<team>").map { "%${identifier}_$it%" }

    override fun onPlaceholderRequest(player: Player, identifier: String): String {
        val playerTeam = player.name.getPlayerTeam() ?: return ""

        return when {
            "color" in identifier -> playerTeam.getTeamColor()
            "prefix" in identifier -> playerTeam.getTeamPrefix()
            "suffix" in identifier -> playerTeam.getTeamSuffix()
            identifier.startsWith("count_") -> {
                val teamName = identifier.substringAfter("count_")
                scoreboard.getTeam(teamName)?.getTeamCount() ?: ""
            }
            "count" in identifier -> playerTeam.getTeamCount()
            "name" in identifier -> playerTeam.name
            else -> ""
        }
    }

    companion object {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}

private fun String.getPlayerTeam(): Team? = TeamsExpansion.scoreboard.getEntryTeam(this)

private fun Team.getTeamColor(): String = runCatching { color().toString() }.getOrDefault("")

private fun Team.getTeamPrefix(): String = prefix().getPlainText()

private fun Team.getTeamSuffix(): String = suffix().getPlainText()

private fun Team.getTeamCount(): String = entries.size.toString()

private fun Component.getPlainText(): String = (this as? TextComponent)?.content() ?: ""