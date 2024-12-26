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
package com.l2jserver.gameserver.network.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.data.xml.impl.HennaData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.L2Henna;
import com.l2jserver.gameserver.network.serverpackets.HennaItemDrawInfo;

/**
 * @author Zoey76
 */
public final class RequestHennaItemInfo extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestHennaItemInfo.class);
	
	private static final String _C__C4_REQUESTHENNAITEMINFO = "[C] C4 RequestHennaItemInfo";
	
	private int _symbolId;
	
	@Override
	protected void readImpl() {
		_symbolId = readD();
	}
	
	@Override
	protected void runImpl() {
		final L2PcInstance activeChar = getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		final L2Henna henna = HennaData.getInstance().getHenna(_symbolId);
		if (henna == null) {
			if (_symbolId != 0) {
				LOG.warn("Invalid Henna Id: {} from player {}", _symbolId, activeChar);
			}
			sendActionFailed();
			return;
		}
		activeChar.sendPacket(new HennaItemDrawInfo(henna, activeChar));
	}
	
	@Override
	public String getType() {
		return _C__C4_REQUESTHENNAITEMINFO;
	}
}