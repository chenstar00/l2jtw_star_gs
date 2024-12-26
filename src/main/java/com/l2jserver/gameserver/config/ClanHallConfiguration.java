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
 * Clan Hall Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/clanhall.properties",
	"file:./config/clanhall.properties",
	"classpath:config/clanhall.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface ClanHallConfiguration extends Reloadable {
	
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
	
	@Key("SupportFeeLvl3")
	int getSupportFeeLvl3();
	
	@Key("SupportFeeLvl4")
	int getSupportFeeLvl4();
	
	@Key("SupportFeeLvl5")
	int getSupportFeeLvl5();
	
	@Key("SupportFeeLvl6")
	int getSupportFeeLvl6();
	
	@Key("SupportFeeLvl7")
	int getSupportFeeLvl7();
	
	@Key("SupportFeeLvl8")
	int getSupportFeeLvl8();
	
	@Key("MpRegenerationFunctionFeeRatio")
	long getMpRegenerationFunctionFeeRatio();
	
	@Key("MpRegenerationFeeLvl1")
	int getMpRegenerationFeeLvl1();
	
	@Key("MpRegenerationFeeLvl2")
	int getMpRegenerationFeeLvl2();
	
	@Key("MpRegenerationFeeLvl3")
	int getMpRegenerationFeeLvl3();
	
	@Key("MpRegenerationFeeLvl4")
	int getMpRegenerationFeeLvl4();
	
	@Key("MpRegenerationFeeLvl5")
	int getMpRegenerationFeeLvl5();
	
	@Key("HpRegenerationFunctionFeeRatio")
	long getHpRegenerationFunctionFeeRatio();
	
	@Key("HpRegenerationFeeLvl1")
	int getHpRegenerationFeeLvl1();
	
	@Key("HpRegenerationFeeLvl2")
	int getHpRegenerationFeeLvl2();
	
	@Key("HpRegenerationFeeLvl3")
	int getHpRegenerationFeeLvl3();
	
	@Key("HpRegenerationFeeLvl4")
	int getHpRegenerationFeeLvl4();
	
	@Key("HpRegenerationFeeLvl5")
	int getHpRegenerationFeeLvl5();
	
	@Key("HpRegenerationFeeLvl6")
	int getHpRegenerationFeeLvl6();
	
	@Key("HpRegenerationFeeLvl7")
	int getHpRegenerationFeeLvl7();
	
	@Key("HpRegenerationFeeLvl8")
	int getHpRegenerationFeeLvl8();
	
	@Key("HpRegenerationFeeLvl9")
	int getHpRegenerationFeeLvl9();
	
	@Key("HpRegenerationFeeLvl10")
	int getHpRegenerationFeeLvl10();
	
	@Key("HpRegenerationFeeLvl11")
	int getHpRegenerationFeeLvl11();
	
	@Key("HpRegenerationFeeLvl12")
	int getHpRegenerationFeeLvl12();
	
	@Key("HpRegenerationFeeLvl13")
	int getHpRegenerationFeeLvl13();
	
	@Key("ExpRegenerationFunctionFeeRatio")
	long getExpRegenerationFunctionFeeRatio();
	
	@Key("ExpRegenerationFeeLvl1")
	int getExpRegenerationFeeLvl1();
	
	@Key("ExpRegenerationFeeLvl2")
	int getExpRegenerationFeeLvl2();
	
	@Key("ExpRegenerationFeeLvl3")
	int getExpRegenerationFeeLvl3();
	
	@Key("ExpRegenerationFeeLvl4")
	int getExpRegenerationFeeLvl4();
	
	@Key("ExpRegenerationFeeLvl5")
	int getExpRegenerationFeeLvl5();
	
	@Key("ExpRegenerationFeeLvl6")
	int getExpRegenerationFeeLvl6();
	
	@Key("ExpRegenerationFeeLvl7")
	int getExpRegenerationFeeLvl7();
	
	@Key("ItemCreationFunctionFeeRatio")
	long getItemCreationFunctionFeeRatio();
	
	@Key("ItemCreationFunctionFeeLvl1")
	int getItemCreationFunctionFeeLvl1();
	
	@Key("ItemCreationFunctionFeeLvl2")
	int getItemCreationFunctionFeeLvl2();
	
	@Key("ItemCreationFunctionFeeLvl3")
	int getItemCreationFunctionFeeLvl3();
	
	@Key("CurtainFunctionFeeRatio")
	long getCurtainFunctionFeeRatio();
	
	@Key("CurtainFunctionFeeLvl1")
	int getCurtainFunctionFeeLvl1();
	
	@Key("CurtainFunctionFeeLvl2")
	int getCurtainFunctionFeeLvl2();
	
	@Key("FrontPlatformFunctionFeeLvl1")
	int getFrontPlatformFunctionFeeLvl1();
	
	@Key("FrontPlatformFunctionFeeLvl2")
	int getFrontPlatformFunctionFeeLvl2();
	
	@Key("FrontPlatformFunctionFeeRatio")
	long getFrontPlatformFunctionFeeRatio();
	
	@Key("MpBuffFree")
	boolean mpBuffFree();
	
	@Key("MinClanLevel")
	int getMinClanLevel();
	
	@Key("MaxAttackers")
	int getMaxAttackers();
	
	// TODO(Zoey76): Implement MaxFlagsPerClan configuration.
	@Key("MaxFlagsPerClan")
	int getMaxFlagsPerClan();
	
	@Key("EnableFame")
	boolean enableFame();
	
	@Key("FameAmount")
	int getFameAmount();
	
	@Key("FameFrequency")
	int getFameFrequency();
}