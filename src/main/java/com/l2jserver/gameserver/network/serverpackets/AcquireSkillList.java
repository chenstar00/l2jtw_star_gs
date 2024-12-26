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

import static com.l2jserver.gameserver.model.base.AcquireSkillType.SUBPLEDGE;

import java.util.List;

import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.base.AcquireSkillType;

/**
 * Acquire Skill List server packet implementation.
 * @author Zoey76
 */
public final class AcquireSkillList extends L2GameServerPacket {
	private final List<L2SkillLearn> skills;
	private final AcquireSkillType skillType;
	
	public AcquireSkillList(AcquireSkillType type, List<L2SkillLearn> skills) {
		this.skillType = type;
		this.skills = skills;
	}
	
	@Override
	protected void writeImpl() {
		if (skills.isEmpty()) {
			return;
		}
		writeC(0x90);
		writeD(skillType.ordinal());
		writeD(skills.size());
		for (var skill : skills) {
			writeD(skill.getSkillId());
			writeD(skill.getSkillLevel());
			writeD(skill.getSkillLevel());
			writeD(skill.getLevelUpSp());
			writeD(skill.getRequiredItems().isEmpty() ? 0x00 : 0x01);
			if (skillType == SUBPLEDGE) {
				writeD(0); // TODO: ?
			}
		}
	}
}