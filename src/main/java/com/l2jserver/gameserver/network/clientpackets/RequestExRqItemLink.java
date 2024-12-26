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

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.ExRpItemLink;

/**
 * @author KenM
 */
public class RequestExRqItemLink extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestExRqItemLink.class);
	
	private static final String _C__D0_1E_REQUESTEXRQITEMLINK = "[C] D0:1E RequestExRqItemLink";
	private int _objectId;
	
	@Override
	protected void readImpl() {
		_objectId = readD();
	}
	
	@Override
	protected void runImpl() {
		final var client = getClient();
		if (client == null) {
			return;
		}
		
		final var object = L2World.getInstance().findObject(_objectId);
		if (!(object instanceof L2ItemInstance item)) {
			return;
		}
		
		if (item.isPublished()) {
			client.sendPacket(new ExRpItemLink(item));
		} else if (general().debug()) {
			LOG.info("{} requested item link for item which wasn't published! ID: {}", getClient(), _objectId);
		}
	}
	
	@Override
	public String getType() {
		return _C__D0_1E_REQUESTEXRQITEMLINK;
	}
}
