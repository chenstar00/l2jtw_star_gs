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

import com.l2jserver.gameserver.config.converter.MapIntegerIntegerConverter;

/**
 * NPC Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/npc.properties",
	"file:./config/npc.properties",
	"classpath:config/npc.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface NPCConfiguration extends Reloadable {
	
	@Key("AnnounceMammonSpawn")
	boolean announceMammonSpawn();
	
	@Key("MobAggroInPeaceZone")
	boolean mobAggroInPeaceZone();
	
	@Key("AttackableNpcs")
	boolean attackableNpcs();
	
	@Key("ViewNpc")
	boolean viewNpc();
	
	@Key("MaxDriftRange")
	int getMaxDriftRange();
	
	@Key("ShowNpcLevel")
	boolean showNpcLevel();
	
	@Key("ShowCrestWithoutQuest")
	boolean showCrestWithoutQuest();
	
	@Key("RandomEnchantEffect")
	boolean randomEnchantEffect();
	
	@Key("MinNPCLevelForDmgPenalty")
	int getMinNPCLevelForDmgPenalty();
	
	@Key("DmgPenaltyForLvLDifferences")
	List<Double> getDmgPenaltyForLvLDifferences();
	
	@Key("CritDmgPenaltyForLvLDifferences")
	List<Double> getCritDmgPenaltyForLvLDifferences();
	
	@Key("SkillDmgPenaltyForLvLDifferences")
	List<Double> getSkillDmgPenaltyForLvLDifferences();
	
	// TODO(Zoey76): Implement MinNPCLevelForMagicPenalty configuration.
	@Key("MinNPCLevelForMagicPenalty")
	int getMinNPCLevelForMagicPenalty();
	
	@Key("SkillChancePenaltyForLvLDifferences")
	List<Double> getSkillChancePenaltyForLvLDifferences();
	
	// Monsters
	
	// TODO(Zoey76): Implement DecayTimeTask configuration.
	@Key("DecayTimeTask")
	int getDecayTimeTask();
	
	@Key("DefaultCorpseTime")
	int getDefaultCorpseTime();
	
	@Key("SpoiledCorpseExtendTime")
	int getSpoiledCorpseExtendTime();
	
	@Key("CorpseConsumeSkillAllowedTimeBeforeDecay")
	int getCorpseConsumeSkillAllowedTimeBeforeDecay();
	
	// Guards
	
	@Key("GuardAttackAggroMob")
	boolean guardAttackAggroMob();
	
	// Pets
	
	// TODO(Zoey76): Implement AllowWyvernUpgrader configuration.
	@Key("AllowWyvernUpgrader")
	int allowWyvernUpgrader();
	
	@Key("PetRentNPCs")
	Set<Integer> getPetRentNPCs();
	
	@Key("MaximumSlotsForPet")
	int getMaximumSlotsForPet();
	
	@Key("PetHpRegenMultiplier")
	double getPetHpRegenMultiplier();
	
	@Key("PetMpRegenMultiplier")
	double getPetMpRegenMultiplier();
	
	// Raid Bosses
	
	@Key("RaidHpRegenMultiplier")
	double getRaidHpRegenMultiplier();
	
	@Key("RaidMpRegenMultiplier")
	double getRaidMpRegenMultiplier();
	
	@Key("RaidPDefenceMultiplier")
	double getRaidPDefenceMultiplier();
	
	@Key("RaidMDefenceMultiplier")
	double getRaidMDefenceMultiplier();
	
	@Key("RaidPAttackMultiplier")
	double getRaidPAttackMultiplier();
	
	@Key("RaidMAttackMultiplier")
	double getRaidMAttackMultiplier();
	
	@Key("RaidMinRespawnMultiplier")
	double getRaidMinRespawnMultiplier();
	
	@Key("RaidMaxRespawnMultiplier")
	double getRaidMaxRespawnMultiplier();
	
	@Key("RaidMinionRespawnTime")
	long getRaidMinionRespawnTime();
	
	@Key("CustomMinionsRespawnTime")
	@ConverterClass(MapIntegerIntegerConverter.class)
	Map<Integer, Integer> getCustomMinionsRespawnTime();
	
	@Key("RaidCurse")
	boolean raidCurse();
	
	@Key("RaidChaosTime")
	int getRaidChaosTime();
	
	@Key("GrandChaosTime")
	int getGrandChaosTime();
	
	@Key("MinionChaosTime")
	int getMinionChaosTime();
	
	// Drops
	
	@Key("UseDeepBlueDropRules")
	boolean useDeepBlueDropRules();
	
	@Key("UseDeepBlueDropRulesRaid")
	boolean useDeepBlueDropRulesRaid();
	
	@Key("DropAdenaMinLevelDifference")
	int getDropAdenaMinLevelDifference();
	
	@Key("DropAdenaMaxLevelDifference")
	int getDropAdenaMaxLevelDifference();
	
	@Key("DropAdenaMinLevelGapChance")
	int getDropAdenaMinLevelGapChance();
	
	@Key("DropItemMinLevelDifference")
	int getDropItemMinLevelDifference();
	
	@Key("DropItemMaxLevelDifference")
	int getDropItemMaxLevelDifference();
	
	@Key("DropItemMinLevelGapChance")
	int getDropItemMinLevelGapChance();
	
	@Key("MaxAggroRange")
	int getMaxAggroRange();
}