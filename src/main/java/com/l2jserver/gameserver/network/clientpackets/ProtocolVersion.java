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
import static com.l2jserver.gameserver.config.Configuration.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.network.serverpackets.KeyPacket;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @since 2005/04/02 10:43:04
 */
public final class ProtocolVersion extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(ProtocolVersion.class);
	private static final Logger LOG_ACCOUNTING = LoggerFactory.getLogger("accounting");
	
	private static final String _C__0E_PROTOCOLVERSION = "[C] 0E ProtocolVersion";
	
	private int _version;
	
	@Override
	protected void readImpl() {
		_version = readD();
	}
	
	@Override
	protected void runImpl() {
		// this packet is never encrypted
		if (_version == -2) {
			if (general().debug()) {
				LOG.info("Ping received");
			}
			// this is just a ping attempt from the new C2 client
			getClient().close((L2GameServerPacket) null);
		} else if (!server().getAllowedProtocolRevisions().contains(_version)) {
			LOG_ACCOUNTING.warn("Wrong protocol, {}, {}", _version, getClient());
			KeyPacket pk = new KeyPacket(getClient().enableCrypt(), 0);
			getClient().setProtocolOk(false);
			getClient().close(pk);
		} else {
			if (general().debug()) {
				LOG.debug("Client Protocol Revision is ok: {}", _version);
			}
			
			KeyPacket pk = new KeyPacket(getClient().enableCrypt(), 1);
			getClient().sendPacket(pk);
			getClient().setProtocolOk(true);
		}
	}
	
	@Override
	public String getType() {
		return _C__0E_PROTOCOLVERSION;
	}
}
