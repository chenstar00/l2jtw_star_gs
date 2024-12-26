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
package com.l2jserver.gameserver.model.itemcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.enums.ItemLocation;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author DS
 */
public class PcRefund extends ItemContainer {
	private static final Logger LOG = LoggerFactory.getLogger(PcRefund.class);
	
	private final L2PcInstance _owner;
	
	public PcRefund(L2PcInstance owner) {
		_owner = owner;
	}
	
	@Override
	public String getName() {
		return "Refund";
	}
	
	@Override
	public L2PcInstance getOwner() {
		return _owner;
	}
	
	@Override
	public ItemLocation getBaseLocation() {
		return ItemLocation.REFUND;
	}
	
	@Override
	protected void addItem(L2ItemInstance item) {
		super.addItem(item);
		try {
			if (getSize() > 12) {
				L2ItemInstance removedItem = _items.removeFirst();
				if (removedItem != null) {
					ItemTable.getInstance().destroyItem("ClearRefund", removedItem, getOwner(), null);
					removedItem.updateDatabase(true);
				}
			}
		} catch (Exception e) {
			LOG.error("addItem()", e);
		}
	}
	
	@Override
	public void refreshWeight() {
	}
	
	@Override
	public void deleteMe() {
		try {
			for (L2ItemInstance item : _items) {
				if (item != null) {
					ItemTable.getInstance().destroyItem("ClearRefund", item, getOwner(), null);
					item.updateDatabase(true);
				}
			}
		} catch (Exception e) {
			LOG.error("deleteMe()", e);
		}
		_items.clear();
	}
	
	@Override
	public void restore() {
	}
}