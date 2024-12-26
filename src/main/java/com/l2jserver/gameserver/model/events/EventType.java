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
package com.l2jserver.gameserver.model.events;

import com.l2jserver.gameserver.model.events.impl.BaseEvent;
import com.l2jserver.gameserver.model.events.impl.character.CreatureAttack;
import com.l2jserver.gameserver.model.events.impl.character.CreatureAttackAvoid;
import com.l2jserver.gameserver.model.events.impl.character.CreatureAttacked;
import com.l2jserver.gameserver.model.events.impl.character.CreatureDamageDealt;
import com.l2jserver.gameserver.model.events.impl.character.CreatureDamageReceived;
import com.l2jserver.gameserver.model.events.impl.character.CreatureKill;
import com.l2jserver.gameserver.model.events.impl.character.CreatureSkillUse;
import com.l2jserver.gameserver.model.events.impl.character.CreatureTeleported;
import com.l2jserver.gameserver.model.events.impl.character.CreatureZoneEnter;
import com.l2jserver.gameserver.model.events.impl.character.CreatureZoneExit;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcCanBeSeen;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcCreatureSee;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcEventReceived;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcFirstTalk;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcManorBypass;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcMoveFinished;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcMoveNodeArrived;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcMoveRouteFinished;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSkillFinished;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSkillSee;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSpawn;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcTeleport;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableAggroRangeEnter;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableAttack;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableFactionCall;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableHate;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.AttackableKill;
import com.l2jserver.gameserver.model.events.impl.character.playable.PlayableExpChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerAugment;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerBypass;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerChat;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerCreate;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerDelete;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerDlgAnswer;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerEquipItem;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerFameChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerHennaAdd;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerHennaRemove;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerKarmaChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLearnSkillRequested;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLevelChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLogin;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLogout;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerOneSkillSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerPKChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerProfessionCancel;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerProfessionChange;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerPvPChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerPvPKill;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerQuestAccepted;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerRestore;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSelect;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSit;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSkillLearned;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSummonSpawn;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSummonTalk;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTransform;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorial;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialClientEvent;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialCmd;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialQuestionMark;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanCreate;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanDestroy;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanJoin;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanLeaderChange;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanLeave;
import com.l2jserver.gameserver.model.events.impl.character.player.clan.PlayerClanLevelUp;
import com.l2jserver.gameserver.model.events.impl.character.player.clanwh.PlayerClanWHItemAdd;
import com.l2jserver.gameserver.model.events.impl.character.player.clanwh.PlayerClanWHItemDestroy;
import com.l2jserver.gameserver.model.events.impl.character.player.clanwh.PlayerClanWHItemTransfer;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.PlayerItemAdd;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.PlayerItemDestroy;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.PlayerItemDrop;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.PlayerItemPickup;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.PlayerItemTransfer;
import com.l2jserver.gameserver.model.events.impl.character.trap.OnTrapAction;
import com.l2jserver.gameserver.model.events.impl.clan.ClanWarFinish;
import com.l2jserver.gameserver.model.events.impl.clan.ClanWarStart;
import com.l2jserver.gameserver.model.events.impl.events.TvTEventFinish;
import com.l2jserver.gameserver.model.events.impl.events.TvTEventKill;
import com.l2jserver.gameserver.model.events.impl.events.TvTEventRegistrationStart;
import com.l2jserver.gameserver.model.events.impl.events.TvTEventStart;
import com.l2jserver.gameserver.model.events.impl.item.ItemBypass;
import com.l2jserver.gameserver.model.events.impl.item.ItemCreate;
import com.l2jserver.gameserver.model.events.impl.item.ItemTalk;
import com.l2jserver.gameserver.model.events.impl.olympiad.OlympiadMatchResult;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeFinish;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeOwnerChange;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeStart;
import com.l2jserver.gameserver.model.events.impl.sieges.fort.FortSiegeFinish;
import com.l2jserver.gameserver.model.events.impl.sieges.fort.FortSiegeStart;
import com.l2jserver.gameserver.model.events.returns.ChatFilterReturn;
import com.l2jserver.gameserver.model.events.returns.TerminateReturn;
import com.l2jserver.gameserver.util.Util;

/**
 * Event type.
 * @author UnAfraid
 * @author Zoey76
 */
public enum EventType {
	// Attackable events
	ATTACKABLE_AGGRO_RANGE_ENTER(AttackableAggroRangeEnter.class, void.class),
	ATTACKABLE_ATTACK(AttackableAttack.class, void.class),
	ATTACKABLE_FACTION_CALL(AttackableFactionCall.class, void.class),
	ATTACKABLE_KILL(AttackableKill.class, void.class),
	
