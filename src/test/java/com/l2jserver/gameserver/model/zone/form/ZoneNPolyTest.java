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
package com.l2jserver.gameserver.model.zone.form;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * ZoneNPoly unit tests
 * @author Kita
 */
class ZoneNPolyTest {
	
	@Test
	void testBoundingBoxForNPoly() {
		final int[] xCoords = { 0, 1, 0, 4,  4, 3, 2 };
		final int[] yCoords = { 0, 1, 2, 2, -3, 1, 0 };
		final var zone = new ZoneNPoly(xCoords, yCoords, 0, 3);
		final var boundingBox = zone.getBoundingBox();
		
		assertThat(boundingBox).isNotNull();
		assertThat(boundingBox.getX()).isEqualTo(0);
		assertThat(boundingBox.getY()).isEqualTo(-3);
		assertThat(boundingBox.getWidth()).isEqualTo(4);
		assertThat(boundingBox.getHeight()).isEqualTo(5);
	}
}