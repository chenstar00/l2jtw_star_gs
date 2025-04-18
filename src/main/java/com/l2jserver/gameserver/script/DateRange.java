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
package com.l2jserver.gameserver.script;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luis Arias
 */
public class DateRange {
	private static final Logger LOG = LoggerFactory.getLogger(DateRange.class);
	
	private final Date _startDate, _endDate;
	
	private DateRange(Date from, Date to) {
		_startDate = from;
		_endDate = to;
	}
	
	public static DateRange parse(String dateRange, DateFormat format) {
		String[] date = dateRange.split("-");
		if (date.length == 2) {
			try {
				Date start = format.parse(date[0]);
				Date end = format.parse(date[1]);
				
				return new DateRange(start, end);
			} catch (ParseException e) {
				LOG.warn("Invalid Date Format.", e);
			}
		}
		return new DateRange(null, null);
	}
	
	public boolean isValid() {
		return (_startDate != null) && (_endDate != null) && _startDate.before(_endDate);
	}
	
	public boolean isWithinRange(Date date) {
		return (date.equals(_startDate) || date.after(_startDate)) //
			&& (date.equals(_endDate) || date.before(_endDate));
	}
	
	public Date getEndDate() {
		return _endDate;
	}
	
	public Date getStartDate() {
		return _startDate;
	}
	
	@Override
	public String toString() {
		return "DateRange: From: " + getStartDate() + " To: " + getEndDate();
	}
}
