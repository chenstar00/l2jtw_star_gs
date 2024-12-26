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

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * Trigger Attack Type.
 * @author Zoey76
 * @version 2.6.3.0
 */
public enum TriggerAttackType {
	NONE {
		@Override
		public boolean check(L2Character trigger, L2Object target) {
			return false;
		}
	},
	ENEMY_ALL {
		@Override
		public boolean check(L2Character trigger, L2Object target) {
			return target.isCharacter() && target.isAutoAttackable(trigger);
		}
	},
	MOB {
		@Override
		public boolean check(L2Character trigger, L2Object target) {
			return target.isAttackable();
		}
	},
	PK {
		@Override
		public boolean check(L2Character trigger, L2Object target) {
			return target.isPlayer() || target.isSummon();
		}
	};
	
	public abstract boolean check(L2Character trigger, L2Object target);
}
