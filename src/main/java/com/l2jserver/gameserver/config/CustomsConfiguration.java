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
import java.util.Map;
import java.util.Set;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.ColorConverter;
import com.l2jserver.gameserver.config.converter.IPLimitConverter;
import com.l2jserver.gameserver.config.converter.Seconds2MillisecondsConverter;

/**
 * Customs Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/customs.properties",
	"file:./config/customs.properties",
	"classpath:config/customs.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface CustomsConfiguration extends Reloadable {
	
	@Key("ChampionEnable")
	boolean championEnable();
	
	@Key("ChampionPassive")
	boolean championPassive();
	
	@Key("ChampionFrequency")
	int getChampionFrequency();
	
	@Key("ChampionTitle")
	String getChampionTitle();
	
	@Key("ChampionMinLevel")
	int getChampionMinLevel();
	
	@Key("ChampionMaxLevel")
	int getChampionMaxLevel();
	
	@Key("ChampionHp")
	int getChampionHp();
	
	@Key("ChampionHpRegen")
	double getChampionHpRegen();
	
	@Key("ChampionRewardsExpSp")
	double getChampionRewardsExpSp();
	
	@Key("ChampionRewardsChance")
	double getChampionRewardsChance();
	
	@Key("ChampionRewardsAmount")
	double getChampionRewardsAmount();
	
	@Key("ChampionAdenasRewardsChance")
	double getChampionAdenasRewardsChance();
	
	@Key("ChampionAdenasRewardsAmount")
	double getChampionAdenasRewardsAmount();
	
	@Key("ChampionAtk")
	float getChampionAtk();
	
	@Key("ChampionSpdAtk")
	float getChampionSpdAtk();
	
	@Key("ChampionRewardItemID")
	int getChampionRewardItemID();
	
	@Key("ChampionRewardItemQty")
	int getChampionRewardItemQty();
	
	@Key("ChampionRewardLowerLvlItemChance")
	int getChampionRewardLowerLvlItemChance();
	
	@Key("ChampionRewardHigherLvlItemChance")
	int getChampionRewardHigherLvlItemChance();
	
	@Key("ChampionEnableVitality")
	boolean championEnableVitality();
	
	@Key("ChampionEnableInInstances")
	boolean championEnableInInstances();
	
	@Key("AllowWedding")
	boolean allowWedding();
	
	@Key("WeddingPrice")
	int getWeddingPrice();
	
	@Key("WeddingPunishInfidelity")
	boolean weddingPunishInfidelity();
	
	// TODO(Zoey76): Implement WeddingTeleport configuration.
	@Key("WeddingTeleport")
	boolean weddingTeleport();
	
	@Key("WeddingTeleportPrice")
	int getWeddingTeleportPrice();
	
	@Key("WeddingTeleportDuration")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getWeddingTeleportDuration();
	
	@Key("WeddingAllowSameSex")
	boolean weddingAllowSameSex();
	
	@Key("WeddingFormalWear")
	boolean weddingFormalWear();
	
	@Key("WeddingDivorceCosts")
	int getWeddingDivorceCosts();
	
	@Key("BankingEnabled")
	boolean bankingEnabled();
	
	@Key("BankingGoldbarCount")
	int getBankingGoldbarCount();
	
	@Key("BankingAdenaCount")
	int getBankingAdenaCount();
	
	@Key("EnableWarehouseSortingClan")
	boolean enableWarehouseSortingClan();
	
	@Key("EnableWarehouseSortingPrivate")
	boolean enableWarehouseSortingPrivate();
	
	@Key("OfflineTradeEnable")
	boolean offlineTradeEnable();
	
	@Key("OfflineCraftEnable")
	boolean offlineCraftEnable();
	
	@Key("OfflineModeInPeaceZone")
	boolean offlineModeInPeaceZone();
	
	@Key("OfflineModeNoDamage")
	boolean offlineModeNoDamage();
	
	@Key("OfflineSetNameColor")
	boolean offlineSetNameColor();
	
	@Key("OfflineNameColor")
	@ConverterClass(ColorConverter.class)
	int getOfflineNameColor();
	
	@Key("OfflineFame")
	boolean offlineFame();
	
	@Key("RestoreOffliners")
	boolean restoreOffliners();
	
	@Key("OfflineMaxDays")
	int getOfflineMaxDays();
	
	@Key("OfflineDisconnectFinished")
	boolean offlineDisconnectFinished();
	
	@Key("EnableManaPotionSupport")
	boolean enableManaPotionSupport();
	
	@Key("DisplayServerTime")
	boolean displayServerTime();
	
	@Key("ScreenWelcomeMessageEnable")
	boolean screenWelcomeMessageEnable();
	
	@Key("ScreenWelcomeMessageText")
	String getScreenWelcomeMessageText();
	
	@Key("ScreenWelcomeMessageTime")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getScreenWelcomeMessageTime();
	
	@Key("AntiFeedEnable")
	boolean antiFeedEnable();
	
	@Key("AntiFeedDualbox")
	boolean antiFeedDualbox();
	
	@Key("AntiFeedDisconnectedAsDualbox")
	boolean antiFeedDisconnectedAsDualbox();
	
	@Key("AntiFeedInterval")
	int getAntiFeedInterval();
	
	@Key("AnnouncePkPvP")
	boolean announcePkPvP();
	
	@Key("AnnouncePkPvPNormalMessage")
	boolean announcePkPvPNormalMessage();
	
	@Key("AnnouncePkMsg")
	String getAnnouncePkMsg();
	
	@Key("AnnouncePvpMsg")
	String getAnnouncePvpMsg();
	
	@Key("ChatAdmin")
	boolean chatAdmin();
	
	@Key("HellboundStatus")
	boolean hellboundStatus();
	
	@Key("AutoLootVoiceEnable")
	boolean autoLootVoiceCommand();
	
	@Key("AutoLootVoiceRestore")
	boolean autoLootVoiceRestore();
	
	@Key("AutoLootItemsVoiceRestore")
	boolean autoLootItemsVoiceRestore();
	
	@Key("AutoLootHerbsVoiceRestore")
	boolean autoLootHerbsVoiceRestore();
	
	@Key("AutoLootHerbsList")
	Set<Integer> getAutoLootHerbsList();
	
	@Key("AutoLootItemsList")
	Set<Integer> getAutoLootItemsList();
	
	@Key("MultiLangEnable")
	boolean multiLangEnable();
	
	@Key("MultiLangDefault")
	String getMultiLangDefault();
	
	@Key("MultiLangAllowed")
	Set<String> getMultiLangAllowed();
	
	@Key("MultiLangVoiceCommand")
	boolean multiLangVoiceCommand();
	
	@Key("MultiLangSystemMessageEnable")
	boolean multiLangSystemMessageEnable();
	
	@Key("MultiLangSystemMessageAllowed")
	List<String> getMultiLangSystemMessageAllowed();
	
	@Key("MultiLangNpcStringEnable")
	boolean multiLangNpcStringEnable();
	
	@Key("MultiLangNpcStringAllowed")
	List<String> getMultiLangNpcStringAllowed();
	
	@Key("L2WalkerProtection")
	boolean l2WalkerProtection();
	
	@Key("DebugVoiceCommand")
	boolean debugVoiceCommand();
	
	@Key("DualboxCheckMaxPlayersPerIP")
	int getDualboxCheckMaxPlayersPerIP();
	
	@Key("DualboxCheckMaxOlympiadParticipantsPerIP")
	int getDualboxCheckMaxOlympiadParticipantsPerIP();
	
	@Key("DualboxCheckMaxL2EventParticipantsPerIP")
	int getDualboxCheckMaxL2EventParticipantsPerIP();
	
	@Key("DualboxCheckWhitelist")
	@ConverterClass(IPLimitConverter.class)
	Map<Integer, Integer> getDualboxCheckWhitelist();
	
	@Key("AllowChangePassword")
	boolean allowChangePassword();
}