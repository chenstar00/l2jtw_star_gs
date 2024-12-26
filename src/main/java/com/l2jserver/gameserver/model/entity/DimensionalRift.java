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
package com.l2jserver.gameserver.model.entity;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.instancemanager.DimensionalRiftManager;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.Earthquake;
import com.l2jserver.gameserver.util.Util;

/**
 * Thanks to L2Fortress and balancer.ru - kombat
 */
public class DimensionalRift {
	protected byte _type;
	protected L2Party _party;
	protected List<Byte> _completedRooms = new ArrayList<>();
	private static final long seconds_5 = 5000L;
	
	protected byte jumps_current = 0;
	private final int MAX_DISTANCE = 1500;
	
	private Timer teleporterTimer;
	private TimerTask teleporterTimerTask;
	private Timer spawnTimer;
	private TimerTask spawnTimerTask;
	
	private Future<?> earthQuakeTask;
	
	protected byte _chosenRoom;
	protected List<L2PcInstance> _playerInside = new CopyOnWriteArrayList<>();
	protected List<L2PcInstance> _deadPlayers = new CopyOnWriteArrayList<>();
	protected List<L2PcInstance> _revivedInWaitingRoom = new CopyOnWriteArrayList<>();
	private boolean isBossRoom = false;
	
	private final int Q00635_IntoTheDimensionalRift = 635;
	
	public DimensionalRift(L2Npc npc, L2Party party, byte type, byte room) {
		DimensionalRiftManager.getInstance().getRoom(type, room).setParty(party);
		
		_type = type;
		_party = party;
		_chosenRoom = room;
		party.setDimensionalRift(this);
		for (L2PcInstance pc : party.getMembers()) {
			final Quest q635 = QuestManager.getInstance().getQuest(Q00635_IntoTheDimensionalRift);
			if (q635 != null) {
				QuestState qs = pc.getQuestState(q635.getName());
				if (qs == null) {
					qs = q635.newQuestState(pc);
				}
				if (!qs.isStarted()) {
					qs.setState(State.STARTED, true);
				}
			}
			
			final double distance = Util.calculateDistance(npc, pc, true, false);
			if (distance <= MAX_DISTANCE) {
				teleportToRoom(pc, room);
				_playerInside.add(pc);
			}
		}
		
		createSpawnTimer(_chosenRoom);
		createTeleporterTimer(true);
	}
	
	public byte getType() {
		return _type;
	}
	
	public byte getCurrentRoom() {
		return _chosenRoom;
	}
	
	protected void createTeleporterTimer(final boolean reasonTP) {
		if (_party == null) {
			return;
		}
		
		if (teleporterTimerTask != null) {
			teleporterTimerTask.cancel();
			teleporterTimerTask = null;
		}
		
		if (teleporterTimer != null) {
			teleporterTimer.cancel();
			teleporterTimer = null;
		}
		
		if (earthQuakeTask != null) {
			earthQuakeTask.cancel(false);
			earthQuakeTask = null;
		}
		
		teleporterTimer = new Timer();
		teleporterTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (_chosenRoom > -1) {
					DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).unspawn().setParty(null);
				}
				
