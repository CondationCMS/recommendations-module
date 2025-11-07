package com.condation.cms.modules.recommendations.extensions;

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


import java.util.Map;
import java.util.function.Function;

import com.condation.cms.api.extensions.RegisterTagsExtensionPoint;
import com.condation.cms.api.feature.features.ContentNodeMapperFeature;
import com.condation.cms.api.model.Parameter;
import com.condation.cms.modules.recommendations.RecommendationHandler;
import com.condation.modules.api.annotation.Extension;

@Extension(RegisterTagsExtensionPoint.class)
public class ShortCodesExtension extends RegisterTagsExtensionPoint {

	private static RecommendationHandler handler = new RecommendationHandler();
	
	@Override
	public Map<String, Function<Parameter, String>> tags() {

		if (getRequestContext() == null
				|| !getRequestContext().has(ContentNodeMapperFeature.class)) {
			return Map.of(
					"recommendations", (params) -> ""
			);
		}
		return Map.of(
				"recommendations", (params) -> handler.handleRecommendations(params, getRequestContext())
		);
	}
}
