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
package com.l2jserver.gameserver.model.events.listeners;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenersContainer;
import com.l2jserver.gameserver.model.events.impl.BaseEvent;
import com.l2jserver.gameserver.model.events.returns.AbstractEventReturn;

/**
 * Annotation event listener provides dynamically attached callback to any method operation with or without any return object.
 * @author UnAfraid
 */
public class AnnotationEventListener extends AbstractEventListener {
	private static final Logger LOG = LoggerFactory.getLogger(AnnotationEventListener.class);
	
	private final Method _callback;
	
	public AnnotationEventListener(ListenersContainer container, EventType type, Method callback, Object owner, int priority) {
		super(container, type, owner);
		_callback = callback;
		setPriority(priority);
	}
	
	@Override
	public <R extends AbstractEventReturn> R executeEvent(BaseEvent event, Class<R> returnBackClass) {
		try {
			final Object result = _callback.invoke(getOwner(), event);
			if (_callback.getReturnType() == returnBackClass) {
				return returnBackClass.cast(result);
			}
		} catch (Exception e) {
			LOG.warn("Error while invoking {} on {}", _callback.getName(), getOwner(), e);
		}
		return null;
	}
}
