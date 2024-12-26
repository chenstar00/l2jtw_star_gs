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

import java.util.regex.Pattern;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.PatternConverter;

/**
 * Clan Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/clan.properties",
	"file:./config/clan.properties",
	"classpath:config/clan.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface ClanConfiguration extends Reloadable {
	
	@Key("ClanNameTemplate")
	@ConverterClass(PatternConverter.class)
	Pattern getClanNameTemplate();
	
	@Key("TakeFortPoints")
	int getTakeFortPoints();
	
	@Key("TakeCastlePoints")
	int getTakeCastlePoints();
	
	@Key("CastleDefendedPoints")
	int getCastleDefendedPoints();
	
	@Key("FestivalOfDarknessWin")
	int getFestivalOfDarknessWin();
	
	@Key("HeroPoints")
	int getHeroPoints();
	
	@Key("CompleteAcademyMinPoints")
	int getCompleteAcademyMinPoints();
	
	@Key("CompleteAcademyMaxPoints")
	int getCompleteAcademyMaxPoints();
	
	@Key("KillBallistaPoints")
	int getKillBallistaPoints();
	
	@Key("BloodAlliancePoints")
	int getBloodAlliancePoints();
	
	@Key("BloodOathPoints")
	int getBloodOathPoints();
	
	@Key("KnightsEpaulettePoints")
	int getKnightsEpaulettePoints();
	
	@Key("1stRaidRankingPoints")
	int get1stRaidRankingPoints();
	
	@Key("2ndRaidRankingPoints")
	int get2ndRaidRankingPoints();
	
	@Key("3rdRaidRankingPoints")
	int get3rdRaidRankingPoints();
	
	@Key("4thRaidRankingPoints")
	int get4thRaidRankingPoints();
	
	@Key("5thRaidRankingPoints")
	int get5thRaidRankingPoints();
	
	@Key("6thRaidRankingPoints")
	int get6thRaidRankingPoints();
	
	@Key("7thRaidRankingPoints")
	int get7thRaidRankingPoints();
	
	@Key("8thRaidRankingPoints")
	int get8thRaidRankingPoints();
	
	@Key("9thRaidRankingPoints")
	int get9thRaidRankingPoints();
	
	@Key("10thRaidRankingPoints")
	int get10thRaidRankingPoints();
	
	@Key("UpTo50thRaidRankingPoints")
	int getUpTo50thRaidRankingPoints();
	
	@Key("UpTo100thRaidRankingPoints")
	int getUpTo100thRaidRankingPoints();
	
	@Key("ReputationScorePerKill")
	int getReputationScorePerKill();
	
	@Key("LoseFortPoints")
	int getLoseFortPoints();
	
	@Key("LoseCastlePoints")
	int getLoseCastlePoints();
	
	@Key("CreateRoyalGuardCost")
	int getCreateRoyalGuardCost();
	
	@Key("CreateKnightUnitCost")
	int getCreateKnightUnitCost();
	
	// TODO(Zoey76): Implement ReinforceKnightUnitCost configuration.
	@Key("ReinforceKnightUnitCost")
	int getReinforceKnightUnitCost();
	
	@Key("ClanLevel6Cost")
	int getClanLevel6Cost();
	
	@Key("ClanLevel7Cost")
	int getClanLevel7Cost();
	
	@Key("ClanLevel8Cost")
	int getClanLevel8Cost();
	
	@Key("ClanLevel9Cost")
	int getClanLevel9Cost();
	
	@Key("ClanLevel10Cost")
	int getClanLevel10Cost();
	
	@Key("ClanLevel11Cost")
	int getClanLevel11Cost();
	
	@Key("ClanLevel6Requirement")
	int getClanLevel6Requirement();
	
	@Key("ClanLevel7Requirement")
	int getClanLevel7Requirement();
	
	@Key("ClanLevel8Requirement")
	int getClanLevel8Requirement();
	
	@Key("ClanLevel9Requirement")
	int getClanLevel9Requirement();
	
	@Key("ClanLevel10Requirement")
	int getClanLevel10Requirement();
	
	@Key("ClanLevel11Requirement")
	int getClanLevel11Requirement();
}