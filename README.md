# TeamsExpansion

Adds `6` new PlaceholderAPI expansions:
- %team_color%
- %team_prefix%
- %team_suffix%
- %team_count%
- %team_displayname%
- %team_name%

All placeholders accept an optional team name parameter,
for example `%team_color_test%` will return the color of the team named `test`.

The team name parameter takes precedence over the player's team.

# How to build

1. Clone the repository
2. Run the `shadowJar` gradle task
3. The jar will be in `build/libs/teamsexpansion.jar`

# Original

This repo is based on aother: https://github.com/Paul19988/TeamsExpansion
