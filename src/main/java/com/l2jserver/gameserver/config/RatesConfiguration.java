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

import java.util.Map;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.MapIntegerFloatConverter;

/**
 * Rates Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/rates.properties",
	"file:./config/rates.properties",
	"classpath:config/rates.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface RatesConfiguration extends Reloadable {
	
	@Key("DeathDropAmountMultiplier")
	double getDeathDropAmountMultiplier();
	
	@Key("CorpseDropAmountMultiplier")
	double getCorpseDropAmountMultiplier();
	
	@Key("HerbDropAmountMultiplier")
	double getHerbDropAmountMultiplier();
	
	@Key("RaidDropAmountMultiplier")
	double getRaidDropAmountMultiplier();
	
	@Key("DeathDropChanceMultiplier")
	double getDeathDropChanceMultiplier();
	
	@Key("CorpseDropChanceMultiplier")
	double getCorpseDropChanceMultiplier();
	
	@Key("HerbDropChanceMultiplier")
	double getHerbDropChanceMultiplier();
	
	@Key("RaidDropChanceMultiplier")
	double getRaidDropChanceMultiplier();
	
	@Key("DropAmountMultiplierByItemId")
	@ConverterClass(MapIntegerFloatConverter.class)
	Map<Integer, Float> getDropAmountMultiplierByItemId();
	
	@Key("DropChanceMultiplierByItemId")
	@ConverterClass(MapIntegerFloatConverter.class)
	Map<Integer, Float> getDropChanceMultiplierByItemId();
	
	@Key("RateXp")
	float getRateXp();
	
	@Key("RateSp")
	float getRateSp();
	
	@Key("RatePartyXp")
	float getRatePartyXp();
	
	@Key("RatePartySp")
	float getRatePartySp();
	
	// TODO(Zoey76): Should this be int instead of float?
	@Key("RateDropManor")
	int getRateDropManor();
	
	@Key("RateKarmaLost")
	double getRateKarmaLost();
	
	@Key("RateKarmaExpLost")
	double getRateKarmaExpLost();
	
	@Key("RateSiegeGuardsPrice")
	double getRateSiegeGuardsPrice();
	
	@Key("RateExtractable")
	float getRateExtractable();
	
	@Key("RateHellboundTrustIncrease")
	float getRateHellboundTrustIncrease();
	
	@Key("RateHellboundTrustDecrease")
	float getRateHellboundTrustDecrease();
	
	@Key("QuestDropChanceMultiplier")
	float getQuestDropChanceMultiplier();
	
	@Key("QuestDropAmountMultiplier")
	float getQuestDropAmountMultiplier();
	
	@Key("RateQuestRewardXP")
	float getRateQuestRewardXP();
	
	@Key("RateQuestRewardSP")
	float getRateQuestRewardSP();
	
	@Key("RateQuestRewardAdena")
	float getRateQuestRewardAdena();
	
	@Key("UseQuestRewardMultipliers")
	boolean useQuestRewardMultipliers();
	
	@Key("RateQuestReward")
	float getRateQuestReward();
	
	@Key("RateQuestRewardPotion")
	float getRateQuestRewardPotion();
	
	@Key("RateQuestRewardScroll")
	float getRateQuestRewardScroll();
	
	@Key("RateQuestRewardRecipe")
	float getRateQuestRewardRecipe();
	
	@Key("RateQuestRewardMaterial")
	float getRateQuestRewardMaterial();
	
	@Key("PlayerDropLimit")
	int getPlayerDropLimit();
	
	@Key("PlayerRateDrop")
	int getPlayerRateDrop();
	
	@Key("PlayerRateDropItem")
	int getPlayerRateDropItem();
	
	@Key("PlayerRateDropEquip")
	int getPlayerRateDropEquip();
	
	@Key("PlayerRateDropEquipWeapon")
	int getPlayerRateDropEquipWeapon();
	
	@Key("KarmaDropLimit")
	int getKarmaDropLimit();
	
	@Key("KarmaRateDrop")
	int getKarmaRateDrop();
	
	@Key("KarmaRateDropItem")
	int getKarmaRateDropItem();
	
	@Key("KarmaRateDropEquip")
	int getKarmaRateDropEquip();
	
	@Key("KarmaRateDropEquipWeapon")
	int getKarmaRateDropEquipWeapon();
	
	@Key("PetXpRate")
	double getPetXpRate();
	
	// TODO(Zoey76): Should PetFoodRate be float?
	@Key("PetFoodRate")
	int getPetFoodRate();
	
	@Key("SinEaterXpRate")
	double getSinEaterXpRate();
}