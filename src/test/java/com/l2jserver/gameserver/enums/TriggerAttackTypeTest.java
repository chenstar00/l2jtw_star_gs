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

import static com.l2jserver.gameserver.enums.TriggerAttackType.ENEMY_ALL;
import static com.l2jserver.gameserver.enums.TriggerAttackType.MOB;
import static com.l2jserver.gameserver.enums.TriggerAttackType.NONE;
import static com.l2jserver.gameserver.enums.TriggerAttackType.PK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * Trigger Attack Type test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
class TriggerAttackTypeTest {
	@Mock
	private L2Character trigger;
	
	@Mock
	private L2Object target;
	
	@Test
	void testNone() {
		assertFalse(NONE.check(trigger, target));
	}
	
	@Test
	void testEnemyAll() {
		when(target.isCharacter()).thenReturn(true);
		when(target.isAutoAttackable(trigger)).thenReturn(true);
		assertTrue(ENEMY_ALL.check(trigger, target));
	}
	
	@Test
	void testEnemyAllTargetIsNotACharacter() {
		when(target.isCharacter()).thenReturn(false);
		assertFalse(ENEMY_ALL.check(trigger, target));
	}
	
	@Test
	void testEnemyAllTargetIsNotAnEnemy() {
		when(target.isCharacter()).thenReturn(true);
		when(target.isAutoAttackable(trigger)).thenReturn(false);
		assertFalse(ENEMY_ALL.check(trigger, target));
	}
	
	@Test
	void testMobTargetIsMonster() {
		when(target.isAttackable()).thenReturn(true);
		assertTrue(MOB.check(trigger, target));
	}
	
	@Test
	void testPKTargetIsPlayer() {
		when(target.isPlayer()).thenReturn(true);
		assertTrue(PK.check(trigger, target));
	}
	
	@Test
	void testPKTargetIsSummon() {
		when(target.isPlayer()).thenReturn(false);
		when(target.isSummon()).thenReturn(true);
		assertTrue(PK.check(trigger, target));
	}
	
	@Test
	void testPKTargetIsNotPlayerOrSummon() {
		when(target.isPlayer()).thenReturn(false);
		when(target.isSummon()).thenReturn(false);
		assertFalse(PK.check(trigger, target));
	}
}