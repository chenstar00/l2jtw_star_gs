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
package com.l2jserver.gameserver.model.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.drops.IDropItem;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;

/**
 * Abstract Script test.
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class AbstractScriptTest {
	
	@Mock
	private L2PcInstance player;
	@Mock
	private PcInventory inventory;
	@Mock
	private L2ItemInstance item;
	@Mock
	private L2Npc npc;
	@Mock
	private IDropItem dropItem;
	
	@Test
	void shouldReturnTrueIfQuestItemsAtLimit() {
		QuestItemChanceHolder questItem = new QuestItemChanceHolder(1, 10L);
		
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(1), anyInt())).thenReturn(10L);
		
		boolean result = AbstractScript.hasItemsAtLimit(player, questItem);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void shouldReturnFalseIfQuestItemsNotAtLimit() {
		QuestItemChanceHolder questItem = new QuestItemChanceHolder(1, 10L);
		
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(1), anyInt())).thenReturn(5L);
		
		boolean result = AbstractScript.hasItemsAtLimit(player, questItem);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldReturnFalseIfDropItemIsNull() {
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, null, 0L, true);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldReturnFalseIfCalculateDropsResultIsNull() {
		when(dropItem.calculateDrops(any(), any())).thenReturn(null);
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 0L, true);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldReturnFalseIfCalculateDropsResultIsEmpty() {
		when(dropItem.calculateDrops(any(), any())).thenReturn(Collections.emptyList());
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 0L, true);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldReturnFalseIfPlayerHasNoInventoryCapacity() {
		int itemId = 1;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, 10L)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(5L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(false);
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 10L, true);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldAddItemsToPlayer() {
		int itemId = 1;
		long amount = 10L;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, amount)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(0L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(true);
		
		AbstractScript.giveItemRandomly(player, npc, player, dropItem, 0L, true);
		
		verify(player).addItem(any(), eq(itemId), eq(amount), any(), anyBoolean());
	}
	
	@Test
	void shouldCapItemsAmountAtLimit() {
		int itemId = 1;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, 10L)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(5L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(true);
		
		AbstractScript.giveItemRandomly(player, npc, player, dropItem, 10L, true);
		
		verify(player).addItem(any(), eq(itemId), eq(5L), any(), anyBoolean());
	}
	
	@Test
	void shouldReturnTrueIfItemsGivenAndLimitReached() {
		int itemId = 1;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, 10L)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(5L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(true);
		when(player.addItem(any(), anyInt(), anyLong(), any(), anyBoolean())).thenReturn(item);
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 10L, true);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void shouldReturnFalseIfItemsGivenAndLimitNotReached() {
		int itemId = 1;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, 10L)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(5L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(true);
		when(player.addItem(any(), anyInt(), anyLong(), any(), anyBoolean())).thenReturn(item);
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 100L, true);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void shouldReturnTrueIfItemsGivenAndNoLimit() {
		int itemId = 1;
		
		when(dropItem.calculateDrops(any(), any())).thenReturn(List.of(new ItemHolder(itemId, 10L)));
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getInventoryItemCount(eq(itemId), anyInt())).thenReturn(5L);
		when(inventory.validateCapacityByItemId(itemId)).thenReturn(true);
		when(player.addItem(any(), anyInt(), anyLong(), any(), anyBoolean())).thenReturn(item);
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, player, dropItem, 0L, true);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void shouldReturnFalseIfDropInfoIsNull() {
		QuestDropInfo dropInfo = null;
		
		boolean result = AbstractScript.giveItemRandomly(player, npc, dropInfo, true);
		
		assertThat(result).isFalse();
	}
}
