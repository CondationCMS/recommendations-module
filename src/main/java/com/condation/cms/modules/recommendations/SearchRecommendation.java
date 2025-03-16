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


import com.condation.cms.api.db.ContentNode;
import com.condation.cms.api.db.ContentQuery;
import com.condation.cms.api.db.DB;
import com.condation.cms.api.db.cms.ReadOnlyFile;
import com.condation.cms.api.feature.features.ContentNodeMapperFeature;
import com.condation.cms.api.feature.features.CurrentNodeFeature;
import com.condation.cms.api.model.ListNode;
import com.condation.cms.api.request.RequestContext;
import com.condation.cms.api.utils.HTTPUtil;
import com.condation.cms.api.utils.PathUtil;
import com.condation.cms.modules.recommendations.index.SearchEngine;
import com.condation.cms.modules.recommendations.index.SearchRequest;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author t.marx
 */
@RequiredArgsConstructor
public class SearchRecommendation {

	private final DB db;
	private final SearchEngine searchEngine;

	public List<ListNode> query(String query, int size, final RequestContext requestContext) {

		SearchRequest request = new SearchRequest(query, 1, size + 1);
		
		var result = searchEngine.search(request);
		
		BiFunction<ContentNode, Integer, ListNode> mapperFunction = (node, excerptLength) -> {
			return requestContext.get(ContentNodeMapperFeature.class)
					.contentNodeMapper()
					.toListNode(node, requestContext, excerptLength);
		};

		var hits = result.getItems().stream()
				.map(item -> db.getContent().byUri(item.uri.substring(1)+".md"))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(ContentNode.class::cast)
				.map(node -> mapperFunction.apply(node, 120))
				.filter(node -> filterCurrentNode(node, requestContext))
				.toList();
		
		
		return hits.subList(0, Math.min(size, hits.size()));
	}

	private boolean filterCurrentNode(ListNode node, final RequestContext requestContext) {
		var currentNode = requestContext.get(CurrentNodeFeature.class).node();

		return !node.path().equals(getPath(currentNode, requestContext));
	}

	private String getPath(ContentNode node, final RequestContext requestContext) {
		final ReadOnlyFile contentBase = db.getReadOnlyFileSystem().contentBase();
		var temp_path = contentBase.resolve(node.uri());
		var url = PathUtil.toURI(temp_path, contentBase);

		return HTTPUtil.modifyUrl(url, requestContext);
	}
}
