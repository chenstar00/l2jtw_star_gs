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
 * Seven Signs Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/sevensigns.properties",
	"file:./config/sevensigns.properties",
	"classpath:config/sevensigns.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface SevenSignsConfiguration extends Reloadable {
	
	// TODO(Zoey76): Implement RequireClanCastle configuration.
	@Key("RequireClanCastle")
	boolean requireClanCastle();
	
	// TODO(Zoey76): Implement CastleForDawn configuration.
	@Key("CastleForDawn")
	boolean castleForDawn();
	
	// TODO(Zoey76): Implement CastleForDusk configuration.
	@Key("CastleForDusk")
	boolean castleForDusk();
	
	@Key("FestivalMinPlayer")
	int getFestivalMinPlayer();
	
	@Key("MaxPlayerContrib")
	int getMaxPlayerContrib();
	
	// TODO(Zoey76): Convert to milliseconds.
	@Key("FestivalManagerStart")
	int getFestivalManagerStart();
	
	@Key("FestivalLength")
	int getFestivalLength();
	
	@Key("FestivalCycleLength")
	int getFestivalCycleLength();
	
	@Key("FestivalFirstSpawn")
	int getFestivalFirstSpawn();
	
	@Key("FestivalFirstSwarm")
	int getFestivalFirstSwarm();
	
	@Key("FestivalSecondSpawn")
	int getFestivalSecondSpawn();
	
	@Key("FestivalSecondSwarm")
	int getFestivalSecondSwarm();
	
	@Key("FestivalChestSpawn")
	int getFestivalChestSpawn();
	
	@Key("DawnGatesPdefMult")
	double getDawnGatesPdefMult();
	
	@Key("DuskGatesPdefMult")
	double getDuskGatesPdefMult();
	
	@Key("DawnGatesMdefMult")
	double getDawnGatesMdefMult();
	
	@Key("DuskGatesMdefMult")
	double getDuskGatesMdefMult();
	
	@Key("StrictSevenSigns")
	boolean strictSevenSigns();
	
	@Key("SevenSignsLazyUpdate")
	boolean sevenSignsLazyUpdate();
	
	@Key("SevenSignsDawnTicketQuantity")
	int getSevenSignsDawnTicketQuantity();
	
	@Key("SevenSignsDawnTicketPrice")
	int getSevenSignsDawnTicketPrice();
	
	@Key("SevenSignsDawnTicketBundle")
	int getSevenSignsDawnTicketBundle();
	
	@Key("SevenSignsManorsAgreementId")
	int getSevenSignsManorsAgreementId();
	
	@Key("SevenSignsJoinDawnFee")
	int getSevenSignsJoinDawnFee();
}