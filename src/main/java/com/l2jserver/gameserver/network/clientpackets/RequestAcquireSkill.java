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
package com.l2jserver.gameserver.network.clientpackets;

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.model.base.AcquireSkillType.SUBPLEDGE;
import static com.l2jserver.gameserver.network.SystemMessageId.ACQUIRE_SKILL_FAILED_BAD_CLAN_REP_SCORE;
import static com.l2jserver.gameserver.network.SystemMessageId.ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL;
import static com.l2jserver.gameserver.network.SystemMessageId.LEARNED_SKILL_S1;
import static com.l2jserver.gameserver.network.SystemMessageId.NOT_COMPLETED_QUEST_FOR_SKILL_ACQUISITION;
import static com.l2jserver.gameserver.network.SystemMessageId.NOT_ENOUGH_SP_TO_LEARN_SKILL;
import static com.l2jserver.gameserver.network.SystemMessageId.NO_MORE_SKILLS_TO_LEARN;
import static com.l2jserver.gameserver.network.SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP;
import static com.l2jserver.gameserver.network.SystemMessageId.S2_S1_DISAPPEARED;
import static com.l2jserver.gameserver.network.SystemMessageId.YOU_DONT_MEET_SKILL_LEVEL_REQUIREMENTS;
import static com.l2jserver.gameserver.network.SystemMessageId.YOU_MUST_LEARN_ONYX_BEAST_SKILL;
import static com.l2jserver.gameserver.network.serverpackets.StatusUpdate.SP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.enums.IllegalActionPunishmentType;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2VillageMasterInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSkillLearned;
import com.l2jserver.gameserver.model.skills.CommonSkill;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillDone;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.ExStorageMaxCount;
import com.l2jserver.gameserver.network.serverpackets.PledgeSkillList;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Request Acquire Skill client packet implementation.
 * @author Zoey76
 */
public final class RequestAcquireSkill extends L2GameClientPacket {
	
	private static final Logger LOG = LoggerFactory.getLogger(RequestAcquireSkill.class);
	
	private static final String _C__7C_REQUESTACQUIRESKILL = "[C] 7C RequestAcquireSkill";
	
	private static final String[] QUEST_VAR_NAMES = {
		"EmergentAbility65-",
		"EmergentAbility70-",
		"ClassAbility75-",
		"ClassAbility80-"
	};
	
	private int _id;
	private int _level;
	private AcquireSkillType _skillType;
	private int _subType;
	
	@Override
	protected void readImpl() {
		_id = readD();
		_level = readD();
		_skillType = AcquireSkillType.getAcquireSkillType(readD());
		if (_skillType == SUBPLEDGE) {
			_subType = readD();
		}
	}
	