				if (reasonTP && (jumps_current < getMaxJumps()) && (_party.getMemberCount() > _deadPlayers.size())) {
					jumps_current++;
					
					_completedRooms.add(_chosenRoom);
					_chosenRoom = -1;
					
					for (L2PcInstance p : _party.getMembers()) {
						if (!_revivedInWaitingRoom.contains(p) && _playerInside.contains(p)) {
							teleportToNextRoom(p);
						}
					}
					
					createTeleporterTimer(true);
					createSpawnTimer(_chosenRoom);
				} else {
					for (L2PcInstance p : _party.getMembers()) {
						if (!_revivedInWaitingRoom.contains(p) && _playerInside.contains(p)) {
							teleportToWaitingRoom(p);
						}
					}
					
					killRift();
					cancel();
				}
			}
		};
		
		if (reasonTP) {
			long jumpTime = calcTimeToNextJump();
			teleporterTimer.schedule(teleporterTimerTask, jumpTime); // Teleporter task, 8-10 minutes
			
			earthQuakeTask = ThreadPoolManager.getInstance().scheduleGeneral(() -> {
				for (L2PcInstance p : _party.getMembers()) {
					if (!_revivedInWaitingRoom.contains(p) && _playerInside.contains(p)) {
						p.sendPacket(new Earthquake(p.getX(), p.getY(), p.getZ(), 65, 9));
					}
				}
			}, jumpTime - 7000);
		} else {
			teleporterTimer.schedule(teleporterTimerTask, seconds_5); // incorrect party member invited.
		}
	}
	
	public void createSpawnTimer(final byte room) {
		if (spawnTimerTask != null) {
			spawnTimerTask.cancel();
			spawnTimerTask = null;
		}
		
		if (spawnTimer != null) {
			spawnTimer.cancel();
			spawnTimer = null;
		}
		
		spawnTimer = new Timer();
		spawnTimerTask = new TimerTask() {
			@Override
			public void run() {
				DimensionalRiftManager.getInstance().getRoom(_type, room).spawn();
			}
		};
		
		spawnTimer.schedule(spawnTimerTask, general().getRiftSpawnDelay());
	}
	
	public void partyMemberInvited() {
		createTeleporterTimer(false);
	}
	
	public void partyMemberExited(L2PcInstance player) {
		_deadPlayers.remove(player);
		_revivedInWaitingRoom.remove(player);
		
		killRift();
	}
	
	public void manualTeleport(L2PcInstance player) {
		if (!player.isInParty() || !player.getParty().isInDimensionalRift()) {
			return;
		}
		
		DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).unspawn().setParty(null);
		_completedRooms.add(_chosenRoom);
		_chosenRoom = -1;
		
		for (L2PcInstance p : _party.getMembers()) {
			if (_playerInside.contains(p)) {
				teleportToNextRoom(p);
			}
		}
		
		DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).setParty(player.getParty());
		
		createSpawnTimer(_chosenRoom);
		createTeleporterTimer(true);
	}
	
	public void manualExitRift(L2PcInstance player) {
		if (!player.isInParty() || !player.getParty().isInDimensionalRift()) {
			return;
		}
		
		for (L2PcInstance p : player.getParty().getMembers()) {
			if (_playerInside.contains(p)) {
				teleportToWaitingRoom(p);
			}
		}
		
		killRift();
	}
	
	protected void teleportToNextRoom(L2PcInstance player) {
		if (_chosenRoom == -1) {
			List<Byte> emptyRooms;
			do {
				emptyRooms = DimensionalRiftManager.getInstance().getFreeRooms(_type);
				// Do not tp in the same room a second time
				emptyRooms.removeAll(_completedRooms);
				// If no room left, find any empty
				if (emptyRooms.isEmpty()) {
					emptyRooms = DimensionalRiftManager.getInstance().getFreeRooms(_type);
				}
				_chosenRoom = emptyRooms.get(Rnd.get(1, emptyRooms.size()) - 1);
			}
			while (DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).getParty() != null);
		}
		
		DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).setParty(player.getParty());
		checkBossRoom(_chosenRoom);
		
		teleportToRoom(player, _chosenRoom);
	}
	
	protected void teleportToWaitingRoom(L2PcInstance player) {
		DimensionalRiftManager.getInstance().teleportToWaitingRoom(player);
	}
	
	public void killRift() {
		_completedRooms.clear();
		
		if (_party != null) {
			_party.setDimensionalRift(null);
		}
		
		_party = null;
		_playerInside = null;
		_revivedInWaitingRoom = null;
		_deadPlayers = null;
		
		if (earthQuakeTask != null) {
			earthQuakeTask.cancel(false);
			earthQuakeTask = null;
		}
		
		DimensionalRiftManager.getInstance().getRoom(_type, _chosenRoom).unspawn().setParty(null);
		DimensionalRiftManager.getInstance().killRift(this);
	}
	
	public Timer getTeleportTimer() {
		return teleporterTimer;
	}
	
	public TimerTask getTeleportTimerTask() {
		return teleporterTimerTask;
	}
	
	public Timer getSpawnTimer() {
		return spawnTimer;
	}
	
	public TimerTask getSpawnTimerTask() {
		return spawnTimerTask;
	}
	
	public void setTeleportTimer(Timer t) {
		teleporterTimer = t;
	}
	
	public void setTeleportTimerTask(TimerTask tt) {
		teleporterTimerTask = tt;
	}
	
	public void setSpawnTimer(Timer t) {
		spawnTimer = t;
	}
	
	public void setSpawnTimerTask(TimerTask st) {
		spawnTimerTask = st;
	}
	
	private long calcTimeToNextJump() {
		final int time = Rnd.get((int) general().getAutoJumpsDelayMin(), (int) general().getAutoJumpsDelayMax());
		if (isBossRoom) {
			return (long) (time * general().getBossRoomTimeMultiply());
		}
		return time;
	}
	
	public void memberDead(L2PcInstance player) {
		if (!_deadPlayers.contains(player)) {
			_deadPlayers.add(player);
		}
	}
	
	public void memberResurrected(L2PcInstance player) {
		_deadPlayers.remove(player);
	}
	
	public List<L2PcInstance> getDeadMemberList() {
		return _deadPlayers;
	}
	
	public List<L2PcInstance> getRevivedAtWaitingRoom() {
		return _revivedInWaitingRoom;
	}
	
	public void checkBossRoom(byte room) {
		isBossRoom = DimensionalRiftManager.getInstance().getRoom(_type, room).isBossRoom();
	}
	
	public Location getRoomCoord(byte room) {
		return DimensionalRiftManager.getInstance().getRoom(_type, room).getTeleportCoordinates();
	}
	
	public int getMaxJumps() {
		if ((general().getMaxRiftJumps() <= 8) && (general().getMaxRiftJumps() >= 1)) {
			return general().getMaxRiftJumps();
		}
		
		return 4;
	}
	
	private void teleportToRoom(L2PcInstance player, byte room) {
		Location coords = getRoomCoord(room);
		player.teleToLocation(coords.getX() + Rnd.get(-150, 150), coords.getY() + Rnd.get(-150, 150), coords.getZ());
	}
}
