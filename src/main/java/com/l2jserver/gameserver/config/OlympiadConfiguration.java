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
import java.util.Set;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.ItemHolderConverter;
import com.l2jserver.gameserver.model.holders.ItemHolder;

/**
 * Olympiad Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/olympiad.properties",
	"file:./config/olympiad.properties",
	"classpath:config/olympiad.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface OlympiadConfiguration extends Reloadable {
	
	@Key("StartHour")
	int getStartHour();
	
	@Key("StartMinute")
	int getStartMinute();
	
	@Key("MaxBuffs")
	int getMaxBuffs();
	
	// TODO(Zoey76): Check if this should be in minutes or other time unit.
	@Key("CompetitionPeriod")
	int getCompetitionPeriod();
	
	@Key("BattlePeriod")
	int getBattlePeriod();
	
	// TODO(Zoey76): Check if this should be in minutes or other time unit.
	@Key("WeeklyPeriod")
	int getWeeklyPeriod();
	
	// TODO(Zoey76): Check if this should be in minutes or other time unit.
	@Key("ValidationPeriod")
	int getValidationPeriod();
	
	@Key("StartPoints")
	int getStartPoints();
	
	@Key("WeeklyPoints")
	int getWeeklyPoints();
	
	@Key("ClassedParticipants")
	int getClassedParticipants();
	
	@Key("NonClassedParticipants")
	int getNonClassedParticipants();
	
	@Key("TeamsParticipants")
	int getTeamsParticipants();
	
	@Key("RegistrationDisplayNumber")
	int getRegistrationDisplayNumber();
	
	@Separator(";")
	@Key("ClassedReward")
	@ConverterClass(ItemHolderConverter.class)
	List<ItemHolder> getClassedReward();
	
	@Separator(";")
	@Key("NonClassedReward")
	@ConverterClass(ItemHolderConverter.class)
	List<ItemHolder> getNonClassedReward();
	
	@Separator(";")
	@Key("TeamReward")
	@ConverterClass(ItemHolderConverter.class)
	List<ItemHolder> getTeamReward();
	
	@Key("CompetitionRewardItem")
	int getCompetitionRewardItem();
	
	@Key("MinMatchesForPoints")
	int getMinMatchesForPoints();
	
	@Key("GPPerPoint")
	int getGPPerPoint();
	
	@Key("HeroPoints")
	int getHeroPoints();
	
	@Key("Rank1Points")
	int getRank1Points();
	
	@Key("Rank2Points")
	int getRank2Points();
	
	@Key("Rank3Points")
	int getRank3Points();
	
	@Key("Rank4Points")
	int getRank4Points();
	
	@Key("Rank5Points")
	int getRank5Points();
	
	@Key("MaxPoints")
	int getMaxPoints();
	
	@Key("ShowMonthlyWinners")
	boolean showMonthlyWinners();
	
	@Key("AnnounceGames")
	boolean announceGames();
	
	@Key("RestrictedItems")
	Set<Integer> getRestrictedItems();
	
	@Key("EnchantLimit")
	int getEnchantLimit();
	
	@Key("LogFights")
	boolean logFights();
	
	@Key("WaitTime")
	int getWaitTime();
	
	@Key("DividerClassed")
	int getDividerClassed();
	
	@Key("DividerNonClassed")
	int getDividerNonClassed();
	
	@Key("MaxWeeklyMatches")
	int getMaxWeeklyMatches();
	
	@Key("MaxWeeklyMatchesNonClassed")
	int getMaxWeeklyMatchesNonClassed();
	
	@Key("MaxWeeklyMatchesClassed")
	int getMaxWeeklyMatchesClassed();
	
	@Key("MaxWeeklyMatchesTeam")
	int getMaxWeeklyMatchesTeam();
	
	// TODO(Zoey76): Change this so the check doesn't depend on a null value.
	@Key("CurrentCycle")
	Integer getCurrentCycle();
	
	@Key("Period")
	int getPeriod();
	
	// TODO(Zoey76): Check if this should be in minutes or other time unit.
	@Key("OlympiadEnd")
	long getOlympiadEnd();
	
	@Key("ValidationEnd")
	long getValidationEnd();
	
	@Key("NextWeeklyChange")
	long getNextWeeklyChange();
}