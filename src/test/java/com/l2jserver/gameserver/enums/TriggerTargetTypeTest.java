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

import static com.l2jserver.gameserver.enums.TriggerTargetType.ENEMY;
import static com.l2jserver.gameserver.enums.TriggerTargetType.MY_PARTY;
import static com.l2jserver.gameserver.enums.TriggerTargetType.SELF;
import static com.l2jserver.gameserver.enums.TriggerTargetType.SUMMON;
import static com.l2jserver.gameserver.enums.TriggerTargetType.TARGET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Trigger Target Type test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
class TriggerTargetTypeTest {
	
	@Mock
	private L2PcInstance target;
	@Mock
	private L2Character attacker;
	@Mock
	private L2PcInstance partyMember;
	@Mock
	private L2Party party;
	@Mock
	private L2Summon summon;
	
	@Test
	void testEnemy() {
		assertEquals(List.of(attacker), ENEMY.getTargets(target, attacker));
	}
	
	@Test
	void testSelf() {
		assertEquals(List.of(target), SELF.getTargets(target, attacker));
	}
	
	@Test
	void testMyPartyTargetIsPlayerInParty() {
		when(target.isPlayer()).thenReturn(true);
		when(target.getActingPlayer()).thenReturn(target);
		when(target.isInParty()).thenReturn(true);
		when(target.getParty()).thenReturn(party);
		when(party.getMembers()).thenReturn(List.of(target, partyMember));
		assertEquals(List.of(target, partyMember), MY_PARTY.getTargets(target, attacker));
	}
	
	@Test
	void testMyPartyTargetIsSummonInParty() {
		when(summon.isPlayer()).thenReturn(false);
		when(summon.isSummon()).thenReturn(true);
		when(summon.getActingPlayer()).thenReturn(target);
		when(target.isInParty()).thenReturn(true);
		when(target.getParty()).thenReturn(party);
		when(party.getMembers()).thenReturn(List.of(target, partyMember));
		assertEquals(List.of(target, partyMember), MY_PARTY.getTargets(summon, attacker));
	}
	
	@Test
	void testMyPartyPlayerIsNotInParty() {
		when(target.isPlayer()).thenReturn(true);
		when(target.getActingPlayer()).thenReturn(target);
		when(target.isInParty()).thenReturn(false);
		assertEquals(List.of(target), MY_PARTY.getTargets(target, attacker));
	}
	
	@Test
	void testMyPartyTargetIsNotPlayerAndIsNotSummon() {
		when(target.isPlayer()).thenReturn(false);
		assertEquals(List.of(), MY_PARTY.getTargets(target, attacker));
	}
	
	@Test
	void testTarget() {
		assertEquals(List.of(target), TARGET.getTargets(target, attacker));
	}
	
	@Test
	void testSummon() {
		when(target.hasSummon()).thenReturn(true);
		when(target.getSummon()).thenReturn(summon);
		assertEquals(List.of(summon), SUMMON.getTargets(target, attacker));
	}
	
	@Test
	void testSummonNoSummon() {
		when(target.hasSummon()).thenReturn(false);
		assertEquals(List.of(), SUMMON.getTargets(target, attacker));
	}
}
