/*
 * Copyright © 2004-2024 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.config;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.aeonbits.owner.Config.HotReloadType.ASYNC;
import static org.aeonbits.owner.Config.LoadType.MERGE;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

/**
 * Territory War Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/territorywar.properties",
	"file:./config/territorywar.properties",
	"classpath:config/territorywar.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface TerritoryWarConfiguration extends Reloadable {
	
	// TODO(Zoey76): Convert this to milliseconds.
	@Key("WarLength")
	long getWarLength();
	
	// TODO(Zoey76): Implement ClanMinLevel configuration.
	@Key("ClanMinLevel")
	int getClanMinLevel();
	
	@Key("PlayerMinLevel")
	int getPlayerMinLevel();
	
	// TODO(Zoey76): Implement DefenderMaxClans configuration.
	@Key("DefenderMaxClans")
	int getDefenderMaxClans();
	
	// TODO(Zoey76): Implement DefenderMaxPlayers configuration.
	@Key("DefenderMaxPlayers")
	int getDefenderMaxPlayers();
	
	@Key("PlayerWithWardCanBeKilledInPeaceZone")
	boolean playerWithWardCanBeKilledInPeaceZone();
	
	@Key("SpawnWardsWhenTWIsNotInProgress")
	boolean spawnWardsWhenTWIsNotInProgress();
	
	@Key("ReturnWardsWhenTWStarts")
	boolean returnWardsWhenTWStarts();
	
	@Key("MinTerritoryBadgeForNobless")
	int getMinTerritoryBadgeForNobless();
	
	@Key("MinTerritoryBadgeForStriders")
	int getMinTerritoryBadgeForStriders();
	
	@Key("MinTerritoryBadgeForBigStrider")
	int getMinTerritoryBadgeForBigStrider();
}