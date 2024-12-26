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
package com.l2jserver.gameserver.enums;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * Trigger Target Type.
 * @author Zoey76
 * @version 2.6.3.0
 */
public enum TriggerTargetType {
	ENEMY {
		@Override
		public List<L2Character> getTargets(L2Character target, L2Character attacker) {
			return List.of(attacker);
		}
	},
	SELF {
		@Override
		public List<L2Character> getTargets(L2Character target, L2Character attacker) {
			return List.of(target);
		}
	},
	MY_PARTY {
		@Override
		public List<L2Character> getTargets(L2Character target, L2Character attacker) {
			if (target.isPlayer() || target.isSummon()) {
				final var player = target.getActingPlayer();
				if (player.isInParty()) {
					return new ArrayList<>(player.getParty().getMembers());
				}
				return List.of(player);
			}
			return List.of();
		}
	},
	TARGET {
		@Override
		public List<L2Character> getTargets(L2Character target, L2Character attacker) {
			return List.of(target);
		}
	},
	SUMMON {
		@Override
		public List<L2Character> getTargets(L2Character target, L2Character attacker) {
			if (target.hasSummon()) {
				return List.of(target.getSummon());
			}
			return List.of();
		}
	};
	
	public abstract List<L2Character> getTargets(L2Character target, L2Character attacker);
}
