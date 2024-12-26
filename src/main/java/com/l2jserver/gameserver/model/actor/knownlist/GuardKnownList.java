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
package com.l2jserver.gameserver.model.actor.knownlist;

import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.npc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2GuardInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;

public class GuardKnownList extends AttackableKnownList {
	private static final Logger LOG = LoggerFactory.getLogger(GuardKnownList.class);
	
	public GuardKnownList(L2GuardInstance activeChar) {
		super(activeChar);
	}
	
	@Override
	public boolean addKnownObject(L2Object object) {
		if (!super.addKnownObject(object)) {
			return false;
		}
		
		if (object.isPlayer()) {
			// Check if the object added is a L2PcInstance that owns Karma
			if (object.getActingPlayer().getKarma() > 0) {
				if (general().debug()) {
					LOG.debug("{}: PK {} entered scan range", getActiveChar().getObjectId(), object.getObjectId());
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE) {
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
		} else if ((npc().guardAttackAggroMob() && getActiveChar().isInActiveRegion()) && object.isMonster()) {
			// Check if the object added is an aggressive L2MonsterInstance
			if (((L2MonsterInstance) object).isAggressive()) {
				if (general().debug()) {
					LOG.debug("{}: Aggressive mob {} entered scan range", getActiveChar().getObjectId(), object.getObjectId());
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE) {
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
		}
		
		return true;
	}
	
	@Override
	protected boolean removeKnownObject(L2Object object, boolean forget) {
		if (!super.removeKnownObject(object, forget)) {
			return false;
		}
		
		// Check if the aggression list of this guard is empty.
		if (getActiveChar().getAggroList().isEmpty()) {
			// Set the L2GuardInstance to AI_INTENTION_IDLE
			if (getActiveChar().hasAI() && !getActiveChar().isWalker()) {
				getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
			}
		}
		
		return true;
	}
	
	@Override
	public final L2GuardInstance getActiveChar() {
		return (L2GuardInstance) super.getActiveChar();
	}
}
