package com.condation.cms.modules.recommendations.index;

/*-
 * #%L
 * recommendations-module
 * %%
 * Copyright (C) 2025 CondationCMS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */



/**
 *
 * @author thmar
 */
public class ArrayUtil {

	private static float[] EMPTY_FLOAT_ARRAY = new float[0];

	public static float[] toPrimitive(final Float[] array) {
		if (array == null) {
			return null;
		}
		if (array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		final float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].floatValue();
		}
		return result;
	}
}
