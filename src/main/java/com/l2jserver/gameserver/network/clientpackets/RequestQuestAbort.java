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

import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.serverpackets.QuestList;

/**
 * @since 2005/03/27 15:29:30
 */
public final class RequestQuestAbort extends L2GameClientPacket {
	private static final Logger LOG = LoggerFactory.getLogger(RequestQuestAbort.class);
	
	private static final String _C__63_REQUESTQUESTABORT = "[C] 63 RequestQuestAbort";
	
	private int _questId;
	
	@Override
	protected void readImpl() {
		_questId = readD();
	}
	
	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		Quest qe = QuestManager.getInstance().getQuest(_questId);
		if (qe != null) {
			QuestState qs = activeChar.getQuestState(qe.getName());
			if (qs != null) {
				qs.exitQuest(true);
				activeChar.sendPacket(new QuestList());
			} else {
				if (general().debug()) {
					LOG.info("Player '{}' try to abort quest {} but he didn't have it started.", activeChar.getName(), qe.getName());
				}
			}
		} else {
			if (general().debug()) {
				LOG.warn("Quest (id='{}') not found.", _questId);
			}
		}
	}
	
	@Override
	public String getType() {
		return _C__63_REQUESTQUESTABORT;
	}
}