	// Castle events
	CASTLE_SIEGE_FINISH(CastleSiegeFinish.class, void.class),
	CASTLE_SIEGE_OWNER_CHANGE(CastleSiegeOwnerChange.class, void.class),
	CASTLE_SIEGE_START(CastleSiegeStart.class, void.class),
	
	// Clan events
	CLAN_WAR_FINISH(ClanWarFinish.class, void.class),
	CLAN_WAR_START(ClanWarStart.class, void.class),
	
	// Creature events
	CREATURE_ATTACK(CreatureAttack.class, void.class, TerminateReturn.class),
	CREATURE_ATTACK_AVOID(CreatureAttackAvoid.class, void.class, void.class),
	CREATURE_ATTACKED(CreatureAttacked.class, void.class, TerminateReturn.class),
	CREATURE_DAMAGE_RECEIVED(CreatureDamageReceived.class, void.class),
	CREATURE_DAMAGE_DEALT(CreatureDamageDealt.class, void.class),
	CREATURE_KILL(CreatureKill.class, void.class, TerminateReturn.class),
	CREATURE_SKILL_USE(CreatureSkillUse.class, void.class, TerminateReturn.class),
	CREATURE_TELEPORTED(CreatureTeleported.class, void.class),
	CREATURE_ZONE_ENTER(CreatureZoneEnter.class, void.class),
	CREATURE_ZONE_EXIT(CreatureZoneExit.class, void.class),
	
	// Fortress events
	FORT_SIEGE_FINISH(FortSiegeFinish.class, void.class),
	FORT_SIEGE_START(FortSiegeStart.class, void.class),
	
	// Item events
	ITEM_BYPASS(ItemBypass.class, void.class),
	ITEM_CREATE(ItemCreate.class, void.class),
	ITEM_TALK(ItemTalk.class, void.class),
	
	// NPC events
	NPC_CAN_BE_SEEN(NpcCanBeSeen.class, void.class, TerminateReturn.class),
	NPC_CREATURE_SEE(NpcCreatureSee.class, void.class),
	NPC_EVENT_RECEIVED(NpcEventReceived.class, void.class),
	NPC_FIRST_TALK(NpcFirstTalk.class, void.class),
	NPC_HATE(AttackableHate.class, void.class, TerminateReturn.class),
	NPC_MOVE_FINISHED(NpcMoveFinished.class, void.class),
	NPC_MOVE_NODE_ARRIVED(NpcMoveNodeArrived.class, void.class),
	NPC_MOVE_ROUTE_FINISHED(NpcMoveRouteFinished.class, void.class),
	NPC_QUEST_START(null, void.class),
	NPC_SKILL_FINISHED(NpcSkillFinished.class, void.class),
	NPC_SKILL_SEE(NpcSkillSee.class, void.class),
	NPC_SPAWN(NpcSpawn.class, void.class),
	NPC_TALK(null, void.class),
	NPC_TELEPORT(NpcTeleport.class, void.class),
	NPC_MANOR_BYPASS(NpcManorBypass.class, void.class),
	
	// Olympiad events
	OLYMPIAD_MATCH_RESULT(OlympiadMatchResult.class, void.class),
	
	// Playable events
	PLAYABLE_EXP_CHANGED(PlayableExpChanged.class, void.class, TerminateReturn.class),
	
	// Player events
	PLAYER_AUGMENT(PlayerAugment.class, void.class),
	PLAYER_BYPASS(PlayerBypass.class, void.class),
	PLAYER_CHAT(PlayerChat.class, void.class, ChatFilterReturn.class),
	
	// Tutorial events
	PLAYER_TUTORIAL(PlayerTutorial.class, void.class, void.class),
	PLAYER_TUTORIAL_CMD(PlayerTutorialCmd.class, void.class, void.class),
	PLAYER_TUTORIAL_CLIENT_EVENT(PlayerTutorialClientEvent.class, void.class, void.class),
	PLAYER_TUTORIAL_QUESTION_MARK(PlayerTutorialQuestionMark.class, void.class, void.class),
	
