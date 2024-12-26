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

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.LinkedList;
import java.util.List;

import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.model.skills.CommonSkill;

/**
 * Acquire Skill Info server packet implementation.
 * @author Zoey76
 */
public class AcquireSkillInfo extends L2GameServerPacket {
	private final AcquireSkillType type;
	private final int id;
	private final int level;
	private final int cost;
	private final List<Requirements> requirements;
	
	private record Requirements(int type, int id, long count, int unk) {
	}
	
	/**
	 * Constructor for the acquire skill info object.
	 * @param player the player
	 * @param skillType the skill learning type
	 * @param skillLearn the skill to learn
	 */
	public AcquireSkillInfo(L2PcInstance player, AcquireSkillType skillType, L2SkillLearn skillLearn) {
		id = skillLearn.getSkillId();
		level = skillLearn.getSkillLevel();
		cost = skillLearn.getCalculatedLevelUpSp(player.getClassId(), player.getLearningClass());
		type = skillType;
		requirements = new LinkedList<>();
		
		if ((skillType != AcquireSkillType.PLEDGE) || character().lifeCrystalNeeded()) {
			for (var item : skillLearn.getRequiredItems()) {
				if (!character().divineInspirationSpBookNeeded() && (id == CommonSkill.DIVINE_INSPIRATION.getId())) {
					continue;
				}
				requirements.add(new Requirements(99, item.getId(), item.getCount(), 50));
			}
		}
	}
	
	@Override
	protected final void writeImpl() {
		writeC(0x91);
		writeD(id);
		writeD(level);
		writeD(cost);
		writeD(type.ordinal());
		writeD(requirements.size());
		for (var requirement : requirements) {
			writeD(requirement.type);
			writeD(requirement.id);
			writeQ(requirement.count);
			writeD(requirement.unk);
		}
	}
}