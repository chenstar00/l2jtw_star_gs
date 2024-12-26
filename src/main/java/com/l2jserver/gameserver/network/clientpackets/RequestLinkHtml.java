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

import static com.l2jserver.gameserver.model.actor.L2Npc.INTERACTION_DISTANCE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * RequestLinkHtml client packet implementation.
 * @author zabbix
 * @author HorridoJoho
 */
public final class RequestLinkHtml extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestLinkHtml.class);
	
	private static final String _C__22_REQUESTLINKHTML = "[C] 22 RequestLinkHtml";
	private String _link;
	
	@Override
	protected void readImpl() {
		_link = readS();
	}
	
	@Override
	public void runImpl() {
		final var actor = getClient().getActiveChar();
		if (actor == null) {
			return;
		}
		
		if (_link.isEmpty()) {
			LOG.warn("Player {} sent empty html link!", actor.getName());
			return;
		}
		
		if (_link.contains("..")) {
			LOG.warn("Player {} sent invalid html link: link {}", actor.getName(), _link);
			return;
		}
		
		int htmlObjectId = actor.validateHtmlAction("link " + _link);
		if (htmlObjectId == -1) {
			LOG.warn("Player {} sent non cached  html link: link {}", actor.getName(), _link);
			return;
		}
		
		if ((htmlObjectId > 0) && !Util.isInsideRangeOfObjectId(actor, htmlObjectId, INTERACTION_DISTANCE)) {
			// No logging here, this could be a common case
			return;
		}
		
		final var filename = "data/html/" + _link;
		final var msg = new NpcHtmlMessage(htmlObjectId);
		msg.setFile(actor.getHtmlPrefix(), filename);
		msg.replace("%objectId%", String.valueOf(htmlObjectId));
		sendPacket(msg);
	}
	
	@Override
	public String getType() {
		return _C__22_REQUESTLINKHTML;
	}
}
