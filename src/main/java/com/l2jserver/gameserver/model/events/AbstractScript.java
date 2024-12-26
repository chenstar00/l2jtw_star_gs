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

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.config.Configuration.rates;
import static com.l2jserver.gameserver.model.events.EventType.ATTACKABLE_AGGRO_RANGE_ENTER;
import static com.l2jserver.gameserver.model.events.EventType.ATTACKABLE_ATTACK;
import static com.l2jserver.gameserver.model.events.EventType.ATTACKABLE_FACTION_CALL;
import static com.l2jserver.gameserver.model.events.EventType.ATTACKABLE_KILL;
import static com.l2jserver.gameserver.model.events.EventType.CASTLE_SIEGE_FINISH;
import static com.l2jserver.gameserver.model.events.EventType.CASTLE_SIEGE_OWNER_CHANGE;
import static com.l2jserver.gameserver.model.events.EventType.CASTLE_SIEGE_START;
import static com.l2jserver.gameserver.model.events.EventType.CREATURE_KILL;
import static com.l2jserver.gameserver.model.events.EventType.CREATURE_ZONE_ENTER;
import static com.l2jserver.gameserver.model.events.EventType.CREATURE_ZONE_EXIT;
import static com.l2jserver.gameserver.model.events.EventType.ITEM_BYPASS;
import static com.l2jserver.gameserver.model.events.EventType.ITEM_TALK;
import static com.l2jserver.gameserver.model.events.EventType.NPC_CAN_BE_SEEN;
import static com.l2jserver.gameserver.model.events.EventType.NPC_CREATURE_SEE;
import static com.l2jserver.gameserver.model.events.EventType.NPC_EVENT_RECEIVED;
import static com.l2jserver.gameserver.model.events.EventType.NPC_FIRST_TALK;
import static com.l2jserver.gameserver.model.events.EventType.NPC_HATE;
import static com.l2jserver.gameserver.model.events.EventType.NPC_MOVE_FINISHED;
import static com.l2jserver.gameserver.model.events.EventType.NPC_MOVE_NODE_ARRIVED;
import static com.l2jserver.gameserver.model.events.EventType.NPC_MOVE_ROUTE_FINISHED;
import static com.l2jserver.gameserver.model.events.EventType.NPC_QUEST_START;
import static com.l2jserver.gameserver.model.events.EventType.NPC_SKILL_FINISHED;
import static com.l2jserver.gameserver.model.events.EventType.NPC_SKILL_SEE;
import static com.l2jserver.gameserver.model.events.EventType.NPC_SPAWN;
import static com.l2jserver.gameserver.model.events.EventType.NPC_TALK;
import static com.l2jserver.gameserver.model.events.EventType.NPC_TELEPORT;
import static com.l2jserver.gameserver.model.events.EventType.OLYMPIAD_MATCH_RESULT;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_LOGIN;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_LOGOUT;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_PROFESSION_CANCEL;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_PROFESSION_CHANGE;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_SUMMON_SPAWN;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_SUMMON_TALK;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_TUTORIAL;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_TUTORIAL_CLIENT_EVENT;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_TUTORIAL_CMD;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_TUTORIAL_QUESTION_MARK;
import static com.l2jserver.gameserver.model.events.EventType.TRAP_ACTION;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.CASTLE;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.GLOBAL;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.ITEM;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.NPC;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.OLYMPIAD;
import static com.l2jserver.gameserver.model.events.ListenerRegisterType.ZONE;
import static com.l2jserver.gameserver.model.quest.QuestDroplist.singleDropItem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.commons.util.Util;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.enums.audio.IAudio;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2TrapInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.drops.GeneralDropItem;
import com.l2jserver.gameserver.model.drops.GroupedGeneralDropItem;
import com.l2jserver.gameserver.model.drops.IDropItem;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.model.entity.Instance;
import com.l2jserver.gameserver.model.events.annotations.Id;
import com.l2jserver.gameserver.model.events.annotations.Ids;
import com.l2jserver.gameserver.model.events.annotations.NpcLevelRange;
import com.l2jserver.gameserver.model.events.annotations.NpcLevelRanges;
import com.l2jserver.gameserver.model.events.annotations.Priority;
import com.l2jserver.gameserver.model.events.annotations.Range;
import com.l2jserver.gameserver.model.events.annotations.Ranges;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.BaseEvent;
import com.l2jserver.gameserver.model.events.impl.character.CreatureKill;
import com.l2jserver.gameserver.model.events.impl.character.CreatureZoneEnter;
import com.l2jserver.gameserver.model.events.impl.character.CreatureZoneExit;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcCanBeSeen;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcCreatureSee;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcEventReceived;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcFirstTalk;
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
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLogin;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLogout;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerProfessionCancel;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerProfessionChange;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSummonSpawn;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerSummonTalk;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorial;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialClientEvent;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialCmd;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerTutorialQuestionMark;
import com.l2jserver.gameserver.model.events.impl.character.trap.OnTrapAction;
import com.l2jserver.gameserver.model.events.impl.item.ItemBypass;
import com.l2jserver.gameserver.model.events.impl.item.ItemTalk;
import com.l2jserver.gameserver.model.events.impl.olympiad.OlympiadMatchResult;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeFinish;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeOwnerChange;
import com.l2jserver.gameserver.model.events.impl.sieges.castle.CastleSiegeStart;
import com.l2jserver.gameserver.model.events.listeners.AbstractEventListener;
import com.l2jserver.gameserver.model.events.listeners.AnnotationEventListener;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.events.listeners.DummyEventListener;
import com.l2jserver.gameserver.model.events.listeners.FunctionEventListener;
import com.l2jserver.gameserver.model.events.listeners.RunnableEventListener;
import com.l2jserver.gameserver.model.events.returns.AbstractEventReturn;
import com.l2jserver.gameserver.model.events.returns.TerminateReturn;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.interfaces.INamable;
import com.l2jserver.gameserver.model.interfaces.IPositionable;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.L2EtcItem;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.olympiad.Olympiad;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.SpecialCamera;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.scripting.ScriptManager;
import com.l2jserver.gameserver.util.MinionList;

/**
 * Abstract script.
 * @author KenM
 * @author UnAfraid
 * @author Zoey76
 */
