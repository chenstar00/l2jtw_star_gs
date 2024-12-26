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
package com.l2jserver.gameserver.model.quest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Quest test.
 * @author Kita (Noé Caratini)
 */
class QuestTest {
	
	private Quest quest;
	
	@BeforeEach
	void setUp() {
		quest = new Quest(1, "Test quest", "A test quest");
	}
	
	@Test
	void shouldRegisterQuestItems() {
		quest.registerQuestItems(1, 2);
		
		assertThat(quest.getRegisteredItemIds()).containsExactlyInAnyOrder(1, 2);
	}
	
	@Test
	void shouldRegisterQuestItemsWithSet() {
		quest.registerQuestItems(Set.of(1, 2));
		
		assertThat(quest.getRegisteredItemIds()).containsExactlyInAnyOrder(1, 2);
	}
	
	@Test
	void shouldAddToRegisteredQuestItemsIfCalledMultipleTimes() {
		quest.registerQuestItems(1, 2);
		quest.registerQuestItems(3, 4);
		quest.registerQuestItems(Set.of(5, 6));
		
		assertThat(quest.getRegisteredItemIds()).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
	}
}