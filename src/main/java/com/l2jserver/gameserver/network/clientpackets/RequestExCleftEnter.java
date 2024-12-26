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

/**
 * Format: (ch) chd
 * @author HorridoJoho
 * @since 2.6.3.0
 */
public class RequestExCleftEnter extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestExCleftEnter.class);
	
	private static final String _C__D0_59_REQUESTEXCLEFTENTER = "[C] D0:59 RequestExCleftEnter";
	
	private int _0;
	private int _1;
	private int _2;
	
	@Override
	protected void readImpl() {
		_0 = readC();
		_1 = readH();
		_2 = readD();
	}
	
	@Override
	protected void runImpl() {
		LOG.debug(_C__D0_59_REQUESTEXCLEFTENTER + " - c={}, h={}, d={}", _0, _1, _2);
	}
	
	@Override
	public String getType() {
		return _C__D0_59_REQUESTEXCLEFTENTER;
	}
}
