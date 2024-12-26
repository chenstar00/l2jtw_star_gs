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
package com.l2jserver.gameserver.model.skills;

import static com.l2jserver.gameserver.config.Configuration.server;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.skills.targets.TargetType;

/**
 * Skill unit tests
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class SkillTest {
	
	@Mock(strictness = Mock.Strictness.LENIENT)
	private StatsSet statsSet;
	
	@BeforeAll
	static void init() {
		server().setProperty("DatapackRoot", "src/test/resources");
	}
	
	@Test
	void testSkillWithPositiveEffectPointShouldNotBeBad() {
		when(statsSet.getInt("effectPoint", 0)).thenReturn(10);
		
		final var skill = new Skill(statsSet);
		
		assertThat(skill.isBad()).isFalse();
	}
	
	@Test
	void testSkillWithNegativeEffectPointTargetedOnSelfShouldNotBeBad() {
		when(statsSet.getEnum("targetType", TargetType.class, TargetType.SELF)).thenReturn(TargetType.SELF);
		when(statsSet.getInt("effectPoint", 0)).thenReturn(-10);
		
		final var skill = new Skill(statsSet);
		
		assertThat(skill.isBad()).isFalse();
	}
	
	@Test
	void testSkillWithNegativeEffectPointTargetedOnSummonShouldNotBeBad() {
		when(statsSet.getEnum("targetType", TargetType.class, TargetType.SELF)).thenReturn(TargetType.SUMMON);
		when(statsSet.getInt("effectPoint", 0)).thenReturn(-10);
		
		final var skill = new Skill(statsSet);
		
		assertThat(skill.isBad()).isFalse();
	}
	
	@Test
	void testSkillWithNegativeEffectPointNotTargetedOnSelfShouldBeBad() {
		when(statsSet.getEnum("targetType", TargetType.class, TargetType.SELF)).thenReturn(TargetType.ENEMY_ONLY);
		when(statsSet.getInt("effectPoint", 0)).thenReturn(-10);
		
		final var skill = new Skill(statsSet);
		
		assertThat(skill.isBad()).isTrue();
	}
}