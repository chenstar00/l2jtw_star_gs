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

import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.GMAudit;

/**
 * All GM commands triggered by //command
 * @since 2005/03/27 15:29:29
 */
public final class SendBypassBuildCmd extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(SendBypassBuildCmd.class);
	
	private static final String _C__74_SENDBYPASSBUILDCMD = "[C] 74 SendBypassBuildCmd";
	
	public static final int GM_MESSAGE = 9;
	
	public static final int ANNOUNCEMENT = 10;
	
	private String _command;
	
	@Override
	protected void readImpl() {
		_command = readS().trim();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		String command = "admin_" + _command.split(" ")[0];
		
		IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(command);
		
		if (ach == null) {
			if (activeChar.isGM()) {
				activeChar.sendMessage("The command " + command.substring(6) + " does not exists!");
			}
			
			LOG.warn("No handler registered for admin command '{}'", command);
			return;
		}
		
		if (!AdminData.getInstance().hasAccess(command, activeChar.getAccessLevel())) {
			activeChar.sendMessage("You don't have the access right to use this command!");
			LOG.warn("Character {} tried to use admin command {}, but have no access to it!", activeChar.getName(), command);
			return;
		}
		
		if (general().gmAudit()) {
			GMAudit.auditGMAction(activeChar.getName() + " [" + activeChar.getObjectId() + "]", _command, (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target"));
		}
		
		ach.useAdminCommand("admin_" + _command, activeChar);
	}
	
	@Override
	public String getType() {
		return _C__74_SENDBYPASSBUILDCMD;
	}
}
