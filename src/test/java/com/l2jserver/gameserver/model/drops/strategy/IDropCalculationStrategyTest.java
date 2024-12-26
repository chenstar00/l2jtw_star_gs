package com.l2jserver.gameserver.model.drops.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.drops.GeneralDropItem;

/**
 * IDropCalculationStrategy test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
class IDropCalculationStrategyTest {
	
	private static final int ITEM_ID = 51424;
	
	@Mock
	private GeneralDropItem item;
	
	@Mock
	private L2Character victim;
	
	@Mock
	private L2Character killer;
	
	@Test
	void testDefaultStrategy() {
		when(item.getItemId()).thenReturn(ITEM_ID);
		when(item.getChance(victim, killer)).thenReturn(150d);
		when(item.isPreciseCalculated()).thenReturn(true);
		try (var rnd = mockStatic(Rnd.class)) {
			rnd.when(Rnd::nextDouble).thenReturn(0.1d);
			rnd.when(() -> Rnd.get(anyLong(), anyLong())).thenReturn(1L);
			final var drops = IDropCalculationStrategy.DEFAULT_STRATEGY.calculateDrops(item, victim, killer);
			assertEquals(ITEM_ID, drops.get(0).getId());
			assertEquals(2, drops.get(0).getCount());
		}
	}
}
