package com.condation.cms.modules.recommendations;

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



import com.condation.cms.api.model.ListNode;
import com.condation.cms.api.model.Parameter;
import com.condation.cms.api.module.CMSRequestContext;
import com.condation.cms.modules.recommendations.extensions.LifecycleExtension;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author t.marx
 */
public class RecommendationHandler {

	public String handleRecommendations(Parameter params, CMSRequestContext requestContext) {
		var type = (String)params.getOrDefault("type", "newest");
		List<ListNode> items = switch (type) {
			case "newest" -> newest(params, requestContext);
			case "search" -> search(params, requestContext);
			default -> Collections.emptyList();
		};
		
		if (items.isEmpty()) {
			return "";
		}
		
		return renderRecommendations(params, items, requestContext);
	}

	private String renderRecommendations(Parameter params, List<ListNode> items, CMSRequestContext requestContext) {
		return LifecycleExtension.RENDER_FUNCTION
				.render(
						(String) params.get("template"),
						Map.of(
								"title", params.getOrDefault("title", ""),
								"items", items
						),
						requestContext);
	}

	private List<ListNode> newest(Parameter params, CMSRequestContext requestContext) {
		return LifecycleExtension.SIMPLE_RECOMMENDATION.newest(
				(String) params.get("start"),
				(int) params.getOrDefault("size", 5),
				requestContext);

	}
	
	private List<ListNode> search(Parameter params, CMSRequestContext requestContext) {
		return LifecycleExtension.SEARCH_RECOMMENDATION.query(
				(String) params.get("query"),
				(int) params.getOrDefault("size", 5),
				requestContext);

	}
}