	// Clan events
	PLAYER_CLAN_CREATE(PlayerClanCreate.class, void.class),
	PLAYER_CLAN_DESTROY(PlayerClanDestroy.class, void.class),
	PLAYER_CLAN_JOIN(PlayerClanJoin.class, void.class),
	PLAYER_CLAN_LEADER_CHANGE(PlayerClanLeaderChange.class, void.class),
	PLAYER_CLAN_LEAVE(PlayerClanLeave.class, void.class),
	PLAYER_CLAN_LEVEL_UP(PlayerClanLevelUp.class, void.class),
	// Clan warehouse events
	PLAYER_CLAN_WH_ITEM_ADD(PlayerClanWHItemAdd.class, void.class),
	PLAYER_CLAN_WH_ITEM_DESTROY(PlayerClanWHItemDestroy.class, void.class),
	PLAYER_CLAN_WH_ITEM_TRANSFER(PlayerClanWHItemTransfer.class, void.class),
	PLAYER_CREATE(PlayerCreate.class, void.class),
	PLAYER_DELETE(PlayerDelete.class, void.class),
	PLAYER_DLG_ANSWER(PlayerDlgAnswer.class, void.class, TerminateReturn.class),
	PLAYER_EQUIP_ITEM(PlayerEquipItem.class, void.class),
	PLAYER_FAME_CHANGED(PlayerFameChanged.class, void.class),
	// Henna events
	PLAYER_HENNA_ADD(PlayerHennaAdd.class, void.class),
	PLAYER_HENNA_REMOVE(PlayerHennaRemove.class, void.class),
	// Inventory events
	PLAYER_ITEM_ADD(PlayerItemAdd.class, void.class),
	PLAYER_ITEM_DESTROY(PlayerItemDestroy.class, void.class),
	PLAYER_ITEM_DROP(PlayerItemDrop.class, void.class),
	PLAYER_ITEM_PICKUP(PlayerItemPickup.class, void.class),
	PLAYER_ITEM_TRANSFER(PlayerItemTransfer.class, void.class),
	// Other players events
	PLAYER_KARMA_CHANGED(PlayerKarmaChanged.class, void.class),
	PLAYER_LEVEL_CHANGED(PlayerLevelChanged.class, void.class),
	PLAYER_LOGIN(PlayerLogin.class, void.class),
	PLAYER_LOGOUT(PlayerLogout.class, void.class),
	PLAYER_PK_CHANGED(PlayerPKChanged.class, void.class),
	PLAYER_PROFESSION_CHANGE(PlayerProfessionChange.class, void.class),
	PLAYER_PROFESSION_CANCEL(PlayerProfessionCancel.class, void.class),
	PLAYER_PVP_CHANGED(PlayerPvPChanged.class, void.class),
	PLAYER_PVP_KILL(PlayerPvPKill.class, void.class),
	PLAYER_RESTORE(PlayerRestore.class, void.class),
	PLAYER_SELECT(PlayerSelect.class, void.class, TerminateReturn.class),
	PLAYER_SIT(PlayerSit.class, TerminateReturn.class),
	PLAYER_LEARN_SKILL_REQUESTED(PlayerLearnSkillRequested.class, void.class),
	PLAYER_ONE_SKILL_SELECTED(PlayerOneSkillSelected.class, void.class),
	PLAYER_SKILL_LEARNED(PlayerSkillLearned.class, void.class),
	PLAYER_MENU_SELECTED(PlayerMenuSelected.class, void.class),
	PLAYER_QUEST_ACCEPTED(PlayerQuestAccepted.class, void.class),
	PLAYER_STAND(PlayerSit.class, TerminateReturn.class),
	PLAYER_SUMMON_SPAWN(PlayerSummonSpawn.class, void.class),
	PLAYER_SUMMON_TALK(PlayerSummonTalk.class, void.class),
	PLAYER_TRANSFORM(PlayerTransform.class, void.class),
	
	// Trap events
	TRAP_ACTION(OnTrapAction.class, void.class),
	
	// TvT events.
	TVT_EVENT_FINISH(TvTEventFinish.class, void.class),
	TVT_EVENT_KILL(TvTEventKill.class, void.class),
	TVT_EVENT_REGISTRATION_START(TvTEventRegistrationStart.class, void.class),
	TVT_EVENT_START(TvTEventStart.class, void.class);
	
	private final Class<? extends BaseEvent> eventClass;
	private final Class<?>[] returnClass;
	
	EventType(Class<? extends BaseEvent> eventClass, Class<?>... returnClass) {
		this.eventClass = eventClass;
		this.returnClass = returnClass;
	}
	
	public Class<? extends BaseEvent> getEventClass() {
		return eventClass;
	}
	
	public Class<?>[] getReturnClasses() {
		return returnClass;
	}
	
	public boolean isEventClass(Class<?> clazz) {
		return eventClass == clazz;
	}
	
	public boolean isReturnClass(Class<?> clazz) {
		return Util.contains(returnClass, clazz);
	}
}
