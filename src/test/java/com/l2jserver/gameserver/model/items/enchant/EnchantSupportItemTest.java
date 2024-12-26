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
package com.l2jserver.gameserver.model.items.enchant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.CrystalType;
import com.l2jserver.gameserver.model.items.type.EtcItemType;
import com.l2jserver.gameserver.model.items.type.ItemType2;

/**
 * EnchantSupportItem unit tests
 * @author Kita (Noé Caratini)
 */
@ExtendWith(MockitoExtension.class)
class EnchantSupportItemTest {
	
	@Mock
	private ItemTable itemTable;
	
	@Mock
	private StatsSet statsSet;
	@Mock
	private L2Item supportItem;
	
	@Mock
	private L2ItemInstance itemToEnchant;
	@Mock
	private L2Item itemTemplate;
	
	private static MockedStatic<ItemTable> mockItemTable;
	
	@BeforeAll
	static void init() {
		mockItemTable = mockStatic(ItemTable.class);
	}
	
	@AfterAll
	static void tearDown() {
		mockItemTable.close();
	}
	
	@BeforeEach
	void setUp() {
		when(ItemTable.getInstance()).thenReturn(itemTable);
		
		final var itemId = 1;
		when(statsSet.getInt("id")).thenReturn(itemId);
		when(itemTable.getTemplate(itemId)).thenReturn(supportItem);
		when(supportItem.getItemType()).thenReturn(EtcItemType.SCRL_INC_ENCHANT_PROP_AM);
	}
	
	@Test
	void testSupportItemIsNotValidForNullItem() {
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(null, support);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void testSupportItemIsNotValidForNonEnchantableItem() {
		when(itemToEnchant.isEnchantable()).thenReturn(0);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void testSupportItemIsNotValidForInvalidItemType() {
		when(itemToEnchant.isEnchantable()).thenReturn(1);
		when(itemToEnchant.getItem()).thenReturn(itemTemplate);
		when(itemTemplate.getType2()).thenReturn(ItemType2.WEAPON);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void testSupportItemIsNotValidForItemWithEnchantLevelHigherThanMaxEnchant() {
		when(statsSet.getInt(eq("maxEnchant"), anyInt())).thenReturn(9);
		
		when(itemToEnchant.isEnchantable()).thenReturn(1);
		when(itemToEnchant.getItem()).thenReturn(itemTemplate);
		when(itemTemplate.getType2()).thenReturn(ItemType2.SHIELD_ARMOR);
		when(itemToEnchant.getEnchantLevel()).thenReturn(10);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void testSupportItemIsNotValidForItemNotMatchingGrade() {
		when(statsSet.getEnum("targetGrade", CrystalType.class, CrystalType.NONE)).thenReturn(CrystalType.A);
		
		when(itemToEnchant.isEnchantable()).thenReturn(1);
		when(itemToEnchant.getItem()).thenReturn(itemTemplate);
		when(itemTemplate.getType2()).thenReturn(ItemType2.SHIELD_ARMOR);
		when(itemTemplate.getItemGradeSPlus()).thenReturn(CrystalType.S);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isFalse();
	}
	
	@Test
	void testSupportItemIsValidForItemWithEnchantLevelEqualToMaxEnchant() {
		when(statsSet.getInt(eq("maxEnchant"), anyInt())).thenReturn(9);
		
		when(itemToEnchant.isEnchantable()).thenReturn(1);
		when(itemToEnchant.getItem()).thenReturn(itemTemplate);
		when(itemTemplate.getType2()).thenReturn(ItemType2.SHIELD_ARMOR);
		when(itemToEnchant.getEnchantLevel()).thenReturn(9);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void testSupportItemIsValidForItemWithEnchantLevelLowerThanMaxEnchant() {
		when(statsSet.getInt(eq("maxEnchant"), anyInt())).thenReturn(9);
		
		when(itemToEnchant.isEnchantable()).thenReturn(1);
		when(itemToEnchant.getItem()).thenReturn(itemTemplate);
		when(itemTemplate.getType2()).thenReturn(ItemType2.SHIELD_ARMOR);
		when(itemToEnchant.getEnchantLevel()).thenReturn(1);
		
		final var support = new EnchantSupportItem(statsSet);
		final var result = support.isValid(itemToEnchant, support);
		
		assertThat(result).isTrue();
	}
}