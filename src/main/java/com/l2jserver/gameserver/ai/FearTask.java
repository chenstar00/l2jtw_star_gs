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
package com.l2jserver.gameserver.ai;

import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * Fear task.
 * @author Zoey76
 * @version 2.6.3.0
 */
public class FearTask implements Runnable {
	
	protected static final int FEAR_TICKS = 5;
	
	private final L2AttackableAI ai;
	
	private final L2Character effector;
	
	private boolean start;
	
	public FearTask(L2AttackableAI ai, L2Character effector, boolean start) {
		this.ai = ai;
		this.effector = effector;
		this.start = start;
	}
	
	@Override
	public void run() {
		ai.setFearTime(ai.getFearTime() - FEAR_TICKS);
		ai.onEvtAfraid(effector, start);
		start = false;
	}
}
