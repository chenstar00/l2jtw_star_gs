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

import com.l2jserver.gameserver.config.converter.Hours2MillisecondsConverter;
import com.l2jserver.gameserver.config.converter.Minutes2MillisecondsConverter;

/**
 * Grand Boss Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/grandboss.properties",
	"file:./config/grandboss.properties",
	"classpath:config/grandboss.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface GrandBossConfiguration extends Reloadable {
	
	@Key("AntharasWaitTime")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getAntharasWaitTime();
	
	@Key("IntervalOfAntharasSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfAntharasSpawn();
	
	@Key("RandomOfAntharasSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfAntharasSpawn();
	
	@Key("ValakasWaitTime")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getValakasWaitTime();
	
	@Key("IntervalOfValakasSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfValakasSpawn();
	
	@Key("RandomOfValakasSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfValakasSpawn();
	
	@Key("IntervalOfBaiumSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfBaiumSpawn();
	
	@Key("RandomOfBaiumSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfBaiumSpawn();
	
	@Key("IntervalOfCoreSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfCoreSpawn();
	
	@Key("RandomOfCoreSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfCoreSpawn();
	
	@Key("IntervalOfOrfenSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfOrfenSpawn();
	
	@Key("RandomOfOrfenSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfOrfenSpawn();
	
	@Key("IntervalOfQueenAntSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfQueenAntSpawn();
	
	@Key("RandomOfQueenAntSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfQueenAntSpawn();
	
	@Key("IntervalOfBelethSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getIntervalOfBelethSpawn();
	
	@Key("RandomOfBelethSpawn")
	@ConverterClass(Hours2MillisecondsConverter.class)
	long getRandomOfBelethSpawn();
	
	@Key("BelethMinPlayers")
	int getBelethMinPlayers();
	
	// TODO(Zoey76): Implement AllowZakenWithoutParty configuration.
	@Key("AllowZakenWithoutParty")
	boolean getAllowZakenWithoutParty();
	
	// TODO(Zoey76): Implement ZakenMinPlayers configuration.
	@Key("ZakenMinPlayers")
	List<Integer> getZakenMinPlayers();
	
	// TODO(Zoey76): Implement ZakenMaxPlayers configuration.
	@Key("ZakenMaxPlayers")
	List<Integer> getZakenMaxPlayers();
	
	// TODO(Zoey76): Implement ZakenMinPlayerLevel configuration.
	@Key("ZakenMinPlayerLevel")
	List<Integer> getZakenMinPlayerLevel();
	
	// TODO(Zoey76): Implement ZakenCanBeRevealedByAOESpells configuration.
	@Key("ZakenCanBeRevealedByAOESpells")
	boolean getZakenCanBeRevealedByAoeSPells();
}