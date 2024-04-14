package dev.groovin.teamsexpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team


class TeamsExpansion : PlaceholderExpansion() {
    override fun canRegister() = true

    override fun getName() = "TeamsExpansion"

    override fun getIdentifier() = "team"

    override fun getAuthor() = "Groovin-Dev"

    override fun getVersion() = "0.1-SNAPSHOT"

    override fun getPlaceholders() = listOf("color", "prefix", "suffix", "name", "count_<team>").map(::plc)

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        val playerTeam = getPlayerTeam(player.name) ?: return null

        return when {
            identifier.contains("color") -> getTeamColor(playerTeam)
            identifier.contains("prefix") -> getTeamPrefix(playerTeam)
            identifier.contains("suffix") -> getTeamSuffix(playerTeam)
            identifier.contains("count") -> getTeamCount(playerTeam.name)
            identifier.startsWith("count_") -> {
                val teamName = identifier.substringAfter("count_")
                getTeamCount(teamName)
            }
            identifier.contains("name") -> playerTeam.name
            else -> null
        }
    }

    private fun plc(str: String) = "%${identifier}_$str%"

    companion object {
        private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        private fun getPlayerTeam(player: String): Team? {
            return scoreboard.getEntryTeam(player);
        }

        private fun getTeamColor(team: Team): String {
            return try {
                team.color().toString()
            } catch (e: IllegalStateException) {
                ""
            }
        }

        private fun getTeamPrefix(team: Team): String {
            return getPlainText(team.prefix())
        }

        private fun getTeamSuffix(team: Team): String {
            return getPlainText(team.suffix())
        }

        private fun getTeamCount(teamName: String): String {
            val team = scoreboard.getTeam(teamName) ?: return "0"
            return team.entries.size.toString()
        }
        
        private fun getPlainText(component: Component): String {
            return (component as TextComponent).content()
        }
    }
}
