package dev.groovin.teamsexpansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

class TeamsExpansion : PlaceholderExpansion() {
    override fun canRegister() = true

    override fun getName() = "TeamsExpansion"

    override fun getIdentifier() = "team"

    override fun getAuthor() = "Groovin-Dev"

    override fun getVersion() = "0.1-SNAPSHOT"

    override fun getPlaceholders() = listOf("color", "prefix", "suffix", "name", "count_<team>").map(::plc)

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        val playerTeam = getPlayerTeam(player.name)

        return when {

            identifier.contains("color") -> getTeamColor(playerTeam)
            identifier.contains("prefix") -> getTeamPrefix(playerTeam)
            identifier.contains("suffix") -> getTeamSuffix(playerTeam)
            identifier.contains("count") -> getTeamCount(playerTeam)
            identifier.startsWith("count_") -> {
                val teamName = identifier.substringAfter("count_")
                getTeamCount(teamName)
            }
            identifier.contains("name") -> playerTeam
            else -> null
        }
    }

    private fun plc(str: String) = "%${identifier}_$str%"

    companion object {
        private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        private fun getPlayerTeam(player: String): String {
            return scoreboard.getEntryTeam(player)?.name ?: ""
        }

        private fun getTeamColor(teamName: String): String {
            val team = scoreboard.getTeam(teamName) ?: return ""

            return try {
                team.color().toString()
            } catch (e: IllegalStateException) {
                ""
            }
        }

        private fun getTeamPrefix(teamName: String): String {
            val team = scoreboard.getTeam(teamName) ?: return ""
            return team.prefix().toString()
        }

        private fun getTeamSuffix(teamName: String): String {
            val team = scoreboard.getTeam(teamName) ?: return ""
            return team.suffix().toString()
        }

        private fun getTeamCount(teamName: String): String {
            val team = scoreboard.getTeam(teamName) ?: return ""
            return team.entries.size.toString()
        }
    }
}
