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
package com.l2jserver.gameserver.model.skills.targets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2ChestInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2TrapInstance;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Target Type test.
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class TargetTypeTest {
	
	@Mock
	private Skill skill;
	@Mock
	private L2Character caster;
	@Mock
	private L2Object target;
	
	@Test
	void doorTreasureShouldReturnNullIfTargetIsNull() {
		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, null);
		
		assertNull(result);
	}
	
	@Test
	void doorTreasureShouldReturnNullIfTargetIsNotADoorOrChest() {
		when(target.isDoor()).thenReturn(false);
		
		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);
		
		assertNull(result);
	}
	
	@Test
	void doorTreasureShouldReturnTargetIfDoor() {
		when(target.isDoor()).thenReturn(true);
		
		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);
		
		assertEquals(target, result);
	}
	
	@Test
	void doorTreasureShouldReturnTargetIfChest() {
		final var target = mock(L2ChestInstance.class);
		final var result = TargetType.DOOR_TREASURE.getTarget(skill, caster, target);
		
		assertEquals(target, result);
	}
	
	@Test
	void testMonstersCanUseEnemyOnlySkillsOnPc() {
		final var caster = mock(L2MonsterInstance.class);
		when(caster.getObjectId()).thenReturn(1);
		when(caster.isPlayable()).thenReturn(false);
		
		final var target = mock(L2PcInstance.class);
		when(target.isCharacter()).thenReturn(true);
		when(target.isDead()).thenReturn(false);
		when(target.getObjectId()).thenReturn(2);
		when(target.isAutoAttackable(any())).thenReturn(true);
		
		final var result = TargetType.ENEMY_ONLY.getTarget(skill, caster, target);
		
		assertEquals(target, result);
	}
	
	@Test
	void testPvpChecksReachedForEnemyOnlySkills() {
		final var caster = mock(L2PcInstance.class);
		when(caster.getObjectId()).thenReturn(1);
		when(caster.isPlayable()).thenReturn(true);
		when(caster.getActingPlayer()).thenReturn(caster);
		when(caster.isInOlympiadMode()).thenReturn(true);
		
		final var target = mock(L2PcInstance.class);
		when(target.isCharacter()).thenReturn(true);
		when(target.isDead()).thenReturn(false);
		when(target.getObjectId()).thenReturn(2);
		when(target.isAutoAttackable(any())).thenReturn(true);
		
		final var result = TargetType.ENEMY_ONLY.getTarget(skill, caster, target);
		
		assertNull(result);
	}
	
	@Test
	void testEnemyTypeShouldTargetAttackableTraps() {
		when(caster.getObjectId()).thenReturn(1);
		
		final var trap = mock(L2TrapInstance.class);
		when(trap.getObjectId()).thenReturn(2);
		when(trap.isCharacter()).thenReturn(true);
		when(trap.isNpc()).thenReturn(true);
		when(trap.isAutoAttackable(caster)).thenReturn(true);
		
		final var result = TargetType.ENEMY.getTarget(skill, caster, trap);
		
		assertThat(result).isEqualTo(trap);
	}
	
	@Test
	void testEnemyTypeShouldNotTargetNonAttackableTraps() {
		when(caster.getObjectId()).thenReturn(1);
		
		final var trap = mock(L2TrapInstance.class);
		when(trap.getObjectId()).thenReturn(2);
		when(trap.isCharacter()).thenReturn(true);
		when(trap.isNpc()).thenReturn(true);
		when(trap.isAutoAttackable(caster)).thenReturn(false);
		
		final var result = TargetType.ENEMY.getTarget(skill, caster, trap);
		
		assertThat(result).isNull();
	}
}
