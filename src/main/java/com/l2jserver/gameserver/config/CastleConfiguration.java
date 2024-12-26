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

import static java.util.concurrent.TimeUnit.HOURS;
import static org.aeonbits.owner.Config.HotReloadType.ASYNC;
import static org.aeonbits.owner.Config.LoadType.MERGE;

import java.util.List;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

/**
 * Castle Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/castle.properties",
	"file:./config/castle.properties",
	"classpath:config/castle.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 1, unit = HOURS, type = ASYNC)
public interface CastleConfiguration extends Reloadable {
	
	@Key("SiegeHourList")
	List<Integer> getSiegeHourList();
	
	@Key("TeleportFunctionFeeRatio")
	long getTeleportFunctionFeeRatio();
	
	@Key("TeleportFunctionFeeLvl1")
	int getTeleportFunctionFeeLvl1();
	
	@Key("TeleportFunctionFeeLvl2")
	int getTeleportFunctionFeeLvl2();
	
	@Key("SupportFunctionFeeRatio")
	long getSupportFunctionFeeRatio();
	
	@Key("SupportFeeLvl1")
	int getSupportFeeLvl1();
	
	@Key("SupportFeeLvl2")
	int getSupportFeeLvl2();
	
	@Key("MpRegenerationFunctionFeeRatio")
	long getMpRegenerationFunctionFeeRatio();
	
	@Key("MpRegenerationFeeLvl1")
	int getMpRegenerationFeeLvl1();
	
	@Key("MpRegenerationFeeLvl2")
	int getMpRegenerationFeeLvl2();
	
	@Key("HpRegenerationFunctionFeeRatio")
	long getHpRegenerationFunctionFeeRatio();
	
	@Key("HpRegenerationFeeLvl1")
	int getHpRegenerationFeeLvl1();
	
	@Key("HpRegenerationFeeLvl2")
	int getHpRegenerationFeeLvl2();
	
	@Key("ExpRegenerationFunctionFeeRatio")
	long getExpRegenerationFunctionFeeRatio();
	
	@Key("ExpRegenerationFeeLvl1")
	int getExpRegenerationFeeLvl1();
	
	@Key("ExpRegenerationFeeLvl2")
	int getExpRegenerationFeeLvl2();
	
	@Key("OuterDoorUpgradePriceLvl2")
	int getOuterDoorUpgradePriceLvl2();
	
	@Key("OuterDoorUpgradePriceLvl3")
	int getOuterDoorUpgradePriceLvl3();
	
	@Key("OuterDoorUpgradePriceLvl5")
	int getOuterDoorUpgradePriceLvl5();
	
	@Key("InnerDoorUpgradePriceLvl2")
	int getInnerDoorUpgradePriceLvl2();
	
	@Key("InnerDoorUpgradePriceLvl3")
	int getInnerDoorUpgradePriceLvl3();
	
	@Key("InnerDoorUpgradePriceLvl5")
	int getInnerDoorUpgradePriceLvl5();
	
	@Key("WallUpgradePriceLvl2")
	int getWallUpgradePriceLvl2();
	
	@Key("WallUpgradePriceLvl3")
	int getWallUpgradePriceLvl3();
	
	@Key("WallUpgradePriceLvl5")
	int getWallUpgradePriceLvl5();
	
	@Key("TrapUpgradePriceLvl1")
	int getTrapUpgradePriceLvl1();
	
	@Key("TrapUpgradePriceLvl2")
	int getTrapUpgradePriceLvl2();
	
	@Key("TrapUpgradePriceLvl3")
	int getTrapUpgradePriceLvl3();
	
	@Key("TrapUpgradePriceLvl4")
	int getTrapUpgradePriceLvl4();
	
	@Key("AllowRideWyvernAlways")
	boolean allowRideWyvernAlways();
	
	@Key("AllowRideWyvernDuringSiege")
	boolean allowRideWyvernDuringSiege();
}