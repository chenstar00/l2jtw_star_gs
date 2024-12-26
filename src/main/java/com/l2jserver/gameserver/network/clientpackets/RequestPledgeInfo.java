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

import static com.l2jserver.gameserver.config.Configuration.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.PledgeInfo;

public final class RequestPledgeInfo extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestPledgeInfo.class);
	
	private static final String _C__65_REQUESTPLEDGEINFO = "[C] 65 RequestPledgeInfo";
	
	private int _clanId;
	
	@Override
	protected void readImpl() {
		_clanId = readD();
	}
	
	@Override
	protected void runImpl() {
		if (general().debug()) {
			LOG.debug("Info for clan {} requested", _clanId);
		}
		
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		final L2Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null) {
			if (general().debug()) {
				LOG.warn("Clan data for clanId {} is missing for player {}", _clanId, activeChar);
			}
			return; // we have no clan data ?!? should not happen
		}
		
		activeChar.sendPacket(new PledgeInfo(clan));
	}
	
	@Override
	public String getType() {
		return _C__65_REQUESTPLEDGEINFO;
	}
	
	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
