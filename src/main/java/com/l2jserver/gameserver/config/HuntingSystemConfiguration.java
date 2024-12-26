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
 * Hunting System Configuration.
 * @author Maneco2
 * @version 2.6.3.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/huntingsystem.properties",
	"file:./config/huntingsystem.properties",
	"classpath:config/huntingsystem.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface HuntingSystemConfiguration extends Reloadable {
	@Key("NevitEnable")
	boolean getNevitEnable();
	
	@Key("NevitBlessingMaxPoints")
	int getNevitBlessingMaxPoints();
	
	@Key("NevitBonusMaxTime")
	int getHuntingBonusMaxTime();
	
	@Key("NevitRegularPoints")
	int getNevitRegularPoints();
	
	@Key("NevitRegularPoints2")
	int getNevitRegularPoints2();
	
	@Key("NevitNormalPoints")
	int getNevitNormalPoints();
	
	@Key("NevitLevelAcquirePoints")
	int getNevitLevelAcquirePoints();
	
	@Key("NevitLevelAcquirePoints2")
	int getNevitLevelAcquirePoints2();
	
	@Key("NevitDecreaseVitalityAcquirePoints")
	int getNevitDecreaseVitalityAcquirePoints();
	
	@Key("NevitBonusEffectTime")
	int getNevitBlessingEffetcTime();
	
	@Key("HuntingSystemLimit")
	boolean getHuntingBonusLimit();
}