public abstract class AbstractScript implements INamable {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractScript.class);
	
	private final Map<ListenerRegisterType, Set<Integer>> _registeredIds = new ConcurrentHashMap<>();
	
	private final List<AbstractEventListener> _listeners = new CopyOnWriteArrayList<>();
	
	private boolean _isActive;
	
	public AbstractScript() {
		initializeAnnotationListeners();
	}
	
	private void initializeAnnotationListeners() {
		final List<Integer> ids = new ArrayList<>();
		for (Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(RegisterEvent.class) && method.isAnnotationPresent(RegisterType.class)) {
				final RegisterEvent listener = method.getAnnotation(RegisterEvent.class);
				final RegisterType regType = method.getAnnotation(RegisterType.class);
				
				final ListenerRegisterType type = regType.value();
				final EventType eventType = listener.value();
				if (method.getParameterCount() != 1) {
					LOG.warn("Non properly defined annotation listener on method {} expected parameter count is 1 but found {}!", method.getName(), method.getParameterCount());
					continue;
				}
				
				if (!eventType.isEventClass(method.getParameterTypes()[0])) {
					LOG.warn("Non properly defined annotation listener on method {} expected parameter to be type of {} but found {}!", method.getName(), eventType.getEventClass().getSimpleName(), method.getParameterTypes()[0].getSimpleName());
					continue;
				}
				
				if (!eventType.isReturnClass(method.getReturnType())) {
					LOG.warn("Non properly defined annotation listener on method {} expected return type to be one of {} but found {}", method.getName(), Arrays.toString(eventType.getReturnClasses()), method.getReturnType().getSimpleName());
					continue;
				}
				
				int priority = 0;
				
				// Clear the list
				ids.clear();
				
				// Scan for possible ID filters
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation instanceof Id npc) {
						for (int id : npc.value()) {
							ids.add(id);
						}
					} else if (annotation instanceof Ids npcs) {
						for (Id npc : npcs.value()) {
							for (int id : npc.value()) {
								ids.add(id);
							}
						}
					} else if (annotation instanceof Range range) {
						if (range.from() > range.to()) {
							LOG.warn("Wrong {} from is higher then to!", annotation.getClass().getSimpleName());
							continue;
						}
						
						for (int id = range.from(); id <= range.to(); id++) {
							ids.add(id);
						}
					} else if (annotation instanceof Ranges ranges) {
						for (Range range : ranges.value()) {
							if (range.from() > range.to()) {
								LOG.warn("Wrong {} from is higher then to!", annotation.getClass().getSimpleName());
								continue;
							}
							
							for (int id = range.from(); id <= range.to(); id++) {
								ids.add(id);
							}
						}
					} else if (annotation instanceof NpcLevelRange range) {
						if (range.from() > range.to()) {
							LOG.warn("Wrong {} from is higher then to!", annotation.getClass().getSimpleName());
							continue;
						} else if (type != NPC) {
							LOG.warn("ListenerRegisterType {} for {} NPC is expected!", type, annotation.getClass().getSimpleName());
							continue;
						}
						
						for (int level = range.from(); level <= range.to(); level++) {
							final List<L2NpcTemplate> templates = NpcData.getInstance().getAllOfLevel(level);
							templates.forEach(template -> ids.add(template.getId()));
						}
					} else if (annotation instanceof NpcLevelRanges ranges) {
						for (NpcLevelRange range : ranges.value()) {
							if (range.from() > range.to()) {
								LOG.warn("Wrong {} from is higher then to!", annotation.getClass().getSimpleName());
								continue;
							} else if (type != NPC) {
								LOG.warn("ListenerRegisterType {} for {} NPC is expected!", type, annotation.getClass().getSimpleName());
								continue;
							}
							
							for (int level = range.from(); level <= range.to(); level++) {
								final List<L2NpcTemplate> templates = NpcData.getInstance().getAllOfLevel(level);
								templates.forEach(template -> ids.add(template.getId()));
							}
						}
					} else if (annotation instanceof Priority p) {
						priority = p.value();
					}
				}
				
				if (!ids.isEmpty()) {
					_registeredIds.putIfAbsent(type, ConcurrentHashMap.newKeySet(ids.size()));
					
					_registeredIds.get(type).addAll(ids);
				}
				
				registerAnnotation(method, eventType, type, priority, ids);
			}
		}
	}
	
	public void setActive(boolean status) {
		_isActive = status;
	}
	
	public boolean isActive() {
		return _isActive;
	}
	
	public abstract boolean reload();
	
	/**
	 * Unloads all listeners registered by this class.
	 * @return {@code true}
	 */
	public boolean unload() {
		_listeners.forEach(AbstractEventListener::unregisterMe);
		_listeners.clear();
		return true;
	}
	
	public abstract ScriptManager<?> getManager();
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides delayed (Depending on {@link com.l2jserver.gameserver.model.actor.L2Attackable#getOnKillDelay()}) callback operation when L2Attackable dies from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableKillId(Consumer<AttackableKill> callback, int... npcIds) {
		return registerConsumer(callback, ATTACKABLE_KILL, NPC, npcIds);
	}
	
	/**
	 * Provides delayed (Depending on {@link com.l2jserver.gameserver.model.actor.L2Attackable#getOnKillDelay()}) callback operation when L2Attackable dies from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableKillId(Consumer<AttackableKill> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ATTACKABLE_KILL, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Attackable dies from a player with return type.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> addCreatureKillId(Function<CreatureKill, ? extends AbstractEventReturn> callback, int... npcIds) {
		return registerFunction(callback, CREATURE_KILL, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Attackable dies from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureKillId(Consumer<CreatureKill> callback, int... npcIds) {
		return registerConsumer(callback, CREATURE_KILL, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Attackable} dies from a {@link L2PcInstance}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureKillId(Consumer<CreatureKill> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, CREATURE_KILL, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc} for first time.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcFirstTalkId(Consumer<NpcFirstTalk> callback, int... npcIds) {
		return registerConsumer(callback, NPC_FIRST_TALK, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc} for first time.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcFirstTalkId(Consumer<NpcFirstTalk> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_FIRST_TALK, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	protected final List<AbstractEventListener> setPlayerTutorialEvent(Consumer<PlayerTutorial> callback) {
		return registerConsumer(callback, PLAYER_TUTORIAL, GLOBAL);
	}
	
	protected final List<AbstractEventListener> setPlayerTutorialClientEvent(Consumer<PlayerTutorialClientEvent> callback) {
		return registerConsumer(callback, PLAYER_TUTORIAL_CLIENT_EVENT, GLOBAL);
	}
	
	protected final List<AbstractEventListener> setPlayerTutorialQuestionMark(Consumer<PlayerTutorialQuestionMark> callback) {
		return registerConsumer(callback, PLAYER_TUTORIAL_QUESTION_MARK, GLOBAL);
	}
	
	protected final List<AbstractEventListener> setPlayerTutorialCmd(Consumer<PlayerTutorialCmd> callback) {
		return registerConsumer(callback, PLAYER_TUTORIAL_CMD, GLOBAL);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc}.
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcTalkId(Collection<Integer> npcIds) {
		return registerDummy(NPC_TALK, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc}.
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcTalkId(int... npcIds) {
		return registerDummy(NPC_TALK, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when teleport {@link L2Npc}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcTeleportId(Consumer<NpcTeleport> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_TELEPORT, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when teleport {@link L2Npc}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcTeleportId(Consumer<NpcTeleport> callback, int... npcIds) {
		return registerConsumer(callback, NPC_TELEPORT, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc} and must receive quest state.
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcQuestStartId(int... npcIds) {
		return registerDummy(NPC_QUEST_START, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Npc} and must receive quest state.
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcQuestStartId(Collection<Integer> npcIds) {
		return registerDummy(NPC_QUEST_START, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Npc sees skill from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSkillSeeId(Consumer<NpcSkillSee> callback, int... npcIds) {
		return registerConsumer(callback, NPC_SKILL_SEE, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Npc sees skill from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSkillSeeId(Consumer<NpcSkillSee> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_SKILL_SEE, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Npc casts skill on a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSkillFinishedId(Consumer<NpcSkillFinished> callback, int... npcIds) {
		return registerConsumer(callback, NPC_SKILL_FINISHED, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Npc casts skill on a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSkillFinishedId(Consumer<NpcSkillFinished> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_SKILL_FINISHED, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Npc is spawned.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSpawnId(Consumer<NpcSpawn> callback, int... npcIds) {
		return registerConsumer(callback, NPC_SPAWN, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Npc is spawned.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcSpawnId(Consumer<NpcSpawn> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_SPAWN, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} receives event from another {@link L2Npc}
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcEventReceivedId(Consumer<NpcEventReceived> callback, int... npcIds) {
		return registerConsumer(callback, NPC_EVENT_RECEIVED, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} receives event from another {@link L2Npc}
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcEventReceivedId(Consumer<NpcEventReceived> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_EVENT_RECEIVED, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} finishes to move.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveFinishedId(Consumer<NpcMoveFinished> callback, int... npcIds) {
		return registerConsumer(callback, NPC_MOVE_FINISHED, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} finishes to move.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveFinishedId(Consumer<NpcMoveFinished> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_MOVE_FINISHED, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} arrive to node of its route
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveNodeArrivedId(Consumer<NpcMoveNodeArrived> callback, int... npcIds) {
		return registerConsumer(callback, NPC_MOVE_NODE_ARRIVED, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} arrive to node of its route
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveNodeArrivedId(Consumer<NpcMoveNodeArrived> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_MOVE_NODE_ARRIVED, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} finishes to move on its route.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveRouteFinishedId(Consumer<NpcMoveRouteFinished> callback, int... npcIds) {
		return registerConsumer(callback, NPC_MOVE_ROUTE_FINISHED, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} finishes to move on its route.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcMoveRouteFinishedId(Consumer<NpcMoveRouteFinished> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_MOVE_ROUTE_FINISHED, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcHateId(Consumer<AttackableHate> callback, int... npcIds) {
		return registerConsumer(callback, NPC_HATE, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcHateId(Consumer<AttackableHate> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_HATE, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> addNpcHateId(Function<AttackableHate, TerminateReturn> callback, int... npcIds) {
		return registerFunction(callback, NPC_HATE, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> addNpcHateId(Function<AttackableHate, TerminateReturn> callback, Collection<Integer> npcIds) {
		return registerFunction(callback, NPC_HATE, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCanBeSeenId(Consumer<NpcCanBeSeen> callback, int... npcIds) {
		return registerConsumer(callback, NPC_CAN_BE_SEEN, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCanBeSeenId(Consumer<NpcCanBeSeen> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_CAN_BE_SEEN, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCanBeSeenId(Function<NpcCanBeSeen, TerminateReturn> callback, int... npcIds) {
		return registerFunction(callback, NPC_CAN_BE_SEEN, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} is about to hate and start attacking a creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCanBeSeenId(Function<NpcCanBeSeen, TerminateReturn> callback, Collection<Integer> npcIds) {
		return registerFunction(callback, NPC_CAN_BE_SEEN, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Npc} sees another creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCreatureSeeId(Consumer<NpcCreatureSee> callback, int... npcIds) {
		return registerConsumer(callback, NPC_CREATURE_SEE, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Npc} sees another creature.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setNpcCreatureSeeId(Consumer<NpcCreatureSee> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, NPC_CREATURE_SEE, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Attackable is under attack to other clan mates.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableFactionIdId(Consumer<AttackableFactionCall> callback, int... npcIds) {
		return registerConsumer(callback, ATTACKABLE_FACTION_CALL, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Attackable is under attack to other clan mates.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableFactionIdId(Consumer<AttackableFactionCall> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ATTACKABLE_FACTION_CALL, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when L2Attackable is attacked from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableAttackId(Consumer<AttackableAttack> callback, int... npcIds) {
		return registerConsumer(callback, ATTACKABLE_ATTACK, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when L2Attackable is attacked from a player.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableAttackId(Consumer<AttackableAttack> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ATTACKABLE_ATTACK, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} enters in {@link L2Attackable}'s aggressive range.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableAggroRangeEnterId(Consumer<AttackableAggroRangeEnter> callback, int... npcIds) {
		return registerConsumer(callback, ATTACKABLE_AGGRO_RANGE_ENTER, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} enters in {@link L2Attackable}'s aggressive range.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setAttackableAggroRangeEnterId(Consumer<AttackableAggroRangeEnter> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ATTACKABLE_AGGRO_RANGE_ENTER, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} summons a servitor or a pet
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerSummonSpawnId(Consumer<PlayerSummonSpawn> callback, int... npcIds) {
		return registerConsumer(callback, PLAYER_SUMMON_SPAWN, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} summons a servitor or a pet
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerSummonSpawnId(Consumer<PlayerSummonSpawn> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, PLAYER_SUMMON_SPAWN, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk with a servitor or a pet
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerSummonTalkId(Consumer<PlayerSummonTalk> callback, int... npcIds) {
		return registerConsumer(callback, PLAYER_SUMMON_TALK, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk with a servitor or a pet
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerSummonTalkId(Consumer<PlayerSummonSpawn> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, PLAYER_SUMMON_TALK, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} summons a servitor or a pet
	 * @param callback
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerLoginId(Consumer<PlayerLogin> callback) {
		return registerConsumer(callback, PLAYER_LOGIN, GLOBAL);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} summons a servitor or a pet
	 * @param callback
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerLogoutId(Consumer<PlayerLogout> callback) {
		return registerConsumer(callback, PLAYER_LOGOUT, GLOBAL);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.L2Character} Enters on a {@link L2ZoneType}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureZoneEnterId(Consumer<CreatureZoneEnter> callback, int... npcIds) {
		return registerConsumer(callback, CREATURE_ZONE_ENTER, ZONE, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.L2Character} Enters on a {@link L2ZoneType}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureZoneEnterId(Consumer<CreatureZoneEnter> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, CREATURE_ZONE_ENTER, ZONE, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.L2Character} Exits on a {@link L2ZoneType}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureZoneExitId(Consumer<CreatureZoneExit> callback, int... npcIds) {
		return registerConsumer(callback, CREATURE_ZONE_EXIT, ZONE, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.L2Character} Exits on a {@link L2ZoneType}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCreatureZoneExitId(Consumer<CreatureZoneExit> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, CREATURE_ZONE_EXIT, ZONE, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.instance.L2TrapInstance} acts.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setTrapActionId(Consumer<OnTrapAction> callback, int... npcIds) {
		return registerConsumer(callback, TRAP_ACTION, NPC, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link com.l2jserver.gameserver.model.actor.instance.L2TrapInstance} acts.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setTrapActionId(Consumer<OnTrapAction> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, TRAP_ACTION, NPC, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2Item} receives an event from {@link L2PcInstance}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setItemBypass(Consumer<ItemBypass> callback, int... npcIds) {
		return registerConsumer(callback, ITEM_BYPASS, ITEM, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2Item} receives an event from {@link L2PcInstance}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setItemBypassEvenId(Consumer<ItemBypass> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ITEM_BYPASS, ITEM, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Item}.
	 * @param callback
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> setItemTalkId(Consumer<ItemTalk> callback, int... npcIds) {
		return registerConsumer(callback, ITEM_TALK, ITEM, npcIds);
	}
	
	/**
	 * Provides instant callback operation when {@link L2PcInstance} talk to {@link L2Item}.
	 * @param callback the event callback
	 * @param npcIds the NPC Ids
	 * @return
	 */
	protected final List<AbstractEventListener> setItemTalkId(Consumer<ItemTalk> callback, Collection<Integer> npcIds) {
		return registerConsumer(callback, ITEM_TALK, ITEM, npcIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when Olympiad match finishes.
	 * @param callback the event callback
	 * @return
	 */
	protected final List<AbstractEventListener> setOlympiadMatchResult(Consumer<OlympiadMatchResult> callback) {
		return registerConsumer(callback, OLYMPIAD_MATCH_RESULT, OLYMPIAD);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when castle siege begins
	 * @param callback the event callback
	 * @param castleIds the castle Ids
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeStartId(Consumer<CastleSiegeStart> callback, int... castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_START, CASTLE, castleIds);
	}
	
	/**
	 * Provides instant callback operation when castle siege begins
	 * @param callback the event callback
	 * @param castleIds the castle Ids
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeStartId(Consumer<CastleSiegeStart> callback, Collection<Integer> castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_START, CASTLE, castleIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when Castle owner has changed during a siege
	 * @param callback
	 * @param castleIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeOwnerChangeId(Consumer<CastleSiegeOwnerChange> callback, int... castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_OWNER_CHANGE, CASTLE, castleIds);
	}
	
	/**
	 * Provides instant callback operation when Castle owner has changed during a siege
	 * @param callback
	 * @param castleIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeOwnerChangeId(Consumer<CastleSiegeOwnerChange> callback, Collection<Integer> castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_OWNER_CHANGE, CASTLE, castleIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when castle siege ends
	 * @param callback
	 * @param castleIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeFinishId(Consumer<CastleSiegeFinish> callback, int... castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_FINISH, CASTLE, castleIds);
	}
	
	/**
	 * Provides instant callback operation when castle siege ends
	 * @param callback
	 * @param castleIds
	 * @return
	 */
	protected final List<AbstractEventListener> setCastleSiegeFinishId(Consumer<CastleSiegeFinish> callback, Collection<Integer> castleIds) {
		return registerConsumer(callback, CASTLE_SIEGE_FINISH, CASTLE, castleIds);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides instant callback operation when player's profession has changed.
	 * @param callback
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerProfessionChangeId(Consumer<PlayerProfessionChange> callback) {
		return registerConsumer(callback, PLAYER_PROFESSION_CHANGE, GLOBAL);
	}
	
	/**
	 * Provides instant callback operation when player's cancel profession
	 * @param callback
	 * @return
	 */
	protected final List<AbstractEventListener> setPlayerProfessionCancelId(Consumer<PlayerProfessionCancel> callback) {
		return registerConsumer(callback, PLAYER_PROFESSION_CANCEL, GLOBAL);
	}
	
	// --------------------------------------------------------------------------------------------------
	// --------------------------------Default listener register methods---------------------------------
	// --------------------------------------------------------------------------------------------------
	
	/**
	 * Method that registers Function type of listeners (Listeners that need parameters but doesn't return objects)
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerConsumer(Consumer<? extends BaseEvent> callback, EventType type, ListenerRegisterType registerType, int... npcIds) {
		return registerListener(container -> new ConsumerEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers Function type of listeners (Listeners that need parameters but doesn't return objects)
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerConsumer(Consumer<? extends BaseEvent> callback, EventType type, ListenerRegisterType registerType, Collection<Integer> npcIds) {
		return registerListener(container -> new ConsumerEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers Function type of listeners (Listeners that need parameters and return objects)
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerFunction(Function<? extends BaseEvent, ? extends AbstractEventReturn> callback, EventType type, ListenerRegisterType registerType, int... npcIds) {
		return registerListener(container -> new FunctionEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers Function type of listeners (Listeners that need parameters and return objects)
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerFunction(Function<? extends BaseEvent, ? extends AbstractEventReturn> callback, EventType type, ListenerRegisterType registerType, Collection<Integer> npcIds) {
		return registerListener(container -> new FunctionEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers runnable type of listeners (Listeners that doesn't need parameters or return objects).
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerRunnable(Runnable callback, EventType type, ListenerRegisterType registerType, int... npcIds) {
		return registerListener(container -> new RunnableEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers runnable type of listeners (Listeners that doesn't need parameters or return objects).
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerRunnable(Runnable callback, EventType type, ListenerRegisterType registerType, Collection<Integer> npcIds) {
		return registerListener(container -> new RunnableEventListener(container, type, callback, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers runnable type of listeners (Listeners that doesn't need parameters or return objects).
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param priority
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerAnnotation(Method callback, EventType type, ListenerRegisterType registerType, int priority, int... npcIds) {
		return registerListener(container -> new AnnotationEventListener(container, type, callback, this, priority), registerType, npcIds);
	}
	
	/**
	 * Method that registers runnable type of listeners (Listeners that doesn't need parameters or return objects).
	 * @param callback
	 * @param type
	 * @param registerType
	 * @param priority
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerAnnotation(Method callback, EventType type, ListenerRegisterType registerType, int priority, Collection<Integer> npcIds) {
		return registerListener(container -> new AnnotationEventListener(container, type, callback, this, priority), registerType, npcIds);
	}
	
	/**
	 * Method that registers dummy type of listeners (Listeners doesn't get notification but just used to check if their type present or not).
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerDummy(EventType type, ListenerRegisterType registerType, int... npcIds) {
		return registerListener(container -> new DummyEventListener(container, type, this), registerType, npcIds);
	}
	
	/**
	 * Method that registers dummy type of listeners (Listeners doesn't get notification but just used to check if their type present or not).
	 * @param type
	 * @param registerType
	 * @param npcIds
	 * @return
	 */
	protected final List<AbstractEventListener> registerDummy(EventType type, ListenerRegisterType registerType, Collection<Integer> npcIds) {
		return registerListener(container -> new DummyEventListener(container, type, this), registerType, npcIds);
	}
	
	// --------------------------------------------------------------------------------------------------
	// --------------------------------------Register methods--------------------------------------------
	// --------------------------------------------------------------------------------------------------
	
	/**
	 * Generic listener register method
	 * @param action
	 * @param registerType
	 * @param ids
	 * @return
	 */
	protected final List<AbstractEventListener> registerListener(Function<ListenersContainer, AbstractEventListener> action, ListenerRegisterType registerType, int... ids) {
		final List<AbstractEventListener> listeners = new ArrayList<>(ids.length > 0 ? ids.length : 1);
		if (ids.length > 0) {
			for (int id : ids) {
				switch (registerType) {
					case NPC -> {
						final L2NpcTemplate template = NpcData.getInstance().getTemplate(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case ZONE -> {
						final L2ZoneType template = ZoneManager.getInstance().getZoneById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case ITEM -> {
						final L2Item template = ItemTable.getInstance().getTemplate(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case CASTLE -> {
						final Castle template = CastleManager.getInstance().getCastleById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case FORTRESS -> {
						final Fort template = FortManager.getInstance().getFortById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					default -> LOG.warn("Unhandled register type {}", registerType);
				}
				
				_registeredIds.putIfAbsent(registerType, ConcurrentHashMap.newKeySet(1));
				_registeredIds.get(registerType).add(id);
			}
		} else {
			switch (registerType) {
				case OLYMPIAD -> {
					final Olympiad template = Olympiad.getInstance();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL -> {
					final ListenersContainer template = Containers.Global();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_NPCS -> {
					final ListenersContainer template = Containers.Npcs();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_MONSTERS -> {
					final ListenersContainer template = Containers.Monsters();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_PLAYERS -> {
					final ListenersContainer template = Containers.Players();
					listeners.add(template.addListener(action.apply(template)));
				}
			}
		}
		
		_listeners.addAll(listeners);
		return listeners;
	}
	
	/**
	 * Generic listener register method
	 * @param action
	 * @param registerType
	 * @param ids
	 * @return
	 */
	protected final List<AbstractEventListener> registerListener(Function<ListenersContainer, AbstractEventListener> action, ListenerRegisterType registerType, Collection<Integer> ids) {
		final List<AbstractEventListener> listeners = new ArrayList<>(!ids.isEmpty() ? ids.size() : 1);
		if (!ids.isEmpty()) {
			for (int id : ids) {
				switch (registerType) {
					case NPC -> {
						final L2NpcTemplate template = NpcData.getInstance().getTemplate(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case ZONE -> {
						final L2ZoneType template = ZoneManager.getInstance().getZoneById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case ITEM -> {
						final L2Item template = ItemTable.getInstance().getTemplate(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case CASTLE -> {
						final Castle template = CastleManager.getInstance().getCastleById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					case FORTRESS -> {
						final Fort template = FortManager.getInstance().getFortById(id);
						if (template != null) {
							listeners.add(template.addListener(action.apply(template)));
						}
					}
					default -> LOG.warn("Unhandled register type {}", registerType);
				}
			}
			
			_registeredIds.putIfAbsent(registerType, ConcurrentHashMap.newKeySet(ids.size()));
			_registeredIds.get(registerType).addAll(ids);
		} else {
			switch (registerType) {
				case OLYMPIAD -> {
					final Olympiad template = Olympiad.getInstance();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL -> {
					final ListenersContainer template = Containers.Global();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_NPCS -> {
					final ListenersContainer template = Containers.Npcs();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_MONSTERS -> {
					final ListenersContainer template = Containers.Monsters();
					listeners.add(template.addListener(action.apply(template)));
				}
				case GLOBAL_PLAYERS -> {
					final ListenersContainer template = Containers.Players();
					listeners.add(template.addListener(action.apply(template)));
				}
			}
		}
		_listeners.addAll(listeners);
		return listeners;
	}
	
	public Set<Integer> getRegisteredIds(ListenerRegisterType type) {
		return _registeredIds.getOrDefault(type, Collections.emptySet());
	}
	
	public List<AbstractEventListener> getListeners() {
		return _listeners;
	}
	
	/**
	 * Show an on-screen message to the player.
	 * @param player the player to display the message to
	 * @param text the message to display
	 * @param time the duration of the message in milliseconds
	 */
	public static void showOnScreenMsg(L2PcInstance player, String text, int time) {
		player.sendPacket(new ExShowScreenMessage(text, time));
	}
	
	/**
	 * Show an on-screen message to the player.
	 * @param player the player to display the message to
	 * @param npcString the NPC string to display
	 * @param position the position of the message on the screen
	 * @param time the duration of the message in milliseconds
	 * @param params values of parameters to replace in the NPC String (like S1, C1 etc.)
	 */
	public static void showOnScreenMsg(L2PcInstance player, NpcStringId npcString, int position, int time, String... params) {
		player.sendPacket(new ExShowScreenMessage(npcString, position, time, params));
	}
	
	/**
	 * Show an on-screen message to the player.
	 * @param player the player to display the message to
	 * @param systemMsg the system message to display
	 * @param position the position of the message on the screen
	 * @param time the duration of the message in milliseconds
	 * @param params values of parameters to replace in the system message (like S1, C1 etc.)
	 */
	public static void showOnScreenMsg(L2PcInstance player, SystemMessageId systemMsg, int position, int time, String... params) {
		player.sendPacket(new ExShowScreenMessage(systemMsg, position, time, params));
	}
	
	/**
	 * Show an on-screen message to the player.
	 * @param player the player to display the message to
	 * @param msgPosType the position of the message on the screen
	 * @param unk1 unknown value
	 * @param fontSize font size (normal, small)
	 * @param unk2 unknown value
	 * @param unk3 unknown value
	 * @param showEffect if {@true} then it will show an effect
	 * @param time the duration of the message in milliseconds
	 * @param fade if {@true} then it will fade
	 * @param npcStringId the NPC string to display
	 * @param params values of parameters to replace in the NPC String
	 */
	public static void showOnScreenMsgFStr(L2PcInstance player, int msgPosType, int unk1, int fontSize, int unk2, int unk3, boolean showEffect, int time, boolean fade, NpcStringId npcStringId, String... params) {
		player.sendPacket(new ExShowScreenMessage(2, -1, msgPosType, unk1, fontSize, unk2, unk3, showEffect, time, fade, null, npcStringId, params));
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, IPositionable pos) {
		return addSpawn(npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), false, 0, false, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param summoner the NPC that requires this spawn
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @return the {@link L2Npc} object of the newly spawned NPC, {@code null} if the NPC doesn't exist
	 */
	public static L2Npc addSpawn(L2Npc summoner, int npcId, IPositionable pos, boolean randomOffset, long despawnDelay) {
		return addSpawn(summoner, npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), randomOffset, despawnDelay, false, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, IPositionable pos, boolean isSummonSpawn) {
		return addSpawn(npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), false, 0, isSummonSpawn, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, IPositionable pos, boolean randomOffset, long despawnDelay) {
		return addSpawn(npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), randomOffset, despawnDelay, false, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, IPositionable pos, boolean randomOffset, long despawnDelay, boolean isSummonSpawn) {
		return addSpawn(npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), randomOffset, despawnDelay, isSummonSpawn, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param pos the object containing the spawn location coordinates
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @param instanceId the ID of the instance to spawn the NPC in (0 - the open world)
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable)
	 * @see #addSpawn(int, IPositionable, boolean)
	 * @see #addSpawn(int, IPositionable, boolean, long)
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, IPositionable pos, boolean randomOffset, long despawnDelay, boolean isSummonSpawn, int instanceId) {
		return addSpawn(npcId, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading(), randomOffset, despawnDelay, isSummonSpawn, instanceId);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param x the X coordinate of the spawn location
	 * @param y the Y coordinate of the spawn location
	 * @param z the Z coordinate (height) of the spawn location
	 * @param heading the heading of the NPC
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffset, long despawnDelay) {
		return addSpawn(npcId, x, y, z, heading, randomOffset, despawnDelay, false, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param x the X coordinate of the spawn location
	 * @param y the Y coordinate of the spawn location
	 * @param z the Z coordinate (height) of the spawn location
	 * @param heading the heading of the NPC
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean, int)
	 */
	public static L2Npc addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffset, long despawnDelay, boolean isSummonSpawn) {
		return addSpawn(npcId, x, y, z, heading, randomOffset, despawnDelay, isSummonSpawn, 0);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param npcId the ID of the NPC to spawn
	 * @param x the X coordinate of the spawn location
	 * @param y the Y coordinate of the spawn location
	 * @param z the Z coordinate (height) of the spawn location
	 * @param heading the heading of the NPC
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @param instanceId the ID of the instance to spawn the NPC in (0 - the open world)
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean)
	 */
	public static L2Npc addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffset, long despawnDelay, boolean isSummonSpawn, int instanceId) {
		return addSpawn(null, npcId, x, y, z, heading, randomOffset, despawnDelay, isSummonSpawn, instanceId);
	}
	
	/**
	 * Add a temporary spawn of the specified NPC.
	 * @param summoner the NPC that requires this spawn
	 * @param npcId the ID of the NPC to spawn
	 * @param x the X coordinate of the spawn location
	 * @param y the Y coordinate of the spawn location
	 * @param z the Z coordinate (height) of the spawn location
	 * @param heading the heading of the NPC
	 * @param randomOffset if {@code true}, adds +/- 50~100 to X/Y coordinates of the spawn location
	 * @param despawnDelay time in milliseconds till the NPC is despawned (0 - only despawned on server shutdown)
	 * @param isSummonSpawn if {@code true}, displays a summon animation on NPC spawn
	 * @param instanceId the ID of the instance to spawn the NPC in (0 - the open world)
	 * @return the {@link L2Npc} object of the newly spawned NPC or {@code null} if the NPC doesn't exist
	 * @see #addSpawn(int, IPositionable, boolean, long, boolean, int)
	 * @see #addSpawn(int, int, int, int, int, boolean, long)
	 * @see #addSpawn(int, int, int, int, int, boolean, long, boolean)
	 */
	public static L2Npc addSpawn(L2Npc summoner, int npcId, int x, int y, int z, int heading, boolean randomOffset, long despawnDelay, boolean isSummonSpawn, int instanceId) {
		try {
			if ((x == 0) && (y == 0)) {
				LOG.error("addSpawn(): invalid spawn coordinates for NPC #{}!", npcId);
				return null;
			}
			
			if (randomOffset) {
				int offset = Rnd.get(50, 100);
				if (Rnd.nextBoolean()) {
					offset *= -1;
				}
				x += offset;
				
				offset = Rnd.get(50, 100);
				if (Rnd.nextBoolean()) {
					offset *= -1;
				}
				y += offset;
			}
			
			final var spawn = new L2Spawn(npcId);
			spawn.setInstanceId(instanceId);
			spawn.setHeading(heading);
			spawn.setX(x);
			spawn.setY(y);
			spawn.setZ(z);
			spawn.stopRespawn();
			
			final var npc = spawn.spawnOne(isSummonSpawn);
			if (despawnDelay > 0) {
				npc.scheduleDespawn(despawnDelay);
			}
			
			if (summoner != null) {
				summoner.addSummonedNpc(npc);
			}
			return npc;
		} catch (Exception ex) {
			LOG.warn("Could not spawn NPC #{}", npcId, ex);
		}
		return null;
	}
	
	/**
	 * @param trapId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param skill
	 * @param instanceId
	 * @return
	 */
	public L2TrapInstance addTrap(int trapId, int x, int y, int z, int heading, Skill skill, int instanceId) {
		final var template = NpcData.getInstance().getTemplate(trapId);
		final var objectId = IdFactory.getInstance().getNextId();
		final var trap = new L2TrapInstance(objectId, template, instanceId, -1);
		trap.setCurrentHp(trap.getMaxHp());
		trap.setCurrentMp(trap.getMaxMp());
		trap.setIsMortal(false);
		trap.setHeading(heading);
		trap.spawnMe(x, y, z);
		return trap;
	}
	
	/**
	 * @param master
	 * @param minionId
	 * @return
	 */
	public L2Npc addMinion(L2MonsterInstance master, int minionId) {
		return MinionList.spawnMinion(master, minionId);
	}
	
	/**
	 * Get the amount of an item in player's inventory.
	 * @param player the player whose inventory to check
	 * @param itemId the ID of the item whose amount to get
	 * @return the amount of the specified item in player's inventory
	 */
	public static long getQuestItemsCount(L2PcInstance player, int itemId) {
		return player.getInventory().getInventoryItemCount(itemId, -1);
	}
	
	/**
	 * Get the total amount of all specified items in player's inventory.
	 * @param player the player whose inventory to check
	 * @param itemIds a list of IDs of items whose amount to get
	 * @return the summary amount of all listed items in player's inventory
	 */
	public long getQuestItemsCount(L2PcInstance player, int... itemIds) {
		long count = 0;
		for (L2ItemInstance item : player.getInventory().getItems()) {
			if (item == null) {
				continue;
			}
			
			for (int itemId : itemIds) {
				if (item.getId() == itemId) {
					try {
						count = Math.addExact(count, item.getCount());
					} catch (ArithmeticException ae) {
						count = Long.MAX_VALUE;
						break;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Check if the player has the specified item in his inventory.
	 * @param player the player whose inventory to check for the specified item
	 * @param item the {@link ItemHolder} object containing the ID and count of the item to check
	 * @return {@code true} if the player has the required count of the item
	 */
	protected static boolean hasItem(L2PcInstance player, ItemHolder item) {
		return hasItem(player, item, true);
	}
	
	/**
	 * Check if the player has the required count of the specified item in his inventory.
	 * @param player the player whose inventory to check for the specified item
	 * @param item the {@link ItemHolder} object containing the ID and count of the item to check
	 * @param checkCount if {@code true}, check if each item is at least of the count specified in the ItemHolder,<br>
	 *            otherwise check only if the player has the item at all
	 * @return {@code true} if the player has the item
	 */
	protected static boolean hasItem(L2PcInstance player, ItemHolder item, boolean checkCount) {
		if (item == null) {
			return false;
		}
		if (checkCount) {
			return (getQuestItemsCount(player, item.getId()) >= item.getCount());
		}
		return hasQuestItems(player, item.getId());
	}
	
	protected static boolean hasItemsAtLimit(L2PcInstance player, QuestItemChanceHolder... items) {
		if (items == null) {
			return false;
		}
		
		return Arrays.stream(items).allMatch(item -> getQuestItemsCount(player, item.getId()) >= item.getLimit());
	}
	
	/**
	 * Check if the player has all the specified items in his inventory and, if necessary, if their count is also as required.
	 * @param player the player whose inventory to check for the specified item
	 * @param checkCount if {@code true}, check if each item is at least of the count specified in the ItemHolder,<br>
	 *            otherwise check only if the player has the item at all
	 * @param itemList a list of {@link ItemHolder} objects containing the IDs of the items to check
	 * @return {@code true} if the player has all the items from the list
	 */
	protected static boolean hasAllItems(L2PcInstance player, boolean checkCount, ItemHolder... itemList) {
		if ((itemList == null) || (itemList.length == 0)) {
			return false;
		}
		for (ItemHolder item : itemList) {
			if (!hasItem(player, item, checkCount)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check for an item in player's inventory.
	 * @param player the player whose inventory to check for quest items
	 * @param itemId the ID of the item to check for
	 * @return {@code true} if the item exists in player's inventory, {@code false} otherwise
	 */
	public static boolean hasQuestItems(L2PcInstance player, int itemId) {
		return (player.getInventory().getItemByItemId(itemId) != null);
	}
	
	/**
	 * Check for multiple items in player's inventory.
	 * @param player the player whose inventory to check for quest items
	 * @param itemIds a list of item IDs to check for
	 * @return {@code true} if all items exist in player's inventory, {@code false} otherwise
	 */
	public static boolean hasQuestItems(L2PcInstance player, int... itemIds) {
		if ((itemIds == null) || (itemIds.length == 0)) {
			return false;
		}
		final PcInventory inv = player.getInventory();
		for (int itemId : itemIds) {
			if (inv.getItemByItemId(itemId) == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check for multiple items in player's inventory.
	 * @param player the player whose inventory to check for quest items
	 * @param itemIds a list of item IDs to check for
	 * @return {@code true} if at least one item exist in player's inventory, {@code false} otherwise
	 */
	public boolean hasAtLeastOneQuestItem(L2PcInstance player, int... itemIds) {
		final PcInventory inv = player.getInventory();
		for (int itemId : itemIds) {
			if (inv.getItemByItemId(itemId) != null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the enchantment level of an item in player's inventory.
	 * @param player the player whose item to check
	 * @param itemId the ID of the item whose enchantment level to get
	 * @return the enchantment level of the item or 0 if the item was not found
	 */
	public static int getEnchantLevel(L2PcInstance player, int itemId) {
		final L2ItemInstance enchantedItem = player.getInventory().getItemByItemId(itemId);
		if (enchantedItem == null) {
			return 0;
		}
		return enchantedItem.getEnchantLevel();
	}
	
	/**
	 * Give Adena to the player.
	 * @param player the player to whom to give the Adena
	 * @param count the amount of Adena to give
	 * @param applyRates if {@code true} quest rates will be applied to the amount
	 */
	public static void giveAdena(L2PcInstance player, long count, boolean applyRates) {
		if (applyRates) {
			rewardItems(player, Inventory.ADENA_ID, count);
		} else {
			giveItems(player, Inventory.ADENA_ID, count);
		}
	}
	
	/**
	 * Give a reward to player using multipliers.
	 * @param player the player to whom to give the item
	 * @param holder
	 */
	public static void rewardItems(L2PcInstance player, ItemHolder holder) {
		rewardItems(player, holder.getId(), holder.getCount());
	}
	
	/**
	 * Give a reward to player using multipliers.
	 * @param player the player to whom to give the item
	 * @param itemId the ID of the item to give
	 * @param count the amount of items to give
	 */
	public static void rewardItems(L2PcInstance player, int itemId, long count) {
		if (count <= 0) {
			return;
		}
		
		final L2Item item = ItemTable.getInstance().getTemplate(itemId);
		if (item == null) {
			return;
		}
		
		try {
			if (itemId == Inventory.ADENA_ID) {
				count *= rates().getRateQuestRewardAdena();
			} else if (rates().useQuestRewardMultipliers()) {
				if (item instanceof L2EtcItem etc) {
					switch (etc.getItemType()) {
						case POTION -> count *= rates().getRateQuestRewardPotion();
						case SCRL_ENCHANT_WP, SCRL_ENCHANT_AM, SCROLL -> count *= rates().getRateQuestRewardScroll();
						case RECIPE -> count *= rates().getRateQuestRewardRecipe();
						case MATERIAL -> count *= rates().getRateQuestRewardMaterial();
						default -> count *= rates().getRateQuestReward();
					}
				}
			} else {
				count *= rates().getRateQuestReward();
			}
		} catch (Exception e) {
			count = Long.MAX_VALUE;
		}
		
		// Add items to player's inventory
		final L2ItemInstance itemInstance = player.getInventory().addItem("Quest", itemId, count, player, player.getTarget());
		if (itemInstance == null) {
			return;
		}
		
		sendItemGetMessage(player, itemInstance, count);
	}
	
	/**
	 * Send the system message and the status update packets to the player.
	 * @param player the player that has got the item
	 * @param item the item obtain by the player
	 * @param count the item count
	 */
	private static void sendItemGetMessage(L2PcInstance player, L2ItemInstance item, long count) {
		// If item for reward is gold, send message of gold reward to client
		if (item.getId() == Inventory.ADENA_ID) {
			SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_ADENA);
			smsg.addLong(count);
			player.sendPacket(smsg);
		}
		// Otherwise, send message of object reward to client
		else {
			if (count > 1) {
				SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
				smsg.addItemName(item);
				smsg.addLong(count);
				player.sendPacket(smsg);
			} else {
				SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
				smsg.addItemName(item);
				player.sendPacket(smsg);
			}
		}
		// send packets
		StatusUpdate su = new StatusUpdate(player);
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
	}
	
	/**
	 * Give item/reward to the player
	 * @param player
	 * @param itemId
	 * @param count
	 */
	public static void giveItems(L2PcInstance player, int itemId, long count) {
		giveItems(player, itemId, count, 0);
	}
	
	/**
	 * Give item/reward to the player
	 * @param player
	 * @param holder
	 */
	protected static void giveItems(L2PcInstance player, ItemHolder holder) {
		giveItems(player, holder.getId(), holder.getCount());
	}
	
	/**
	 * @param player
	 * @param itemId
	 * @param count
	 * @param enchantlevel
	 */
	public static void giveItems(L2PcInstance player, int itemId, long count, int enchantlevel) {
		if (count <= 0) {
			return;
		}
		
		// Add items to player's inventory
		final L2ItemInstance item = player.getInventory().addItem("Quest", itemId, count, player, player.getTarget());
		if (item == null) {
			return;
		}
		
		// set enchant level for item if that item is not adena
		if ((enchantlevel > 0) && (itemId != Inventory.ADENA_ID)) {
			item.setEnchantLevel(enchantlevel);
		}
		
		sendItemGetMessage(player, item, count);
	}
	
	/**
	 * @param player
	 * @param itemId
	 * @param count
	 * @param attributeId
	 * @param attributeLevel
	 */
	public static void giveItems(L2PcInstance player, int itemId, long count, byte attributeId, int attributeLevel) {
		if (count <= 0) {
			return;
		}
		
		// Add items to player's inventory
		final L2ItemInstance item = player.getInventory().addItem("Quest", itemId, count, player, player.getTarget());
		if (item == null) {
			return;
		}
		
		// set enchant level for item if that item is not adena
		if ((attributeId >= 0) && (attributeLevel > 0)) {
			item.setElementAttr(attributeId, attributeLevel);
			if (item.isEquipped()) {
				item.updateElementAttrBonus(player);
			}
			
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(item);
			player.sendPacket(iu);
		}
		
		sendItemGetMessage(player, item, count);
	}
	
	/**
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	@Deprecated
	public static boolean giveItemRandomly(L2PcInstance player, int itemId, long amountToGive, long limit, double dropChance, boolean playSound) {
		return giveItemRandomly(player, null, player, singleDropItem(itemId, amountToGive, amountToGive, dropChance * 100), limit, playSound);
	}
	
	/**
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	@Deprecated
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, int itemId, long amountToGive, long limit, double dropChance, boolean playSound) {
		return giveItemRandomly(player, npc, player, singleDropItem(itemId, amountToGive, amountToGive, dropChance * 100), limit, playSound);
	}
	
	/**
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	@Deprecated
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, int itemId, long minAmount, long maxAmount, long limit, double dropChance, boolean playSound) {
		return giveItemRandomly(player, npc, player, singleDropItem(itemId, minAmount, maxAmount, dropChance * 100), limit, playSound);
	}
	
	/**
	 * For one-off use when no {@link QuestDroplist} has been created.
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, int itemId, boolean playSound) {
		return giveItemRandomly(player, npc, player, singleDropItem(itemId, 1, 1, 100.0), 0, playSound);
	}
	
	/**
	 * For one-off use when no {@link QuestDroplist} has been created.
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, QuestItemChanceHolder questItem, boolean playSound) {
		return giveItemRandomly(player, npc, player, singleDropItem(questItem), questItem.getLimit(), playSound);
	}
	
	/**
	 * For use with {@link QuestDroplist} elements.
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, QuestDropInfo dropInfo, boolean playSound) {
		if (dropInfo == null) {
			return false;
		}
		return giveItemRandomly(player, npc, player, dropInfo.drop(), dropInfo.getLimit(), playSound);
	}
	
	/**
	 * @see AbstractScript#giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound)
	 */
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, IDropItem dropItem, long limit, boolean playSound) {
		return giveItemRandomly(player, npc, player, dropItem, limit, playSound);
	}
	
	/**
	 * Give the specified player a random amount of items if he is lucky enough.<br>
	 * Not recommended to use this for non-stacking items.
	 * @param player the player to give the item(s) to
	 * @param npc the NPC that "dropped" the item (can be null)
	 * @param killer the player who killed the NPC
	 * @param dropItem the item or item group to drop
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached. 0 - no limit.
	 * @param playSound if true, plays ItemSound.quest_itemget when items are given and ItemSound.quest_middle when the limit is reached
	 * @return {@code true} if limit > 0 and the limit was reached or if limit <= 0 and items were given; {@code false} in all other cases
	 */
	public static boolean giveItemRandomly(L2PcInstance player, L2Npc npc, L2PcInstance killer, IDropItem dropItem, long limit, boolean playSound) {
		if (dropItem == null) {
			return false;
		}
		
		List<ItemHolder> drops = dropItem.calculateDrops(npc, killer);
		if ((drops == null) || drops.isEmpty()) {
			return false;
		}
		
		ItemHolder drop = drops.get(0);
		
		final long currentCount = getQuestItemsCount(player, drop.getId());
		
		if ((limit > 0) && (currentCount >= limit)) {
			return true;
		}
		
		long amountToGive = drop.getCount();
		// Inventory slot check (almost useless for non-stacking items)
		if ((amountToGive > 0) && player.getInventory().validateCapacityByItemId(drop.getId())) {
			if ((limit > 0) && ((currentCount + amountToGive) > limit)) {
				amountToGive = limit - currentCount;
			}
			
			// Give the item to player
			L2ItemInstance item = player.addItem("Quest", drop.getId(), amountToGive, npc, true);
			if (item != null) {
				// limit reached (if there is no limit, this block doesn't execute)
				if ((currentCount + amountToGive) == limit) {
					if (playSound) {
						playSound(player, Sound.ITEMSOUND_QUEST_MIDDLE);
					}
					return true;
				}
				
				if (playSound) {
					playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
				}
				// if there is no limit, return true every time an item is given
				return limit <= 0;
			}
		}
		return false;
	}
	
	/**
	 * Gives an item to the player
	 * @param player
	 * @param item
	 * @param victim the character that "dropped" the item
	 * @return <code>true</code> if at least one item was given, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim) {
		List<ItemHolder> items = item.calculateDrops(victim, player);
		if ((items == null) || items.isEmpty()) {
			return false;
		}
		giveItems(player, items);
		return true;
	}
	
	/**
	 * Gives an item to the player
	 * @param player
	 * @param items
	 */
	protected static void giveItems(L2PcInstance player, List<ItemHolder> items) {
		for (ItemHolder item : items) {
			giveItems(player, item);
		}
		
	}
	
	/**
	 * Gives an item to the player
	 * @param player
	 * @param item
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached.
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, ItemHolder item, long limit) {
		long maxToGive = limit - player.getInventory().getInventoryItemCount(item.getId(), -1);
		if (maxToGive <= 0) {
			return false;
		}
		giveItems(player, item.getId(), Math.min(maxToGive, item.getCount()));
		return true;
	}
	
	protected static boolean giveItems(L2PcInstance player, ItemHolder item, long limit, boolean playSound) {
		boolean drop = giveItems(player, item, limit);
		if (drop && playSound) {
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
		return drop;
	}
	
	/**
	 * @param player
	 * @param items
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached.
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, long limit) {
		boolean b = false;
		for (ItemHolder item : items) {
			b |= giveItems(player, item, limit);
		}
		return b;
	}
	
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, long limit, boolean playSound) {
		boolean drop = giveItems(player, items, limit);
		if (drop && playSound) {
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
		return drop;
	}
	
	/**
	 * @param player
	 * @param items
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached. If a no limit for an itemId is specified, item will always be given
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, Map<Integer, Long> limit) {
		return giveItems(player, items, Util.mapToFunction(limit));
	}
	
	/**
	 * @param player
	 * @param items
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached. If a no limit for an itemId is specified, item will always be given
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, Function<Integer, Long> limit) {
		boolean b = false;
		for (ItemHolder item : items) {
			if (limit != null) {
				Long longLimit = limit.apply(item.getId());
				// null -> no limit specified for that item id. This trick is to avoid limit.apply() be called twice (once for the null check)
				if (longLimit != null) {
					b |= giveItems(player, item, longLimit);
					continue; // the item is given, continue with next
				}
			}
			// da BIG else
			// no limit specified here (either limit or limit.apply(item.getId()) is null)
			b = true;
			giveItems(player, item);
			
		}
		return b;
	}
	
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, Function<Integer, Long> limit, boolean playSound) {
		boolean drop = giveItems(player, items, limit);
		if (drop && playSound) {
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
		return drop;
	}
	
	protected static boolean giveItems(L2PcInstance player, List<ItemHolder> items, Map<Integer, Long> limit, boolean playSound) {
		return giveItems(player, items, Util.mapToFunction(limit), playSound);
	}
	
	/**
	 * @param player
	 * @param item
	 * @param victim the character that "dropped" the item
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached.
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, int limit) {
		return giveItems(player, item.calculateDrops(victim, player), limit);
	}
	
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, int limit, boolean playSound) {
		boolean drop = giveItems(player, item, victim, limit);
		if (drop && playSound) {
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
		return drop;
	}
	
	/**
	 * @param player
	 * @param item
	 * @param victim the character that "dropped" the item
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached. If a no limit for an itemId is specified, item will always be given
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, Map<Integer, Long> limit) {
		return giveItems(player, item.calculateDrops(victim, player), limit);
	}
	
	/**
	 * @param player
	 * @param item
	 * @param victim the character that "dropped" the item
	 * @param limit the maximum amount of items the player can have. Won't give more if this limit is reached. If a no limit for an itemId is specified, item will always be given
	 * @return <code>true</code> if at least one item was given to the player, <code>false</code> otherwise
	 */
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, Function<Integer, Long> limit) {
		return giveItems(player, item.calculateDrops(victim, player), limit);
	}
	
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, Map<Integer, Long> limit, boolean playSound) {
		return giveItems(player, item, victim, Util.mapToFunction(limit), playSound);
	}
	
	protected static boolean giveItems(L2PcInstance player, IDropItem item, L2Character victim, Function<Integer, Long> limit, boolean playSound) {
		boolean drop = giveItems(player, item, victim, limit);
		if (drop && playSound) {
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
		return drop;
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, Collection<ItemHolder> items, Function<Integer, Long> limit, boolean playSound) {
		Map<L2PcInstance, Map<Integer, Long>> rewardedCounts = calculateDistribution(players, items, limit);
		// now give the calculated items to the players
		giveItems(rewardedCounts, playSound);
		return rewardedCounts;
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, Collection<ItemHolder> items, Map<Integer, Long> limit, boolean playSound) {
		return distributeItems(players, items, Util.mapToFunction(limit), playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, Collection<ItemHolder> items, long limit, boolean playSound) {
		return distributeItems(players, items, t -> limit, playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param item the items to distribute
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Long> distributeItems(Collection<L2PcInstance> players, ItemHolder item, long limit, boolean playSound) {
		Map<L2PcInstance, Map<Integer, Long>> distribution = distributeItems(players, Collections.singletonList(item), limit, playSound);
		Map<L2PcInstance, Long> returnMap = new HashMap<>();
		for (Entry<L2PcInstance, Map<Integer, Long>> entry : distribution.entrySet()) {
			for (Entry<Integer, Long> entry2 : entry.getValue().entrySet()) {
				returnMap.put(entry.getKey(), entry2.getValue());
			}
		}
		return returnMap;
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, IDropItem items, L2Character killer, L2Character victim, Function<Integer, Long> limit, boolean playSound) {
		return distributeItems(players, items.calculateDrops(victim, killer), limit, playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, IDropItem items, L2Character killer, L2Character victim, Map<Integer, Long> limit, boolean playSound) {
		return distributeItems(players, items.calculateDrops(victim, killer), limit, playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, IDropItem items, L2Character killer, L2Character victim, long limit, boolean playSound) {
		return distributeItems(players, items.calculateDrops(victim, killer), limit, playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @param smartDrop true if to not calculate a drop, which can't be given to any player 'cause of limits
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, final GroupedGeneralDropItem items, L2Character killer, L2Character victim, Function<Integer, Long> limit, boolean playSound, boolean smartDrop) {
		GroupedGeneralDropItem toDrop;
		if (smartDrop) {
			toDrop = new GroupedGeneralDropItem(items.getChance(), items.getDropCalculationStrategy(), items.getKillerChanceModifierStrategy(), items.getPreciseStrategy());
			List<GeneralDropItem> dropItems = new LinkedList<>(items.getItems());
			ITEM_LOOP:
			for (Iterator<GeneralDropItem> it = dropItems.iterator(); it.hasNext();) {
				GeneralDropItem item = it.next();
				for (L2PcInstance player : players) {
					int itemId = item.getItemId();
					if (player.getInventory().getInventoryItemCount(itemId, -1, true) < avoidNull(limit, itemId)) {
						// we can give this item to this player
						continue ITEM_LOOP;
					}
				}
				// there's nobody to give this item to
				it.remove();
			}
			toDrop.setItems(dropItems);
			toDrop = toDrop.normalizeMe(victim, killer);
		} else {
			toDrop = items;
		}
		return distributeItems(players, toDrop, killer, victim, limit, playSound);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @param smartDrop true if to not calculate a drop, which can't be given to any player
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, final GroupedGeneralDropItem items, L2Character killer, L2Character victim, Map<Integer, Long> limit, boolean playSound, boolean smartDrop) {
		return distributeItems(players, items, killer, victim, Util.mapToFunction(limit), playSound, smartDrop);
	}
	
	/**
	 * Distributes items to players equally
	 * @param players the players to whom the items will be distributed
	 * @param items the items to distribute
	 * @param killer the one who "kills" the victim
	 * @param victim the character that "dropped" the item
	 * @param limit the limit what single player can have of each item
	 * @param playSound if to play sound if a player gets at least one item
	 * @param smartDrop true if to not calculate a drop, which can't be given to any player
	 * @return the counts of each items given to each player
	 */
	protected static Map<L2PcInstance, Map<Integer, Long>> distributeItems(Collection<L2PcInstance> players, final GroupedGeneralDropItem items, L2Character killer, L2Character victim, long limit, boolean playSound, boolean smartDrop) {
		return distributeItems(players, items, killer, victim, t -> limit, playSound, smartDrop);
	}
	
	/**
	 * @param players
	 * @param items
	 * @param limit
	 * @return
	 */
	private static Map<L2PcInstance, Map<Integer, Long>> calculateDistribution(Collection<L2PcInstance> players, Collection<ItemHolder> items, Function<Integer, Long> limit) {
		Map<L2PcInstance, Map<Integer, Long>> rewardedCounts = new HashMap<>();
		for (L2PcInstance player : players) {
			rewardedCounts.put(player, new HashMap<>());
		}
		NEXT_ITEM:
		for (ItemHolder item : items) {
			long equaldist = item.getCount() / players.size();
			long randomdist = item.getCount() % players.size();
			List<L2PcInstance> toDist = new ArrayList<>(players);
			do // this must happen at least once in order to get away already full players (and then equaldist can become nonzero)
			{
				for (Iterator<L2PcInstance> it = toDist.iterator(); it.hasNext();) {
					L2PcInstance player = it.next();
					if (!rewardedCounts.get(player).containsKey(item.getId())) {
						rewardedCounts.get(player).put(item.getId(), 0L);
					}
					long maxGive = avoidNull(limit, item.getId()) - player.getInventory().getInventoryItemCount(item.getId(), -1, true) - rewardedCounts.get(player).get(item.getId());
					long toGive = equaldist;
					if (equaldist >= maxGive) {
						toGive = maxGive;
						randomdist += (equaldist - maxGive); // overflown items are available to next players
						it.remove(); // this player is already full
					}
					rewardedCounts.get(player).put(item.getId(), rewardedCounts.get(player).get(item.getId()) + toGive);
				}
				if (toDist.isEmpty()) {
					// there's no one to give items anymore, all players will be full when we give the items
					continue NEXT_ITEM;
				}
				equaldist = randomdist / toDist.size(); // the rest of items may be allowed to be equally distributed between remaining players
				randomdist %= toDist.size();
			}
			while (equaldist > 0);
			while (randomdist > 0) {
				if (toDist.isEmpty()) {
					// we don't have any player left
					continue NEXT_ITEM;
				}
				L2PcInstance player = toDist.get(getRandom(toDist.size()));
				// avoid null return
				long maxGive = avoidNull(limit, item.getId()) - limit.apply(item.getId()) - player.getInventory().getInventoryItemCount(item.getId(), -1, true) - rewardedCounts.get(player).get(item.getId());
				if (maxGive > 0) {
					// we can add an item to player
					// so we add one item to player
					rewardedCounts.get(player).put(item.getId(), rewardedCounts.get(player).get(item.getId()) + 1);
					randomdist--;
				}
				toDist.remove(player); // Either way this player isn't allowable for next random award
			}
		}
		return rewardedCounts;
	}
	
	/**
	 * This function is for avoidance null returns in function limits
	 * @param <T> the type of function arg
	 * @param function the function
	 * @param arg the argument
	 * @return {@link Long#MAX_VALUE} if function.apply(arg) is null, function.apply(arg) otherwise
	 */
	private static <T> long avoidNull(Function<T, Long> function, T arg) {
		Long longLimit = function.apply(arg);
		return longLimit == null ? Long.MAX_VALUE : longLimit;
	}
	
	/**
	 * Distributes items to players
	 * @param rewardedCounts A scheme of distribution items (the structure is: Map<player Map<itemId, count>>)
	 * @param playSound if to play sound if a player gets at least one item
	 */
	private static void giveItems(Map<L2PcInstance, Map<Integer, Long>> rewardedCounts, boolean playSound) {
		for (Entry<L2PcInstance, Map<Integer, Long>> entry : rewardedCounts.entrySet()) {
			L2PcInstance player = entry.getKey();
			boolean playPlayerSound = false;
			for (Entry<Integer, Long> item : entry.getValue().entrySet()) {
				if (item.getValue() >= 0) {
					playPlayerSound = true;
					giveItems(player, item.getKey(), item.getValue());
				}
			}
			if (playSound && playPlayerSound) {
				playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
	}
	
	/**
	 * Take an amount of a specified item from player's inventory.
	 * @param player the player whose item to take
	 * @param itemId the ID of the item to take
	 * @param amount the amount to take
	 * @return {@code true} if any items were taken, {@code false} otherwise
	 */
	public static boolean takeItems(L2PcInstance player, int itemId, long amount) {
		final List<L2ItemInstance> items = player.getInventory().getItemsByItemId(itemId);
		if (amount < 0) {
			items.forEach(i -> takeItem(player, i, i.getCount()));
		} else {
			long currentCount = 0;
			for (L2ItemInstance i : items) {
				long toDelete = i.getCount();
				if ((currentCount + toDelete) > amount) {
					toDelete = amount - currentCount;
				}
				takeItem(player, i, toDelete);
				currentCount += toDelete;
			}
		}
		return true;
	}
	
	private static boolean takeItem(L2PcInstance player, L2ItemInstance item, long toDelete) {
		if (item.isEquipped()) {
			final L2ItemInstance[] unequiped = player.getInventory().unEquipItemInBodySlotAndRecord(item.getItem().getBodyPart());
			final InventoryUpdate iu = new InventoryUpdate();
			for (L2ItemInstance itm : unequiped) {
				iu.addModifiedItem(itm);
			}
			player.sendPacket(iu);
			player.broadcastUserInfo();
		}
		return player.destroyItemByItemId("Quest", item.getId(), toDelete, player, true);
	}
	
	/**
	 * Take a set amount of a specified item from player's inventory.
	 * @param player the player whose item to take
	 * @param holder the {@link ItemHolder} object containing the ID and count of the item to take
	 * @return {@code true} if the item was taken, {@code false} otherwise
	 */
	protected static boolean takeItem(L2PcInstance player, ItemHolder holder) {
		if (holder == null) {
			return false;
		}
		return takeItems(player, holder.getId(), holder.getCount());
	}
	
	/**
	 * Take a set amount of all specified items from player's inventory.
	 * @param player the player whose items to take
	 * @param itemList the list of {@link ItemHolder} objects containing the IDs and counts of the items to take
	 * @return {@code true} if all items were taken, {@code false} otherwise
	 */
	protected static boolean takeAllItems(L2PcInstance player, ItemHolder... itemList) {
		if ((itemList == null) || (itemList.length == 0)) {
			return false;
		}
		// first check if the player has all items to avoid taking half the items from the list
		if (!hasAllItems(player, true, itemList)) {
			return false;
		}
		for (ItemHolder item : itemList) {
			// this should never be false, but just in case
			if (!takeItem(player, item)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Take an amount of all specified items from player's inventory.
	 * @param player the player whose items to take
	 * @param amount the amount to take of each item
	 * @param itemIds a list or an array of IDs of the items to take
	 * @return {@code true} if all items were taken, {@code false} otherwise
	 */
	public static boolean takeItems(L2PcInstance player, int amount, int... itemIds) {
		boolean check = true;
		if (itemIds != null) {
			for (int item : itemIds) {
				check &= takeItems(player, item, amount);
			}
		}
		return check;
	}
	
	/**
	 * Send a packet in order to play a sound to the player.
	 * @param player the player whom to send the packet
	 * @param sound the {@link IAudio} object of the sound to play
	 */
	public static void playSound(L2PcInstance player, IAudio sound) {
		player.sendPacket(sound.getPacket());
	}
	
	/**
	 * Add EXP and SP as quest reward.
	 * @param player the player whom to reward with the EXP/SP
	 * @param exp the amount of EXP to give to the player
	 * @param sp the amount of SP to give to the player
	 */
	public static void addExpAndSp(L2PcInstance player, long exp, int sp) {
		player.addExpAndSpQuest((long) (exp * rates().getRateQuestRewardXP()), (int) (sp * rates().getRateQuestRewardSP()));
	}
	
	public static double getRandom() {
		return Rnd.get();
	}
	
	/**
	 * Get a random integer from 0 (inclusive) to {@code max} (exclusive).<br>
	 * Use this method instead of importing {@link com.l2jserver.commons.util.Rnd} utility.
	 * @param max the maximum value for randomization
	 * @return a random integer number from 0 to {@code max - 1}
	 */
	public static int getRandom(int max) {
		return Rnd.get(max);
	}
	
	/**
	 * Get a random integer from {@code min} (inclusive) to {@code max} (inclusive).<br>
	 * Use this method instead of importing {@link com.l2jserver.commons.util.Rnd} utility.
	 * @param min the minimum value for randomization
	 * @param max the maximum value for randomization
	 * @return a random integer number from {@code min} to {@code max}
	 */
	public static int getRandom(int min, int max) {
		return Rnd.get(min, max);
	}
	
	/**
	 * Get a random boolean.<br>
	 * Use this method instead of importing {@link com.l2jserver.commons.util.Rnd} utility.
	 * @return {@code true} or {@code false} randomly
	 */
	public static boolean getRandomBoolean() {
		return Rnd.nextBoolean();
	}
	
	/**
	 * Get the ID of the item equipped in the specified inventory slot of the player.
	 * @param player the player whose inventory to check
	 * @param slot the location in the player's inventory to check
	 * @return the ID of the item equipped in the specified inventory slot or 0 if the slot is empty or item is {@code null}.
	 */
	public static int getItemEquipped(L2PcInstance player, int slot) {
		return player.getInventory().getPaperdollItemId(slot);
	}
	
	/**
	 * @return the number of ticks from the {@link com.l2jserver.gameserver.GameTimeController}.
	 */
	public static int getGameTicks() {
		return GameTimeController.getInstance().getGameTicks();
	}
	
	/**
	 * Execute a procedure for each player depending on the parameters.
	 * @param player the player on which the procedure will be executed
	 * @param npc the related NPC
	 * @param isSummon {@code true} if the event that called this method was originated by the player's summon, {@code false} otherwise
	 * @param includeParty if {@code true}, #actionForEachPlayer(L2PcInstance, L2Npc, boolean) will be called with the player's party members
	 * @param includeCommandChannel if {@code true}, {@link #actionForEachPlayer(L2PcInstance, L2Npc, boolean)} will be called with the player's command channel members
	 * @see #actionForEachPlayer(L2PcInstance, L2Npc, boolean)
	 */
	public final void executeForEachPlayer(L2PcInstance player, final L2Npc npc, final boolean isSummon, boolean includeParty, boolean includeCommandChannel) {
		if ((includeParty || includeCommandChannel) && player.isInParty()) {
			if (includeCommandChannel && player.getParty().isInCommandChannel()) {
				player.getParty().getCommandChannel().forEachMember(member -> {
					actionForEachPlayer(member, npc, isSummon);
					return true;
				});
			} else if (includeParty) {
				player.getParty().forEachMember(member -> {
					actionForEachPlayer(member, npc, isSummon);
					return true;
				});
			}
		} else {
			actionForEachPlayer(player, npc, isSummon);
		}
	}
	
	/**
	 * Overridable method called from {@link #executeForEachPlayer(L2PcInstance, L2Npc, boolean, boolean, boolean)}
	 * @param player the player on which the action will be run
	 * @param npc the NPC related to this action
	 * @param isSummon {@code true} if the event that called this method was originated by the player's summon
	 */
	public void actionForEachPlayer(L2PcInstance player, L2Npc npc, boolean isSummon) {
		// To be overridden in quest scripts.
	}
	
	/**
	 * Open a door if it is present on the instance and it's not open.
	 * @param doorId the ID of the door to open
	 * @param instanceId the ID of the instance the door is in (0 if the door is not inside an instance)
	 */
	public void openDoor(int doorId, int instanceId) {
		final var door = getDoor(doorId, instanceId);
		if (door == null) {
			LOG.warn("Called openDoor({}, {}); but door wasn't found!", doorId, instanceId);
		} else if (!door.getOpen()) {
			door.openMe();
		}
	}
	
	/**
	 * Close a door if it is present in a specified the instance and its open.
	 * @param doorId the ID of the door to close
	 * @param instanceId the ID of the instance the door is in (0 if the door is not inside an instance)
	 */
	public void closeDoor(int doorId, int instanceId) {
		final var door = getDoor(doorId, instanceId);
		if (door == null) {
			LOG.warn("Called closeDoor({}, {}); but door wasn't found!", doorId, instanceId);
		} else if (door.getOpen()) {
			door.closeMe();
		}
	}
	
	/**
	 * Retrieve a door from an instance or the real world.
	 * @param doorId the ID of the door to get
	 * @param instanceId the ID of the instance the door is in (0 if the door is not inside an instance)
	 * @return the found door or {@code null} if no door with that ID and instance ID was found
	 */
	public L2DoorInstance getDoor(int doorId, int instanceId) {
		L2DoorInstance door = null;
		if (instanceId <= 0) {
			door = DoorData.getInstance().getDoor(doorId);
		} else {
			final Instance inst = InstanceManager.getInstance().getInstance(instanceId);
			if (inst != null) {
				door = inst.getDoor(doorId);
			}
		}
		return door;
	}
	
	/**
	 * Teleport a player into/out of an instance.
	 * @param player the player to teleport
	 * @param loc the {@link Location} object containing the destination coordinates
	 * @param instanceId the ID of the instance to teleport the player to (0 to teleport out of an instance)
	 */
	public void teleportPlayer(L2PcInstance player, Location loc, int instanceId) {
		teleportPlayer(player, loc, instanceId, true);
	}
	
	/**
	 * Teleport a player into/out of an instance.
	 * @param player the player to teleport
	 * @param loc the {@link Location} object containing the destination coordinates
	 * @param instanceId the ID of the instance to teleport the player to (0 to teleport out of an instance)
	 * @param allowRandomOffset if {@code true}, will randomize the teleport coordinates by +/- MaxOffsetOnTeleport
	 */
	public void teleportPlayer(L2PcInstance player, Location loc, int instanceId, boolean allowRandomOffset) {
		player.teleToLocation(loc, instanceId, allowRandomOffset ? character().getMaxOffsetOnTeleport() : 0);
	}
	
	/**
	 * Monster is running and attacking the playable.
	 * @param npc the NPC that performs the attack
	 * @param creature the target of the attack
	 */
	protected void addAttackDesire(L2Npc npc, L2Character creature) {
		addAttackDesire(npc, creature, 999);
	}
	
	/**
	 * Monster is running and attacking the target.
	 * @param npc the NPC that performs the attack
	 * @param creature the target of the attack
	 * @param desire the desire to perform the attack
	 */
	protected void addAttackDesire(L2Npc npc, L2Character creature, long desire) {
		if (npc instanceof L2Attackable attackable) {
			attackable.addDamageHate(creature, 0, desire);
		}
		npc.setIsRunning(true);
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, creature);
	}
	
	/**
	 * Adds desire to move to the given NPC.
	 * @param npc the NPC
	 * @param loc the location
	 * @param desire the desire
	 */
	protected void addMoveToDesire(L2Npc npc, Location loc, int desire) {
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, loc);
	}
	
	/**
	 * Instantly cast a skill upon the given target.
	 * @param npc the caster NPC
	 * @param target the target of the cast
	 * @param skill the skill to cast
	 */
	protected void castSkill(L2Npc npc, L2Playable target, SkillHolder skill) {
		npc.setTarget(target);
		npc.doCast(skill.getSkill());
	}
	
	/**
	 * Instantly cast a skill upon the given target.
	 * @param npc the caster NPC
	 * @param target the target of the cast
	 * @param skill the skill to cast
	 */
	protected void castSkill(L2Npc npc, L2Playable target, Skill skill) {
		npc.setTarget(target);
		npc.doCast(skill);
	}
	
	/**
	 * Adds the desire to cast a skill to the given NPC.
	 * @param npc the NPC who casts the skill
	 * @param target the skill target
	 * @param skill the skill to cast
	 * @param desire the desire to cast the skill
	 */
	protected void addSkillCastDesire(L2Npc npc, L2Character target, SkillHolder skill, long desire) {
		addSkillCastDesire(npc, target, skill.getSkill(), desire);
	}
	
	/**
	 * Adds the desire to cast a skill to the given NPC.
	 * @param npc the NPC who casts the skill
	 * @param target the skill target
	 * @param skill the skill to cast
	 * @param desire the desire to cast the skill
	 */
	protected void addSkillCastDesire(L2Npc npc, L2Character target, Skill skill, long desire) {
		if (npc instanceof L2Attackable attackable) {
			attackable.addDamageHate(target, 0, desire);
		}
		npc.setTarget(target);
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
	}
	
	/**
	 * Sends the special camera packet to the player.
	 * @param player the player
	 * @param creature the watched creature
	 * @param force
	 * @param angle1
	 * @param angle2
	 * @param time
	 * @param range
	 * @param duration
	 * @param relYaw
	 * @param relPitch
	 * @param isWide
	 * @param relAngle
	 */
	public static void specialCamera(L2PcInstance player, L2Character creature, int force, int angle1, int angle2, int time, int range, int duration, int relYaw, int relPitch, int isWide, int relAngle) {
		player.sendPacket(new SpecialCamera(creature, force, angle1, angle2, time, range, duration, relYaw, relPitch, isWide, relAngle));
	}
	
	/**
	 * Sends the special camera packet to the player.
	 * @param player
	 * @param creature
	 * @param force
	 * @param angle1
	 * @param angle2
	 * @param time
	 * @param duration
	 * @param relYaw
	 * @param relPitch
	 * @param isWide
	 * @param relAngle
	 */
	public static void specialCameraEx(L2PcInstance player, L2Character creature, int force, int angle1, int angle2, int time, int duration, int relYaw, int relPitch, int isWide, int relAngle) {
		player.sendPacket(new SpecialCamera(creature, player, force, angle1, angle2, time, duration, relYaw, relPitch, isWide, relAngle));
	}
	
	/**
	 * Sends the special camera packet to the player.
	 * @param player
	 * @param creature
	 * @param force
	 * @param angle1
	 * @param angle2
	 * @param time
	 * @param range
	 * @param duration
	 * @param relYaw
	 * @param relPitch
	 * @param isWide
	 * @param relAngle
	 * @param unk
	 */
	public static void specialCamera3(L2PcInstance player, L2Character creature, int force, int angle1, int angle2, int time, int range, int duration, int relYaw, int relPitch, int isWide, int relAngle, int unk) {
		player.sendPacket(new SpecialCamera(creature, force, angle1, angle2, time, range, duration, relYaw, relPitch, isWide, relAngle, unk));
	}
	
	/**
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void addRadar(L2PcInstance player, int x, int y, int z) {
		player.getRadar().addMarker(x, y, z);
	}
	
	/**
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	public void removeRadar(L2PcInstance player, int x, int y, int z) {
		player.getRadar().removeMarker(x, y, z);
	}
	
	/**
	 * @param player
	 */
	public void clearRadar(L2PcInstance player) {
		player.getRadar().removeAllMarkers();
	}
}
