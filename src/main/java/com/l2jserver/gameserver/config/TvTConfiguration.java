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

import java.util.List;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.ItemHolderConverter;
import com.l2jserver.gameserver.config.converter.LocationConverter;
import com.l2jserver.gameserver.config.converter.Seconds2MillisecondsConverter;
import com.l2jserver.gameserver.config.converter.SkillHolderConverter;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.SkillHolder;

/**
 * TvT Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/tvt.properties",
	"file:./config/tvt.properties",
	"classpath:config/tvt.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface TvTConfiguration extends Reloadable {
	
	@Key("Enabled")
	boolean enabled();
	
	@Key("Instanced")
	boolean instanced();
	
	@Key("InstanceFile")
	String getInstanceFile();
	
	@Key("Interval")
	List<String> getInterval();
	
	// TODO(Zoey76): Convert to milliseconds.
	@Key("ParticipationTime")
	int getParticipationTime();
	
	// TODO(Zoey76): Convert to milliseconds.
	@Key("RunningTime")
	int getRunningTime();
	
	@Key("ParticipationNpcId")
	int getParticipationNpcId();
	
	@Key("ParticipationFee")
	@ConverterClass(ItemHolderConverter.class)
	ItemHolder getParticipationFee();
	
	@Key("ParticipationNpcLoc")
	@ConverterClass(LocationConverter.class)
	Location getParticipationNpcLoc();
	
	@Key("MinPlayersInTeams")
	int getMinPlayersInTeams();
	
	@Key("MaxPlayersInTeams")
	int getMaxPlayersInTeams();
	
	@Key("MinPlayerLevel")
	int getMinPlayerLevel();
	
	@Key("MaxPlayerLevel")
	int getMaxPlayerLevel();
	
	@Key("RespawnTeleportDelay")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getRespawnTeleportDelay();
	
	@Key("StartLeaveTeleportDelay")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getStartLeaveTeleportDelay();
	
	@Key("Team1Name")
	String getTeam1Name();
	
	@Key("Team1Loc")
	@ConverterClass(LocationConverter.class)
	Location getTeam1Loc();
	
	@Key("Team2Name")
	String getTeam2Name();
	
	@Key("Team2Loc")
	@ConverterClass(LocationConverter.class)
	Location getTeam2Loc();
	
	@Separator(";")
	@Key("Reward")
	@ConverterClass(ItemHolderConverter.class)
	List<ItemHolder> getReward();
	
	@Key("AllowTargetTeamMember")
	boolean allowTargetTeamMember();
	
	@Key("AllowScroll")
	boolean allowScroll();
	
	@Key("AllowPotion")
	boolean allowPotion();
	
	// TODO(Zoey76): Implement AllowSummonByItem configuration.
	@Key("AllowSummonByItem")
	boolean allowSummonByItem();
	
	@Key("DoorsToOpen")
	List<Integer> getDoorsToOpen();
	
	@Key("DoorsToClose")
	List<Integer> getDoorsToClose();
	
	@Key("RewardTeamTie")
	boolean rewardTeamTie();
	
	@Key("EffectsRemoval")
	int getEffectsRemoval();
	
	@Separator(";")
	@Key("FighterBuffs")
	@ConverterClass(SkillHolderConverter.class)
	List<SkillHolder> getFighterBuffs();
	
	@Separator(";")
	@Key("MageBuffs")
	@ConverterClass(SkillHolderConverter.class)
	List<SkillHolder> getMageBuffs();
	
	@Key("MaxParticipantsPerIP")
	int getMaxParticipantsPerIP();
	
	@Key("AllowVoicedInfoCommand")
	boolean allowVoicedInfoCommand();
}