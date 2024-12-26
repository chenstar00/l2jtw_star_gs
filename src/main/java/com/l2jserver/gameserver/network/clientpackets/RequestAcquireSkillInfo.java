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

import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillInfo;

/**
 * Request Acquire Skill Info client packet implementation.
 * @author Zoey76
 */
public final class RequestAcquireSkillInfo extends L2GameClientPacket {
	
	private static final Logger LOG = LoggerFactory.getLogger(RequestAcquireSkillInfo.class);
	
	private static final String _C__73_REQUESTACQUIRESKILLINFO = "[C] 73 RequestAcquireSkillInfo";
	
	private int _id;
	private int _level;
	private AcquireSkillType _skillType;
	
	@Override
	protected void readImpl() {
		_id = readD();
		_level = readD();
		_skillType = AcquireSkillType.getAcquireSkillType(readD());
	}
	
	@Override
	protected void runImpl() {
		if ((_id <= 0) || (_level <= 0)) {
			LOG.warn("Invalid Id: {} or level: {}!", _id, _level);
			return;
		}
		
		final var activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}
		
		final var trainer = activeChar.getLastFolkNPC();
		if (!(trainer instanceof L2NpcInstance)) {
			return;
		}
		
		if (!trainer.canInteract(activeChar) && !activeChar.isGM()) {
			return;
		}
		
		final var skill = SkillData.getInstance().getSkill(_id, _level);
		if (skill == null) {
			LOG.warn("Skill Id: {} level: {} is undefined!", _id, _level);
			return;
		}
		
		// Hack check. Doesn't apply to all Skill Types
		final int prevSkillLevel = activeChar.getSkillLevel(_id);
		if ((prevSkillLevel > 0) && !((_skillType == AcquireSkillType.TRANSFER) || (_skillType == AcquireSkillType.SUBPLEDGE))) {
			if (prevSkillLevel == _level) {
				LOG.warn("Player {} is requesting info for a skill that already knows, Id: {} level: {}!", activeChar, _id, _level);
			} else if (prevSkillLevel != (_level - 1)) {
				LOG.warn("Player {} is requesting info for skill Id: {} level {} without knowing it's previous level!", activeChar, _id, _level);
			}
		}
		
		if ((_skillType == AcquireSkillType.PLEDGE) && !activeChar.isClanLeader()) {
			return;
		}
		
		if ((_skillType == AcquireSkillType.SUBPLEDGE) && (!activeChar.isClanLeader() || !activeChar.hasClanPrivilege(ClanPrivilege.CL_TROOPS_FAME))) {
			return;
		}
		
		final var skillLearn = SkillTreesData.getInstance().getSkillLearn(_skillType, _id, _level, activeChar);
		if (skillLearn == null) {
			return;
		}
		
		sendPacket(new AcquireSkillInfo(activeChar, _skillType, skillLearn));
	}
	
	@Override
	public String getType() {
		return _C__73_REQUESTACQUIRESKILLINFO;
	}
}
