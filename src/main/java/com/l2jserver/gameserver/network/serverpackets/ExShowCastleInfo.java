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
package com.l2jserver.gameserver.network.serverpackets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.entity.Castle;

/**
 * @author KenM
 */
public class ExShowCastleInfo extends L2GameServerPacket {
	private static final Logger LOG = LoggerFactory.getLogger(ExShowCastleInfo.class);
	
	@Override
	protected void writeImpl() {
		writeC(0xFE);
		writeH(0x14);
		List<Castle> castles = CastleManager.getInstance().getCastles();
		writeD(castles.size());
		for (Castle castle : castles) {
			writeD(castle.getResidenceId());
			if (castle.getOwnerId() > 0) {
				if (ClanTable.getInstance().getClan(castle.getOwnerId()) != null) {
					writeS(ClanTable.getInstance().getClan(castle.getOwnerId()).getName());
				} else {
					LOG.warn("Castle owner with no name! Castle: {} has an OwnerId = {} who does not have a  name!", castle.getName(), castle.getOwnerId());
					writeS("");
				}
			} else {
				writeS("");
			}
			writeD(castle.getTaxPercent());
			writeD((int) (castle.getSiege().getSiegeDate().getTimeInMillis() / 1000));
		}
	}
	
}
