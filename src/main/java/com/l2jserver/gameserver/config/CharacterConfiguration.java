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
import java.util.regex.Pattern;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.ClassMasterSetting;
import com.l2jserver.gameserver.config.converter.ClassMasterSettingConverter;
import com.l2jserver.gameserver.config.converter.Days2MillisecondsConverter;
import com.l2jserver.gameserver.config.converter.MapIntegerIntegerConverter;
import com.l2jserver.gameserver.config.converter.PatternConverter;
import com.l2jserver.gameserver.config.converter.Seconds2MillisecondsConverter;

/**
 * Character Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/character.properties",
	"file:./config/character.properties",
	"classpath:config/character.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 5, unit = MINUTES, type = ASYNC)
public interface CharacterConfiguration extends Reloadable {
	
	// Statistics
	@Key("Delevel")
	boolean delevel();
	
	@Key("DecreaseSkillOnDelevel")
	boolean decreaseSkillOnDelevel();
	
	@Key("WeightLimit")
	int getWeightLimit();
	
	@Key("RunSpeedBoost")
	int getRunSpeedBoost();
	
	@Key("DeathPenaltyChance")
	int getDeathPenaltyChance();
	
	@Key("RespawnRestoreCP")
	double getRespawnRestoreCP();
	
	@Key("RespawnRestoreHP")
	double getRespawnRestoreHP();
	
	@Key("RespawnRestoreMP")
	double getRespawnRestoreMP();
	
	@Key("HpRegenMultiplier")
	double getHpRegenMultiplier();
	
	@Key("MpRegenMultiplier")
	double getMpRegenMultiplier();
	
	@Key("CpRegenMultiplier")
	double getCpRegenMultiplier();
	
	// Skills & Effects
	
	@Key("ModifySkillDuration")
	boolean modifySkillDuration();
	
	@Key("SkillDuration")
	@ConverterClass(MapIntegerIntegerConverter.class)
	Map<Integer, Integer> getSkillDuration();
	
	@Key("ModifySkillReuse")
	boolean modifySkillReuse();
	
	@Key("SkillReuse")
	@ConverterClass(MapIntegerIntegerConverter.class)
	Map<Integer, Integer> getSkillReuse();
	
	@Key("AutoLearnSkills")
	boolean autoLearnSkills();
	
	@Key("AutoLearnForgottenScrollSkills")
	boolean autoLearnForgottenScrollSkills();
	
	@Key("AutoLootHerbs")
	boolean autoLootHerbs();
	
	@Key("MaxBuffAmount")
	int getMaxBuffAmount();
	
	@Key("MaxTriggeredBuffAmount")
	int getMaxTriggeredBuffAmount();
	
	@Key("MaxDanceAmount")
	int getMaxDanceAmount();
	
	@Key("DanceCancelBuff")
	boolean danceCancelBuff();
	
	@Key("DanceConsumeAdditionalMP")
	boolean danceConsumeAdditionalMP();
	
	@Key("StoreDances")
	boolean storeDances();
	
	@Key("AutoLearnDivineInspiration")
	boolean autoLearnDivineInspiration();
	
	@Key("CancelByHit")
	String cancelByHit();
	
	default boolean cancelBow() {
		return cancelByHit().equalsIgnoreCase("all") || cancelByHit().equalsIgnoreCase("bow");
	}
	
	default boolean cancelCast() {
		return cancelByHit().equalsIgnoreCase("all") || cancelByHit().equalsIgnoreCase("cast");
	}
	
	@Key("MagicFailures")
	boolean magicFailures();
	
	@Key("PlayerFakeDeathUpProtection")
	int getPlayerFakeDeathUpProtection();
	
	@Key("StoreSkillCooltime")
	boolean storeSkillCooltime();
	
	@Key("SubclassStoreSkillCooltime")
	boolean subclassStoreSkillCooltime();
	
	@Key("ShieldBlocks")
	boolean shieldBlocks();
	
	@Key("PerfectShieldBlockRate")
	int getPerfectShieldBlockRate();
	
	@Key("EffectTickRatio")
	int getEffectTickRatio();
	
	// Class, Sub-class and skill learning
	
	@Key("AllowClassMasters")
	boolean allowClassMasters();
	
	@Key("ConfigClassMaster")
	@ConverterClass(ClassMasterSettingConverter.class)
	ClassMasterSetting getClassMaster();
	
	@Key("AllowEntireTree")
	boolean allowEntireTree();
	
	@Key("AlternateClassMaster")
	boolean alternateClassMaster();
	
	@Key("LifeCrystalNeeded")
	boolean lifeCrystalNeeded();
	
	@Key("EnchantSkillSpBookNeeded")
	boolean enchantSkillSpBookNeeded();
	
	@Key("DivineInspirationSpBookNeeded")
	boolean divineInspirationSpBookNeeded();
	
	@Key("SkillLearn")
	boolean skillLearn();
	
	@Key("SubclassWithoutQuests")
	boolean subclassWithoutQuests();
	
	@Key("SubclassEverywhere")
	boolean subclassEverywhere();
	
	@Key("TransformationWithoutQuest")
	boolean transformationWithoutQuest();
	
	@Key("FeeDeleteTransferSkills")
	int getFeeDeleteTransferSkills();
	
	@Key("FeeDeleteSubClassSkills")
	int getFeeDeleteSubClassSkills();
	
	// Summons
	
	@Key("SummonStoreSkillCooltime")
	boolean summonStoreSkillCooltime();
	
	@Key("RestoreServitorOnReconnect")
	boolean restoreServitorOnReconnect();
	
	@Key("RestorePetOnReconnect")
	boolean restorePetOnReconnect();
	
	// Limits
	
	@Key("MaxExpBonus")
	double getMaxExpBonus();
	
	@Key("MaxSpBonus")
	double getMaxSpBonus();
	
	@Key("MaxRunSpeed")
	int getMaxRunSpeed();
	
	@Key("MaxPCritRate")
	int getMaxPCritRate();
	
	@Key("MaxMCritRate")
	int getMaxMCritRate();
	
	@Key("MaxPAtkSpeed")
	int getMaxPAtkSpeed();
	
	@Key("MaxMAtkSpeed")
	int getMaxMAtkSpeed();
	
	@Key("MaxEvasion")
	int getMaxEvasion();
	
	@Key("MinAbnormalStateSuccessRate")
	int getMinAbnormalStateSuccessRate();
	
	@Key("MaxAbnormalStateSuccessRate")
	int getMaxAbnormalStateSuccessRate();
	
	@Key("MaxPlayerLevel")
	int getMaxPlayerLevel();
	
	@Key("MaxPetLevel")
	int getMaxPetLevel();
	
	@Key("MaxSubclass")
	int getMaxSubclass();
	
	@Key("BaseSubclassLevel")
	int getBaseSubclassLevel();
	
	@Key("MaxSubclassLevel")
	int getMaxSubclassLevel();
	
	@Key("MaxPvtStoreSellSlotsDwarf")
	int getMaxPvtStoreSellSlotsDwarf();
	
	@Key("MaxPvtStoreSellSlotsOther")
	int getMaxPvtStoreSellSlotsOther();
	
	@Key("MaxPvtStoreBuySlotsDwarf")
	int getMaxPvtStoreBuySlotsDwarf();
	
	@Key("MaxPvtStoreBuySlotsOther")
	int getMaxPvtStoreBuySlotsOther();
	
	@Key("MaximumSlotsForNoDwarf")
	int getMaximumSlotsForNoDwarf();
	
	@Key("MaximumSlotsForDwarf")
	int getMaximumSlotsForDwarf();
	
	@Key("MaximumSlotsForGMPlayer")
	int getMaximumSlotsForGMPlayer();
	
	@Key("MaximumSlotsForQuestItems")
	int getMaximumSlotsForQuestItems();
	
	@Key("MaximumWarehouseSlotsForDwarf")
	int getMaximumWarehouseSlotsForDwarf();
	
	@Key("MaximumWarehouseSlotsForNoDwarf")
	int getMaximumWarehouseSlotsForNoDwarf();
	
	@Key("MaximumWarehouseSlotsForClan")
	int getMaximumWarehouseSlotsForClan();
	
	@Key("MaximumFreightSlots")
	int getMaximumFreightSlots();
	
	@Key("FreightPrice")
	int getFreightPrice();
	
	@Key("NpcTalkBlockingTime")
	int getNpcTalkBlockingTime();
	
	@Key("FriendListLimit")
	int getFriendListLimit();
	
	@Key("BlockListLimit")
	int getBlockListLimit();
	
	// Enchanting
	
	@Key("EnchantChanceElementStone")
	int getEnchantChanceElementStone();
	
	@Key("EnchantChanceElementCrystal")
	int getEnchantChanceElementCrystal();
	
	@Key("EnchantChanceElementJewel")
	int getEnchantChanceElementJewel();
	
	@Key("EnchantChanceElementEnergy")
	int getEnchantChanceElementEnergy();
	
	@Key("EnchantBlacklist")
	Set<Integer> getEnchantBlacklist();
	
	// Augmenting
	
	@Key("AugmentationNGSkillChance")
	int getAugmentationNGSkillChance();
	
	@Key("AugmentationMidSkillChance")
	int getAugmentationMidSkillChance();
	
	@Key("AugmentationHighSkillChance")
	int getAugmentationHighSkillChance();
	
	@Key("AugmentationTopSkillChance")
	int getAugmentationTopSkillChance();
	
	@Key("AugmentationAccSkillChance")
	int getAugmentationAccSkillChance();
	
	@Key("AugmentationBaseStatChance")
	int getAugmentationBaseStatChance();
	
	@Key("AugmentationNGGlowChance")
	int getAugmentationNGGlowChance();
	
	@Key("AugmentationMidGlowChance")
	int getAugmentationMidGlowChance();
	
	@Key("AugmentationHighGlowChance")
	int getAugmentationHighGlowChance();
	
	@Key("AugmentationTopGlowChance")
	int getAugmentationTopGlowChance();
	
	@Key("RetailLikeAugmentation")
	boolean retailLikeAugmentation();
	
	@Key("RetailLikeAugmentationNoGradeChance")
	List<Integer> getRetailLikeAugmentationNoGradeChance();
	
	@Key("RetailLikeAugmentationMidGradeChance")
	List<Integer> getRetailLikeAugmentationMidGradeChance();
	
	@Key("RetailLikeAugmentationHighGradeChance")
	List<Integer> getRetailLikeAugmentationHighGradeChance();
	
	@Key("RetailLikeAugmentationTopGradeChance")
	List<Integer> getRetailLikeAugmentationTopGradeChance();
	
	@Key("RetailLikeAugmentationAccessory")
	boolean retailLikeAugmentationAccessory();
	
	@Key("AugmentationBlacklist")
	Set<Integer> getAugmentationBlacklist();
	
	@Key("AllowAugmentPvPItems")
	boolean allowAugmentPvPItems();
	
	// Karma
	
	@Key("KarmaPlayerCanBeKilledInPeaceZone")
	boolean karmaPlayerCanBeKilledInPeaceZone();
	
	@Key("KarmaPlayerCanUseGK")
	boolean karmaPlayerCanUseGK();
	
	@Key("KarmaPlayerCanTeleport")
	boolean karmaPlayerCanTeleport();
	
	@Key("KarmaPlayerCanShop")
	boolean karmaPlayerCanShop();
	
	@Key("KarmaPlayerCanTrade")
	boolean karmaPlayerCanTrade();
	
	@Key("KarmaPlayerCanUseWareHouse")
	boolean karmaPlayerCanUseWareHouse();
	
	// Fame
	
	@Key("MaxPersonalFamePoints")
	int getMaxPersonalFamePoints();
	
	@Key("FortressZoneFameTaskFrequency")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getFortressZoneFameTaskFrequency();
	
	@Key("FortressZoneFameAcquirePoints")
	int getFortressZoneFameAcquirePoints();
	
	@Key("CastleZoneFameTaskFrequency")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getCastleZoneFameTaskFrequency();
	
	@Key("CastleZoneFameAcquirePoints")
	int getCastleZoneFameAcquirePoints();
	
	@Key("FameForDeadPlayers")
	boolean fameForDeadPlayers();
	
	// Crafting
	
	@Key("Crafting")
	boolean crafting();
	
	@Key("CraftMasterwork")
	boolean craftMasterwork();
	
	@Key("DwarfRecipeLimit")
	int getDwarfRecipeLimit();
	
	@Key("CommonRecipeLimit")
	int getCommonRecipeLimit();
	
	@Key("AlternativeCrafting")
	boolean alternativeCrafting();
	
	@Key("CraftingSpeed")
	double getCraftingSpeed();
	
	@Key("CraftingXpRate")
	double getCraftingXpRate();
	
	@Key("CraftingSpRate")
	double getCraftingSpRate();
	
	@Key("CraftingRareXpRate")
	double getCraftingRareXpRate();
	
	@Key("CraftingRareSpRate")
	double getCraftingRareSpRate();
	
	@Key("BlacksmithUseRecipes")
	boolean blacksmithUseRecipes();
	
	@Key("StoreRecipeShopList")
	boolean storeRecipeShopList();
	
	// Clan
	
	@Key("ClanLeaderDateChange")
	int getClanLeaderDateChange();
	
	@Key("ClanLeaderHourChange")
	String getClanLeaderHourChange();
	
	@Key("ClanLeaderInstantActivation")
	boolean clanLeaderInstantActivation();
	
	@Key("DaysBeforeJoinAClan")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeJoinAClan();
	
	@Key("DaysBeforeCreateAClan")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeCreateAClan();
	
	@Key("DaysToPassToDissolveAClan")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysToPassToDissolveAClan();
	
	@Key("DaysBeforeJoiningAllianceAfterLeaving")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeJoiningAllianceAfterLeaving();
	
	@Key("DaysBeforeJoinAllyWhenDismissed")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeJoinAllyWhenDismissed();
	
	@Key("DaysBeforeAcceptNewClanWhenDismissed")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeAcceptNewClanWhenDismissed();
	
	@Key("DaysBeforeCreateNewAllyWhenDissolved")
	@ConverterClass(Days2MillisecondsConverter.class)
	long getDaysBeforeCreateNewAllyWhenDissolved();
	
	@Key("MaxNumOfClansInAlly")
	int getMaxNumOfClansInAlly();
	
	@Key("MembersCanWithdrawFromClanWH")
	boolean membersCanWithdrawFromClanWH();
	
	@Key("RemoveCastleCirclets")
	boolean removeCastleCirclets();
	
	@Key("ClanMembersForWar")
	int getClanMembersForWar();
	
	// Party
	
	@Key("PartyRange")
	int getPartyRange();
	
	@Key("PartyRange2")
	int getPartyRange2();
	
	@Key("PartyEvenlyDistributeAllStackableItems")
	boolean getPartyEvenlyDistributeAllStackableItems();
	
	@Key("PartyEvenlyDistributeAllOtherItems")
	boolean getPartyEvenlyDistributeAllOtherItems();
	
	@Key("PartyEvenlyDistributeItems")
	List<Integer> getPartyEvenlyDistributeItems();
	
	@Key("LeavePartyLeader")
	boolean leavePartyLeader();
	
	// Initial
	
	@Key("InitialEquipmentEvent")
	boolean initialEquipmentEvent();
	
	@Key("StartingAdena")
	int getStartingAdena();
	
	@Key("StartingLevel")
	int getStartingLevel();
	
	@Key("StartingSP")
	int getStartingSP();
	
	// Other
	
	@Key("MaxAdena")
	long getMaxAdena();
	
	@Key("AutoLoot")
	boolean autoLoot();
	
	@Key("AutoLootRaids")
	boolean autoLootRaids();
	
	@Key("RaidLootRightsInterval")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getRaidLootRightsInterval();
	
	@Key("RaidLootRightsCCSize")
	int getRaidLootRightsCCSize();
	
	@Key("UnstuckInterval")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getUnstuckInterval();
	
	@Key("TeleportWatchdogTimeout")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	long getTeleportWatchdogTimeout();
	
	@Key("PlayerSpawnProtection")
	int getPlayerSpawnProtection();
	
	@Key("PlayerSpawnProtectionAllowedItems")
	Set<Integer> getPlayerSpawnProtectionAllowedItems();
	
	@Key("PlayerTeleportProtection")
	int getPlayerTeleportProtection();
	
	@Key("RandomRespawnInTown")
	boolean randomRespawnInTown();
	
	@Key("OffsetOnTeleport")
	boolean offsetOnTeleport();
	
	@Key("MaxOffsetOnTeleport")
	int getMaxOffsetOnTeleport();
	
	@Key("PetitioningAllowed")
	boolean petitioningAllowed();
	
	@Key("MaxPetitionsPerPlayer")
	int getMaxPetitionsPerPlayer();
	
	@Key("MaxPetitionsPending")
	int getMaxPetitionsPending();
	
	@Key("FreeTeleporting")
	boolean freeTeleporting();
	
	@Key("DeleteCharAfterDays")
	int getDeleteCharAfterDays();
	
	@Key("ExponentXp")
	int getExponentXp();
	
	@Key("ExponentSp")
	int getExponentSp();
	
	@Key("PartyXpCutoffMethod")
	String getPartyXpCutoffMethod();
	
	@Key("PartyXpCutoffPercent")
	double getPartyXpCutoffPercent();
	
	@Key("PartyXpCutoffLevel")
	int getPartyXpCutoffLevel();
	
	@Key("PartyXpCutoffGaps")
	@ConverterClass(MapIntegerIntegerConverter.class)
	Map<Integer, Integer> getPartyXpCutoffGaps();
	
	@Key("PartyXpCutoffGapPercent")
	List<Integer> getPartyXpCutoffGapPercent();
	
	@Key("Tutorial")
	boolean tutorial();
	
	@Key("ExpertisePenalty")
	boolean expertisePenalty();
	
	@Key("StoreUISettings")
	boolean storeUISettings();
	
	@Key("SilenceModeExclude")
	boolean silenceModeExclude();
	
	@Key("ValidateTriggerSkills")
	boolean validateTriggerSkills();
	
	@Key("PlayerNameTemplate")
	@ConverterClass(PatternConverter.class)
	Pattern getPlayerNameTemplate();
	
	@Key("PetNameTemplate")
	@ConverterClass(PatternConverter.class)
	Pattern getPetNameTemplate();
	
	@Key("ForbiddenNames")
	Set<String> getForbiddenNames();
	
	@Key("CharMaxNumber")
	int getCharMaxNumber();
}