	@Override
	protected void runImpl() {
		final var activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		if ((_level < 1) || (_level > 1000) || (_id < 1) || (_id > 32000)) {
			Util.handleIllegalPlayerAction(activeChar, "Wrong Packet Data in Acquired Skill");
			LOG.warn("Received wrong packet data from player {} with skill id: {} and level: {}!" + _level, activeChar, _id, _level);
			return;
		}
		
		final var trainer = activeChar.getLastFolkNPC();
		if (!(trainer instanceof L2NpcInstance)) {
			return;
		}
		
		if (!trainer.canInteract(activeChar) && !activeChar.isGM()) {
			return;
		}
		
		final var skill = SkillData.getInstance().getSkill(_id, _level);
		if (skill == null) {
			LOG.warn("Player {} is trying to learn an invalid skill Id: {} level: {}!", activeChar, _id, _level);
			return;
		}
		
		final var skillLearn = SkillTreesData.getInstance().getSkillLearn(_skillType, _id, _level, activeChar);
		if (!canBeLearn(activeChar, skill, skillLearn)) {
			return;
		}
		
		switch (_skillType) {
			case CLASS -> {
				if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
					giveSkill(activeChar, trainer, skill);
				}
			}
			case TRANSFORM -> {
				// Hack check.
				if (!canTransform(activeChar)) {
					activeChar.sendPacket(NOT_COMPLETED_QUEST_FOR_SKILL_ACQUISITION);
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without required quests!", IllegalActionPunishmentType.NONE);
					return;
				}
				
				if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
					giveSkill(activeChar, trainer, skill);
				}
			}
			case FISHING -> {
				if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
					giveSkill(activeChar, trainer, skill);
				}
			}
			case PLEDGE -> {
				if (!activeChar.isClanLeader()) {
					return;
				}
				
				final var clan = activeChar.getClan();
				int repCost = skillLearn.getLevelUpSp();
				if (clan.getReputationScore() >= repCost) {
					if (character().lifeCrystalNeeded()) {
						for (var item : skillLearn.getRequiredItems()) {
							if (!activeChar.destroyItemByItemId("Consume", item.getId(), item.getCount(), trainer, false)) {
								// Doesn't have required item.
								activeChar.sendPacket(ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
								L2VillageMasterInstance.showPledgeSkillList(activeChar);
								return;
							}
							
							final var sm = SystemMessage.getSystemMessage(S2_S1_DISAPPEARED);
							sm.addItemName(item.getId());
							sm.addLong(item.getCount());
							activeChar.sendPacket(sm);
						}
					}
					
					clan.takeReputationScore(repCost, true);
					
					final var cr = SystemMessage.getSystemMessage(S1_DEDUCTED_FROM_CLAN_REP);
					cr.addInt(repCost);
					activeChar.sendPacket(cr);
					
					clan.addNewSkill(skill);
					
					clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
					
					activeChar.sendPacket(new AcquireSkillDone());
					
					L2VillageMasterInstance.showPledgeSkillList(activeChar);
				} else {
					activeChar.sendPacket(ACQUIRE_SKILL_FAILED_BAD_CLAN_REP_SCORE);
					L2VillageMasterInstance.showPledgeSkillList(activeChar);
				}
			}
			case SUBPLEDGE -> {
				final var clan = activeChar.getClan();
				final int repCost = skillLearn.getLevelUpSp();
				if (clan.getReputationScore() < repCost) {
					activeChar.sendPacket(ACQUIRE_SKILL_FAILED_BAD_CLAN_REP_SCORE);
					return;
				}
				
				for (var item : skillLearn.getRequiredItems()) {
					if (!activeChar.destroyItemByItemId("SubSkills", item.getId(), item.getCount(), trainer, false)) {
						activeChar.sendPacket(ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
						return;
					}
					
					final var sm = SystemMessage.getSystemMessage(S2_S1_DISAPPEARED);
					sm.addItemName(item.getId());
					sm.addLong(item.getCount());
					activeChar.sendPacket(sm);
				}
				
				if (repCost > 0) {
					clan.takeReputationScore(repCost, true);
					final var cr = SystemMessage.getSystemMessage(S1_DEDUCTED_FROM_CLAN_REP);
					cr.addInt(repCost);
					activeChar.sendPacket(cr);
				}
				
				clan.addNewSkill(skill, _subType);
				clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
				activeChar.sendPacket(new AcquireSkillDone());
				
				showSubUnitSkillList(activeChar);
			}
			case TRANSFER -> {
				if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
					giveSkill(activeChar, trainer, skill);
				}
			}
			case SUBCLASS -> {
				var st = activeChar.getQuestState("SubClassSkills");
				if (st == null) {
					final var subClassSkillsQuest = QuestManager.getInstance().getQuest("SubClassSkills");
					if (subClassSkillsQuest != null) {
						st = subClassSkillsQuest.newQuestState(activeChar);
					} else {
						LOG.warn("Player {} does not have sub-class quest for skill Id: {} level: {}!", activeChar, _id, _level);
						return;
					}
				}
				
				for (var varName : QUEST_VAR_NAMES) {
					for (int i = 1; i <= character().getMaxSubclass(); i++) {
						final var itemOID = st.getGlobalQuestVar(varName + i);
						if (!itemOID.isEmpty() && !itemOID.endsWith(";") && !itemOID.equals("0")) {
							if (Util.isDigit(itemOID)) {
								final int itemObjId = Integer.parseInt(itemOID);
								final var item = activeChar.getInventory().getItemByObjectId(itemObjId);
								if (item != null) {
									for (var itemIdCount : skillLearn.getRequiredItems()) {
										if (item.getId() == itemIdCount.getId()) {
											if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
												giveSkill(activeChar, trainer, skill);
												// Logging the given skill.
												st.saveGlobalQuestVar(varName + i, skill.getId() + ";");
											}
											return;
										}
									}
								} else {
									LOG.warn("Nonexistent item for object Id " + itemObjId + ", for Sub-Class skill Id: " + _id + " level: " + _level + " for player " + activeChar.getName() + "!");
								}
							} else {
								LOG.warn("Invalid item object Id " + itemOID + ", for Sub-Class skill Id: " + _id + " level: " + _level + " for player " + activeChar.getName() + "!");
							}
						}
					}
				}
				
				// Player doesn't have required item.
				activeChar.sendPacket(ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
				// TODO: showSkillList(trainer, activeChar);
			}
			case COLLECT -> {
				if (checkPlayerSkill(activeChar, trainer, skillLearn)) {
					giveSkill(activeChar, trainer, skill);
				}
			}
			default -> LOG.warn("Received wrong packet data in Acquired Skill, unknown skill type {}!", _skillType);
		}
	}
	
	/**
	 * Check if player try to exploit by add extra levels on skill learn.
	 * @param activeChar the player
	 * @param skill the skill
	 * @param skl the skill for learn
	 * @return {@code true} if skill id and level is fine, otherwise {@code false}
	 */
	private boolean canBeLearn(L2PcInstance activeChar, Skill skill, L2SkillLearn skl) {
		final int prevSkillLevel = activeChar.getSkillLevel(_id);
		switch (_skillType) {
			case SUBPLEDGE: {
				final var clan = activeChar.getClan();
				if (clan == null) {
					return false;
				}
				
				if (!activeChar.isClanLeader() || !activeChar.hasClanPrivilege(ClanPrivilege.CL_TROOPS_FAME)) {
					return false;
				}
				
				if ((clan.getFortId() == 0) && (clan.getCastleId() == 0)) {
					return false;
				}
				
				if (!clan.isLearnableSubPledgeSkill(skill, _subType)) {
					activeChar.sendPacket(SystemMessageId.SQUAD_SKILL_ALREADY_ACQUIRED);
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without knowing it's previous level!");
					return false;
				}
				break;
			}
			case TRANSFER: {
				if (skl == null) {
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting transfer skill Id: " + _id + " level " + _level + " what is not included in transfer skills!");
				}
				break;
			}
			case SUBCLASS: {
				if (activeChar.isSubClassActive()) {
					activeChar.sendPacket(SystemMessageId.SKILL_NOT_FOR_SUBCLASS);
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " while Sub-Class is active!");
					return false;
				}
			}
			default: {
				if (prevSkillLevel == _level) {
					LOG.warn("Player {} is trying to learn a skill that already knows, Id: {} level: {}!", activeChar, _id, _level);
					return false;
				}
				if ((_level != 1) && (prevSkillLevel != (_level - 1))) {
					activeChar.sendPacket(SystemMessageId.PREVIOUS_LEVEL_SKILL_NOT_LEARNED);
					Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " is requesting skill Id: " + _id + " level " + _level + " without knowing it's previous level!");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void showSubUnitSkillList(L2PcInstance activeChar) {
		final var skills = SkillTreesData.getInstance().getAvailableSubPledgeSkills(activeChar.getClan());
		if (skills.size() == 0) {
			activeChar.sendPacket(NO_MORE_SKILLS_TO_LEARN);
		} else {
			activeChar.sendPacket(new AcquireSkillList(SUBPLEDGE, skills));
		}
	}
	
	/**
	 * Perform a simple check for current player and skill.<br>
	 * Takes the needed SP if the skill require it and all requirements are meet.<br>
	 * Consume required items if the skill require it and all requirements are meet.<br>
	 * @param player the skill learning player.
	 * @param trainer the skills teaching Npc.
	 * @param s the skill to be learn.
	 * @return {@code true} if all requirements are meet, {@code false} otherwise.
	 */
	private boolean checkPlayerSkill(L2PcInstance player, L2Npc trainer, L2SkillLearn s) {
		if (s != null) {
			if ((s.getSkillId() == _id) && (s.getSkillLevel() == _level)) {
				// Hack check.
				if (s.getGetLevel() > player.getLevel()) {
					player.sendPacket(YOU_DONT_MEET_SKILL_LEVEL_REQUIREMENTS);
					Util.handleIllegalPlayerAction(player, "Player " + player.getName() + ", level " + player.getLevel() + " is requesting skill Id: " + _id + " level " + _level + " without having minimum required level, " + s.getGetLevel() + "!", IllegalActionPunishmentType.NONE);
					return false;
				}
				
				// First it checks that the skill require SP and the player has enough SP to learn it.
				final int levelUpSp = s.getCalculatedLevelUpSp(player.getClassId(), player.getLearningClass());
				if ((levelUpSp > 0) && (levelUpSp > player.getSp())) {
					player.sendPacket(NOT_ENOUGH_SP_TO_LEARN_SKILL);
					// TODO: showSkillList(trainer, player);
					return false;
				}
				
				if (!character().divineInspirationSpBookNeeded() && (_id == CommonSkill.DIVINE_INSPIRATION.getId())) {
					return true;
				}
				
				// Check for required skills.
				if (!s.getPreReqSkills().isEmpty()) {
					for (var skill : s.getPreReqSkills()) {
						if (player.getSkillLevel(skill.getSkillId()) != skill.getSkillLvl()) {
							if (skill.getSkillId() == CommonSkill.ONYX_BEAST_TRANSFORMATION.getId()) {
								player.sendPacket(YOU_MUST_LEARN_ONYX_BEAST_SKILL);
							} else {
								player.sendPacket(ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
							}
							return false;
						}
					}
				}
				
				// Check for required items.
				if (!s.getRequiredItems().isEmpty()) {
					// Then checks that the player has all the items
					for (var item : s.getRequiredItems()) {
						final var reqItemCount = player.getInventory().getInventoryItemCount(item.getId(), -1);
						if (reqItemCount < item.getCount()) {
							// Player doesn't have required item.
							player.sendPacket(ITEM_OR_PREREQUISITES_MISSING_TO_LEARN_SKILL);
							// TODO: showSkillList(trainer, player);
							return false;
						}
					}
					// If the player has all required items, they are consumed.
					for (var itemIdCount : s.getRequiredItems()) {
						if (!player.destroyItemByItemId("SkillLearn", itemIdCount.getId(), itemIdCount.getCount(), trainer, true)) {
							Util.handleIllegalPlayerAction(player, "Somehow player " + player.getName() + ", level " + player.getLevel() + " lose required item Id: " + itemIdCount.getId() + " to learn skill while learning skill Id: " + _id + " level " + _level
								+ "!", IllegalActionPunishmentType.NONE);
						}
					}
				}
				// If the player has SP and all required items then consume SP.
				if (levelUpSp > 0) {
					player.setSp(player.getSp() - levelUpSp);
					final var su = new StatusUpdate(player);
					su.addAttribute(SP, player.getSp());
					player.sendPacket(su);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add the skill to the player and makes proper updates.
	 * @param player the player acquiring a skill.
	 * @param trainer the Npc teaching a skill.
	 * @param skill the skill to be learn.
	 */
	private void giveSkill(L2PcInstance player, L2Npc trainer, Skill skill) {
		// Send message.
		final var sm = SystemMessage.getSystemMessage(LEARNED_SKILL_S1);
		sm.addSkillName(skill);
		player.sendPacket(sm);
		
		player.sendPacket(new AcquireSkillDone());
		player.addSkill(skill, true);
		player.sendSkillList();
		player.updateShortCuts(_id, _level);
		
		// If skill is expand type then sends packet:
		if ((_id >= 1368) && (_id <= 1372)) {
			player.sendPacket(new ExStorageMaxCount(player));
		}
		
		// Notify scripts of the skill learn.
		EventDispatcher.getInstance().notifyEventAsync(new PlayerSkillLearned(trainer, player, skill, _skillType), trainer);
	}
	
	/**
	 * Verify if the player can transform.
	 * @param player the player to verify
	 * @return {@code true} if the player meets the required conditions to learn a transformation, {@code false} otherwise
	 */
	public static boolean canTransform(L2PcInstance player) {
		if (character().transformationWithoutQuest()) {
			return true;
		}
		return player.hasQuestCompleted("Q00136_MoreThanMeetsTheEye");
	}
	
	@Override
	public String getType() {
		return _C__7C_REQUESTACQUIRESKILL;
	}
}
