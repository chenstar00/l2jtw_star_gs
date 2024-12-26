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
package com.l2jserver.gameserver.model.actor.instance;

import static com.l2jserver.gameserver.config.Configuration.server;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.events.EventDispatcher;

/**
 * L2TrapInstance unit tests
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class L2TrapInstanceTest {
	
	private static final int OBJECT_ID = 1;
	
	@Mock
	private EventDispatcher eventDispatcher;
	@Mock
	private L2NpcTemplate template;
	@Mock
	private StatsSet statsSet;
	
	@Mock
	private L2PcInstance owner;
	@Mock
	private L2Character character;
	@Mock
	private L2PcInstance player;
	@Mock
	private L2Party party;
	
	private static MockedStatic<EventDispatcher> mockedEventDispatcher;
	
	@BeforeAll
	static void init() {
		mockedEventDispatcher = mockStatic(EventDispatcher.class);
		
		server().setProperty("DatapackRoot", "src/test/resources");
	}
	
	@AfterAll
	static void after() {
		mockedEventDispatcher.close();
	}
	
	@BeforeEach
	void setUp() {
		when(EventDispatcher.getInstance()).thenReturn(eventDispatcher);
	}
	
	@Test
	void testTrapIsNotVisibleForNullCharacter() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(null)).isFalse();
	}
	
	@Test
	void testTrapIsNotVisibleForOtherCharacter() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(character)).isFalse();
	}
	
	@Test
	void testTrapIsVisibleForCharacterThatDetectedIt() {
		when(template.getParameters()).thenReturn(statsSet);
		when(owner.getPvpFlag()).thenReturn((byte) 1);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		trap.setDetected(character);
		
		assertThat(trap.isVisibleFor(character)).isTrue();
	}
	
	@Test
	void testTrapIsNotVisibleWhenOwnerIsNull() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, 1, 1000);
		
		assertThat(trap.isVisibleFor(character)).isFalse();
	}
	
	@Test
	void testTrapIsVisibleForTheOwner() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(owner)).isTrue();
	}
	
	@Test
	void testTrapIsNotVisibleForPlayerInObserverMode() {
		when(template.getParameters()).thenReturn(statsSet);
		when(player.inObserverMode()).thenReturn(true);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(player)).isFalse();
	}
	
	@Test
	void testTrapIsNotVisibleForOlympiadCompetitor() {
		when(template.getParameters()).thenReturn(statsSet);
		when(owner.isInOlympiadMode()).thenReturn(true);
		when(owner.getOlympiadSide()).thenReturn(0);
		when(player.isInOlympiadMode()).thenReturn(true);
		when(player.getOlympiadSide()).thenReturn(1);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(player)).isFalse();
	}
	
	@Test
	void testTrapIsVisibleForPlayersInOwnersParty() {
		when(template.getParameters()).thenReturn(statsSet);
		when(party.getLeaderObjectId()).thenReturn(10);
		when(owner.isInParty()).thenReturn(true);
		when(owner.getParty()).thenReturn(party);
		when(player.isInParty()).thenReturn(true);
		when(player.getParty()).thenReturn(party);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isVisibleFor(player)).isTrue();
	}
	
	@Test
	void testTrapIsNotAutoAttackableByNullCharacter() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isAutoAttackable(null)).isFalse();
	}
	
	@Test
	void testTrapIsNotAutoAttackableByOwner() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isAutoAttackable(owner)).isFalse();
	}
	
	@Test
	void testTrapIsNotAutoAttackableByCharacterThatHasNotDetectedIt() {
		when(template.getParameters()).thenReturn(statsSet);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isAutoAttackable(character)).isFalse();
	}
	
	@Test
	void testTrapIsAutoAttackableByCharacterThatHasDetectedIt() {
		when(template.getParameters()).thenReturn(statsSet);
		when(owner.getPvpFlag()).thenReturn((byte) 1);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		trap.setDetected(character);
		
		assertThat(trap.isAutoAttackable(character)).isTrue();
	}
	
	@Test
	void testTrapIsNotAutoAttackableForPlayersInOwnersParty() {
		when(template.getParameters()).thenReturn(statsSet);
		when(party.getLeaderObjectId()).thenReturn(10);
		when(owner.isInParty()).thenReturn(true);
		when(owner.getParty()).thenReturn(party);
		when(player.isInParty()).thenReturn(true);
		when(player.getParty()).thenReturn(party);
		
		final var trap = new L2TrapInstance(OBJECT_ID, template, owner, 1000);
		
		assertThat(trap.isAutoAttackable(player)).isFalse();
	}
}
