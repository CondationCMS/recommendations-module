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



import com.condation.cms.api.db.ContentNode;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author thmar
 */
public record IndexDocument(String uri, String title, String content, List<String> tags, ContentNode node) {

	public IndexDocument(String uri, String title, String content, ContentNode node) {
		this(uri, title, content, Collections.emptyList(), node);
	}
}
