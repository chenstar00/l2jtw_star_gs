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

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

/**
 * Flood Protector Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/floodprotector.properties",
	"file:./config/floodprotector.properties",
	"classpath:config/floodprotector.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface FloodProtectorConfiguration extends Accessible, Reloadable {
	
	// TODO(Zoey76): Implement all these configurations.
	
	@Key("UseItemInterval")
	int getUseItemInterval();
	
	@Key("UseItemLogFlooding")
	boolean useItemLogFlooding();
	
	@Key("UseItemPunishmentLimit")
	int getUseItemPunishmentLimit();
	
	@Key("UseItemPunishmentType")
	String getUseItemPunishmentType();
	
	@Key("UseItemPunishmentTime")
	int getUseItemPunishmentTime();
	
	@Key("RollDiceInterval")
	int getRollDiceInterval();
	
	@Key("RollDiceLogFlooding")
	boolean rollDiceLogFlooding();
	
	@Key("RollDicePunishmentLimit")
	int getRollDicePunishmentLimit();
	
	@Key("RollDicePunishmentType")
	String getRollDicePunishmentType();
	
	@Key("RollDicePunishmentTime")
	int getRollDicePunishmentTime();
	
	@Key("FireworkInterval")
	int getFireworkInterval();
	
	@Key("FireworkLogFlooding")
	boolean fireworkLogFlooding();
	
	@Key("FireworkPunishmentLimit")
	int getFireworkPunishmentLimit();
	
	@Key("FireworkPunishmentType")
	String getFireworkPunishmentType();
	
	@Key("FireworkPunishmentTime")
	int getFireworkPunishmentTime();
	
	@Key("ItemPetSummonInterval")
	int geItemPetSummonInterval();
	
	@Key("ItemPetSummonLogFlooding")
	boolean itemPetSummonLogFlooding();
	
	@Key("ItemPetSummonPunishmentLimit")
	int getItemPetSummonPunishmentLimit();
	
	@Key("ItemPetSummonPunishmentType")
	String getItemPetSummonPunishmentType();
	
	@Key("ItemPetSummonPunishmentTime")
	int getItemPetSummonPunishmentTime();
	
	@Key("HeroVoiceInterval")
	int getHeroVoiceInterval();
	
	@Key("HeroVoiceLogFlooding")
	boolean heroVoiceLogFlooding();
	
	@Key("HeroVoicePunishmentLimit")
	int getHeroVoicePunishmentLimit();
	
	@Key("HeroVoicePunishmentType")
	String getHeroVoicePunishmentType();
	
	@Key("HeroVoicePunishmentTime")
	int getHeroVoicePunishmentTime();
	
	@Key("GlobalChatInterval")
	int getGlobalChatInterval();
	
	@Key("GlobalChatLogFlooding")
	boolean globalChatLogFlooding();
	
	@Key("GlobalChatPunishmentLimit")
	int getGlobalChatPunishmentLimit();
	
	@Key("GlobalChatPunishmentType")
	String getGlobalChatPunishmentType();
	
	@Key("GlobalChatPunishmentTime")
	int getGlobalChatPunishmentTime();
	
	@Key("SubclassInterval")
	int getSubclassInterval();
	
	@Key("SubclassLogFlooding")
	boolean subclassLogFlooding();
	
	@Key("SubclassPunishmentLimit")
	int getSubclassPunishmentLimit();
	
	@Key("SubclassPunishmentType")
	String getSubclassPunishmentType();
	
	@Key("SubclassPunishmentTime")
	int getSubclassPunishmentTime();
	
	@Key("DropItemInterval")
	int getDropItemInterval();
	
	@Key("DropItemLogFlooding")
	boolean dropItemLogFlooding();
	
	@Key("DropItemPunishmentLimit")
	int getDropItemPunishmentLimit();
	
	@Key("DropItemPunishmentType")
	String getDropItemPunishmentType();
	
	@Key("DropItemPunishmentTime")
	int getDropItemPunishmentTime();
	
	@Key("ServerBypassInterval")
	int getServerBypassInterval();
	
	@Key("ServerBypassLogFlooding")
	boolean serverBypassLogFlooding();
	
	@Key("ServerBypassPunishmentLimit")
	int getServerBypassPunishmentLimit();
	
	@Key("ServerBypassPunishmentType")
	String getServerBypassPunishmentType();
	
	@Key("ServerBypassPunishmentTime")
	int getServerBypassPunishmentTime();
	
	@Key("MultiSellInterval")
	int getMultiSellInterval();
	
	@Key("MultiSellLogFlooding")
	boolean multiSellLogFlooding();
	
	@Key("MultiSellPunishmentLimit")
	int getMultiSellPunishmentLimit();
	
	@Key("MultiSellPunishmentType")
	String getMultiSellPunishmentType();
	
	@Key("MultiSellPunishmentTime")
	int getMultiSellPunishmentTime();
	
	@Key("TransactionInterval")
	int getTransactionInterval();
	
	@Key("TransactionLogFlooding")
	boolean transactionLogFlooding();
	
	@Key("TransactionPunishmentLimit")
	int getTransactionPunishmentLimit();
	
	@Key("TransactionPunishmentType")
	String getTransactionPunishmentType();
	
	@Key("TransactionPunishmentTime")
	int getTransactionPunishmentTime();
	
	@Key("ManufactureInterval")
	int getManufactureInterval();
	
	@Key("ManufactureLogFlooding")
	boolean manufactureLogFlooding();
	
	@Key("ManufacturePunishmentLimit")
	int getManufacturePunishmentLimit();
	
	@Key("ManufacturePunishmentType")
	String getManufacturePunishmentType();
	
	@Key("ManufacturePunishmentTime")
	int getManufacturePunishmentTime();
	
	@Key("ManorInterval")
	int getManorInterval();
	
	@Key("ManorLogFlooding")
	boolean manorLogFlooding();
	
	@Key("ManorPunishmentLimit")
	int getManorPunishmentLimit();
	
	@Key("ManorPunishmentType")
	String getManorPunishmentType();
	
	@Key("ManorPunishmentTime")
	int getManorPunishmentTime();
	
	@Key("SendMailInterval")
	int getSendMailInterval();
	
	@Key("SendMailLogFlooding")
	boolean sendMailLogFlooding();
	
	@Key("SendMailPunishmentLimit")
	int getSendMailPunishmentLimit();
	
	@Key("SendMailPunishmentType")
	String getSendMailPunishmentType();
	
	@Key("SendMailPunishmentTime")
	int getSendMailPunishmentTime();
	
	@Key("CharacterSelectInterval")
	int getCharacterSelectInterval();
	
	@Key("CharacterSelectLogFlooding")
	boolean characterSelectLogFlooding();
	
	@Key("CharacterSelectPunishmentLimit")
	int getCharacterSelectPunishmentLimit();
	
	@Key("CharacterSelectPunishmentType")
	String getCharacterSelectPunishmentType();
	
	@Key("CharacterSelectPunishmentTime")
	int getCharacterSelectPunishmentTime();
	
	@Key("ItemAuctionInterval")
	int getItemAuctionInterval();
	
	@Key("ItemAuctionLogFlooding")
	boolean itemAuctionLogFlooding();
	
	@Key("ItemAuctionPunishmentLimit")
	int getItemAuctionPunishmentLimit();
	
	@Key("ItemAuctionPunishmentType")
	String getItemAuctionPunishmentType();
	
	@Key("ItemAuctionPunishmentTime")
	int getItemAuctionPunishmentTime();
}