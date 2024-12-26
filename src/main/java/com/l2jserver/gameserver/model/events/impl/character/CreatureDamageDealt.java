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
package com.l2jserver.gameserver.model.events.impl.character;

import static com.l2jserver.gameserver.model.events.EventType.CREATURE_DAMAGE_DEALT;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.BaseEvent;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Creature Damage Dealt event.
 * @author UnAfraid
 * @author Zoey76
 */
public record CreatureDamageDealt(L2Character attacker, L2Character target, double damage, Skill skill, boolean critical, boolean damageOverTime, boolean isReflect) implements BaseEvent {
	@Override
	public EventType getType() {
		return CREATURE_DAMAGE_DEALT;
	}
}
