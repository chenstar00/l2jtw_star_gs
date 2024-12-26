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

import java.util.Set;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.Minutes2MillisecondsConverter;
import com.l2jserver.gameserver.config.converter.Seconds2MillisecondsConverter;
import com.l2jserver.gameserver.config.converter.ServerListTypeConverter;
import com.l2jserver.gameserver.enums.IllegalActionPunishmentType;

/**
 * General Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/general.properties",
	"file:./config/general.properties",
	"classpath:config/general.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface GeneralConfiguration extends Mutable, Reloadable {
	
	@Key("EverybodyHasAdminRights")
	boolean everybodyHasAdminRights();
	
	@Key("ServerListBrackets")
	boolean getServerListBrackets();
	
	@Key("ServerListType")
	@ConverterClass(ServerListTypeConverter.class)
	int getServerListType();
	
	@Key("ServerListAge")
	int getServerListAge();
	
	@Key("ServerGMOnly")
	boolean serverGMOnly();
	
	@Key("GMHeroAura")
	boolean gmHeroAura();
	
	@Key("GMStartupInvulnerable")
	boolean gmStartupInvulnerable();
	
	@Key("GMStartupInvisible")
	boolean gmStartupInvisible();
	
	@Key("GMStartupSilence")
	boolean gmStartupSilence();
	
	@Key("GMStartupAutoList")
	boolean gmStartupAutoList();
	
	@Key("GMStartupDietMode")
	boolean gmStartupDietMode();
	
	@Key("GMItemRestriction")
	boolean gmItemRestriction();
	
	@Key("GMSkillRestriction")
	boolean gmSkillRestriction();
	
	@Key("GMTradeRestrictedItems")
	boolean gmTradeRestrictedItems();
	
	@Key("GMRestartFighting")
	boolean gmRestartFighting();
	
	@Key("GMShowAnnouncerName")
	boolean gmShowAnnouncerName();
	
	// TODO(Zoey76): Implement GMShowCritAnnouncerName configuration.
	@Key("GMShowCritAnnouncerName")
	boolean gmShowCritAnnouncerName();
	
	@Key("GMGiveSpecialSkills")
	boolean gmGiveSpecialSkills();
	
	@Key("GMGiveSpecialAuraSkills")
	boolean gmGiveSpecialAuraSkills();
	
	@Key("GameGuardEnforce")
	boolean gameGuardEnforce();
	
	// TODO(Zoey76): Implement GameGuardProhibitAction configuration.
	@Key("GameGuardProhibitAction")
	boolean gameGuardProhibitAction();
	
	@Key("LogChat")
	boolean logChat();
	
	// TODO(Zoey76): Implement LogAutoAnnouncements configuration.
	@Key("LogAutoAnnouncements")
	boolean logAutoAnnouncements();
	
	@Key("LogItems")
	boolean logItems();
	
	@Key("LogItemsSmallLog")
	boolean logItemsSmallLog();
	
	@Key("LogItemEnchants")
	boolean logItemEnchants();
	
	@Key("LogSkillEnchants")
	boolean logSkillEnchants();
	
	@Key("GMAudit")
	boolean gmAudit();
	
	@Key("SkillCheckEnable")
	boolean skillCheckEnable();
	
	@Key("SkillCheckRemove")
	boolean skillCheckRemove();
	
	@Key("SkillCheckGM")
	boolean skillCheckGM();
	
	@Key("ThreadPoolSizeEffects")
	int getThreadPoolSizeEffects();
	
	@Key("ThreadPoolSizeGeneral")
	int getThreadPoolSizeGeneral();
	
	@Key("ThreadPoolSizeEvents")
	int getThreadPoolSizeEvents();
	
	@Key("UrgentPacketThreadCoreSize")
	int getUrgentPacketThreadCoreSize();
	
	@Key("GeneralPacketThreadCoreSize")
	int getGeneralPacketThreadCoreSize();
	
	@Key("GeneralThreadCoreSize")
	int getGeneralThreadCoreSize();
	
	@Key("AiMaxThread")
	int getAiMaxThread();
	
	@Key("EventsMaxThread")
	int getEventsMaxThread();
	
	@Key("DeadLockDetector")
	boolean deadLockDetector();
	
	@Key("DeadLockCheckInterval")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getDeadLockCheckInterval();
	
	@Key("RestartOnDeadlock")
	boolean restartOnDeadlock();
	
	@Key("ClientPacketQueueSize")
	int getClientPacketQueueSize();
	
	@Key("ClientPacketQueueMaxBurstSize")
	int getClientPacketQueueMaxBurstSize();
	
	@Key("ClientPacketQueueMaxPacketsPerSecond")
	int getClientPacketQueueMaxPacketsPerSecond();
	
	@Key("ClientPacketQueueMeasureInterval")
	int getClientPacketQueueMeasureInterval();
	
	@Key("ClientPacketQueueMaxAveragePacketsPerSecond")
	int getClientPacketQueueMaxAveragePacketsPerSecond();
	
	@Key("ClientPacketQueueMaxFloodsPerMin")
	int getClientPacketQueueMaxFloodsPerMin();
	
	@Key("ClientPacketQueueMaxOverflowsPerMin")
	int getClientPacketQueueMaxOverflowsPerMin();
	
	@Key("ClientPacketQueueMaxUnderflowsPerMin")
	int getClientPacketQueueMaxUnderflowsPerMin();
	
	@Key("ClientPacketQueueMaxUnknownPerMin")
	int getClientPacketQueueMaxUnknownPerMin();
	
	@Key("AllowDiscardItem")
	boolean allowDiscardItem();
	
	@Key("AutoDestroyDroppedItemAfter")
	int getAutoDestroyDroppedItemAfter();
	
	@Key("AutoDestroyHerbTime")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getAutoDestroyHerbTime();
	
	@Key("ProtectedItems")
	Set<Integer> getProtectedItems();
	
	@Key("DatabaseCleanUp")
	boolean databaseCleanUp();
	
	// TODO(Zoey76): Implement ConnectionCloseTime configuration.
	@Key("ConnectionCloseTime")
	long getConnectionCloseTime();
	
	@Key("CharacterDataStoreInterval")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getCharacterDataStoreInterval();
	
	@Key("LazyItemsUpdate")
	boolean lazyItemsUpdate();
	
	@Key("UpdateItemsOnCharStore")
	boolean updateItemsOnCharStore();
	
	@Key("DestroyPlayerDroppedItem")
	boolean destroyPlayerDroppedItem();
	
	@Key("DestroyEquipableItem")
	boolean destroyEquipableItem();
	
	@Key("SaveDroppedItem")
	boolean saveDroppedItem();
	
	@Key("EmptyDroppedItemTableAfterLoad")
	boolean emptyDroppedItemTableAfterLoad();
	
	@Key("SaveDroppedItemInterval")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getSaveDroppedItemInterval();
	
	@Key("ClearDroppedItemTable")
	boolean clearDroppedItemTable();
	
	@Key("AutoDeleteInvalidQuestData")
	boolean autoDeleteInvalidQuestData();
	
	@Key("PreciseDropCalculation")
	boolean preciseDropCalculation();
	
	@Key("PreciseDropMultipleGroupRolls")
	boolean preciseDropMultipleGroupRolls();
	
	@Key("PreciseDropMultipleRollsAggregateDrops")
	boolean preciseDropMultipleRollsAggregateDrops();
	
	@Key("MultipleItemDrop")
	boolean multipleItemDrop();
	
	@Key("ForceInventoryUpdate")
	boolean forceInventoryUpdate();
	
	@Key("LazyCache")
	boolean lazyCache();
	
	@Key("CacheCharNames")
	boolean cacheCharNames();
	
	@Key("MinNPCAnimation")
	int getMinNPCAnimation();
	
	@Key("MaxNPCAnimation")
	int getMaxNPCAnimation();
	
	@Key("MinMonsterAnimation")
	int getMinMonsterAnimation();
	
	@Key("MaxMonsterAnimation")
	int getMaxMonsterAnimation();
	
	@Key("MoveBasedKnownList")
	boolean moveBasedKnownList();
	
	@Key("KnownListUpdateInterval")
	long getKnownListUpdateInterval();
	
	@Key("CheckKnownList")
	boolean checkKnownList();
	
	@Key("GridsAlwaysOn")
	boolean gridsAlwaysOn();
	
	@Key("GridNeighborTurnOnTime")
	int getGridNeighborTurnOnTime();
	
	@Key("GridNeighborTurnOffTime")
	int getGridNeighborTurnOffTime();
	
	@Key("EnableFallingDamage")
	boolean enableFallingDamage();
	
	@Key("PeaceZoneMode")
	int getPeaceZoneMode();
	
	@Key("GlobalChat")
	String getGlobalChat();
	
	@Key("TradeChat")
	String getTradeChat();
	
	@Key("AllowWarehouse")
	boolean allowWarehouse();
	
	@Key("WarehouseCache")
	boolean warehouseCache();
	
	@Key("WarehouseCacheTime")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getWarehouseCacheTime();
	
	@Key("AllowRefund")
	boolean allowRefund();
	
	@Key("AllowMail")
	boolean allowMail();
	
	@Key("AllowAttachments")
	boolean allowAttachments();
	
	@Key("AllowWear")
	boolean allowWear();
	
	@Key("WearDelay")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getWearDelay();
	
	@Key("WearPrice")
	int getWearPrice();
	
	@Key("RestorePlayerInstance")
	boolean restorePlayerInstance();
	
	@Key("AllowSummonInInstance")
	boolean allowSummonInInstance();
	
	@Key("EjectDeadPlayerTime")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getEjectDeadPlayerTime();
	
	@Key("InstanceFinishTime")
	int getInstanceFinishTime();
	
	// TODO(Zoey76): Implement AllowRace configuration.
	@Key("AllowRace")
	boolean allowRace();
	
	@Key("AllowWater")
	boolean allowWater();
	
	@Key("AllowRentPet")
	boolean allowRentPet();
	
	@Key("AllowFishing")
	boolean allowFishing();
	
	@Key("AllowBoat")
	boolean allowBoat();
	
	@Key("BoatBroadcastRadius")
	int getBoatBroadcastRadius();
	
	@Key("AllowCursedWeapons")
	boolean allowCursedWeapons();
	
	@Key("AllowPetWalkers")
	boolean allowPetWalkers();
	
	@Key("ShowServerNews")
	boolean showServerNews();
	
	@Key("EnableCommunityBoard")
	boolean enableCommunityBoard();
	
	@Key("BBSDefault")
	String getBBSDefault();
	
	@Key("UseChatFilter")
	boolean useChatFilter();
	
	@Key("ChatFilterChars")
	String getChatFilterChars();
	
	@Key("ChatFilter")
	Set<String> getChatFilter();
	
	@Key("BanChatChannels")
	Set<Integer> getBanChatChannels();
	
	@Key("AllowManor")
	boolean allowManor();
	
	@Key("ManorRefreshTime")
	int getManorRefreshTime();
	
	@Key("ManorRefreshMin")
	int getManorRefreshMin();
	
	@Key("ManorApproveTime")
	int getManorApproveTime();
	
	@Key("ManorApproveMin")
	int getManorApproveMin();
	
	@Key("ManorMaintenanceMin")
	int getManorMaintenanceMin();
	
	@Key("ManorSaveAllActions")
	boolean manorSaveAllActions();
	
	@Key("ManorSavePeriodRate")
	int getManorSavePeriodRate();
	
	@Key("AllowLottery")
	boolean allowLottery();
	
	@Key("LotteryPrize")
	long getLotteryPrize();
	
	@Key("LotteryTicketPrice")
	long getLotteryTicketPrice();
	
	@Key("Lottery5NumberRate")
	float getLottery5NumberRate();
	
	@Key("Lottery4NumberRate")
	float getLottery4NumberRate();
	
	@Key("Lottery3NumberRate")
	float getLottery3NumberRate();
	
	@Key("Lottery2and1NumberPrize")
	long getLottery2and1NumberPrize();
	
	@Key("ItemAuctionEnabled")
	boolean itemAuctionEnabled();
	
	@Key("ItemAuctionExpiredAfter")
	int getItemAuctionExpiredAfter();
	
	@Key("ItemAuctionTimeExtendsOnBid")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getItemAuctionTimeExtendsOnBid();
	
	// TODO(Zoey76): Implement RiftMinPartySize configuration.
	@Key("RiftMinPartySize")
	int getRiftMinPartySize();
	
	@Key("MaxRiftJumps")
	int getMaxRiftJumps();
	
	@Key("RiftSpawnDelay")
	int getRiftSpawnDelay();
	
	@Key("AutoJumpsDelayMin")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getAutoJumpsDelayMin();
	
	@Key("AutoJumpsDelayMax")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getAutoJumpsDelayMax();
	
	@Key("BossRoomTimeMultiply")
	float getBossRoomTimeMultiply();
	
	// TODO(Zoey76): Implement RecruitCost configuration.
	@Key("RecruitCost")
	int getRecruitCost();
	
	// TODO(Zoey76): Implement SoldierCost configuration.
	@Key("SoldierCost")
	int getSoldierCost();
	
	// TODO(Zoey76): Implement OfficerCost configuration.
	@Key("OfficerCost")
	int getOfficerCost();
	
	// TODO(Zoey76): Implement CaptainCost configuration.
	@Key("CaptainCost")
	int getCaptainCost();
	
	// TODO(Zoey76): Implement CommanderCost configuration.
	@Key("CommanderCost")
	int getCommanderCost();
	
	// TODO(Zoey76): Implement HeroCost configuration.
	@Key("HeroCost")
	int getHeroCost();
	
	@Key("TimeOfAttack")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getTimeOfAttack();
	
	// TODO(Zoey76): Implement TimeOfCoolDown configuration.
	@Key("TimeOfCoolDown")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getTimeOfCoolDown();
	
	@Key("TimeOfEntry")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getTimeOfEntry();
	
	@Key("TimeOfWarmUp")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getTimeOfWarmUp();
	
	// TODO(Zoey76): Move this four sepulchers to own configuration file.
	@Key("NumberOfNecessaryPartyMembers")
	int getNumberOfNecessaryPartyMembers();
	
	@Key("DefaultPunish")
	IllegalActionPunishmentType getDefaultPunish();
	
	@Key("DefaultPunishParam")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getDefaultPunishParam();
	
	@Key("OnlyGMItemsFree")
	boolean onlyGMItemsFree();
	
	@Key("JailIsPvp")
	boolean jailIsPvp();
	
	@Key("JailDisableChat")
	boolean jailDisableChat();
	
	@Key("JailDisableTransaction")
	boolean jailDisableTransaction();
	
	@Key("NormalEnchantCostMultipiler")
	int getNormalEnchantCostMultipiler();
	
	@Key("SafeEnchantCostMultipiler")
	int getSafeEnchantCostMultipiler();
	
	@Key("CustomSpawnlistTable")
	boolean customSpawnlistTable();
	
	@Key("SaveGmSpawnOnCustom")
	boolean saveGmSpawnOnCustom();
	
	@Key("CustomNpcData")
	boolean customNpcData();
	
	@Key("CustomTeleportTable")
	boolean customTeleportTable();
	
	@Key("CustomNpcBufferTables")
	boolean customNpcBufferTables();
	
	@Key("CustomSkillsLoad")
	boolean customSkillsLoad();
	
	@Key("CustomItemsLoad")
	boolean customItemsLoad();
	
	@Key("CustomMultisellLoad")
	boolean customMultisellLoad();
	
	@Key("CustomBuyListLoad")
	boolean customBuyListLoad();
	
	@Key("BirthdayGift")
	int getBirthdayGift();
	
	@Key("BirthdayMailSubject")
	String getBirthdayMailSubject();
	
	@Key("BirthdayMailText")
	String getBirthdayMailText();
	
	@Key("EnableBlockCheckerEvent")
	boolean enableBlockCheckerEvent();
	
	@Key("BlockCheckerMinTeamMembers")
	int getBlockCheckerMinTeamMembers();
	
	@Key("HBCEFairPlay")
	boolean isHBCEFairPlay();
	
	@Key("HellboundWithoutQuest")
	boolean hellboundWithoutQuest();
	
	@Key("EnableBotReportButton")
	boolean enableBotReportButton();
	
	@Key("BotReportPointsResetHour")
	String getBotReportPointsResetHour();
	
	@Key("BotReportDelay")
	@ConverterClass(Minutes2MillisecondsConverter.class)
	long getBotReportDelay();
	
	@Key("AllowReportsFromSameClanMembers")
	boolean allowReportsFromSameClanMembers();
	
	@Key("Debug")
	boolean debug();
	
	@Key("InstanceDebug")
	boolean instanceDebug();
	
	@Key("HtmlActionCacheDebug")
	boolean htmlActionCacheDebug();
	
	@Key("PacketHandlerDebug")
	boolean packetHandlerDebug();
	
	@Key("Developer")
	boolean developer();
	
	@Key("NoHandlers")
	boolean noHandlers();
	
	@Key("NoQuests")
	boolean noQuests();
	
	@Key("NoSpawns")
	boolean noSpawns();
	
	@Key("ShowQuestsLoadInLogs")
	boolean showQuestsLoadInLogs();
	
	// TODO(Zoey76): Implement ShowScriptsLoadInLogs configuration.
	@Key("ShowScriptsLoadInLogs")
	boolean showScriptsLoadInLogs();
}