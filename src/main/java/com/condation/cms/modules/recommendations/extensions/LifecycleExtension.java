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


import com.condation.cms.api.db.DB;
import com.condation.cms.api.eventbus.events.ContentChangedEvent;
import com.condation.cms.api.eventbus.events.TemplateChangedEvent;
import com.condation.cms.api.feature.features.DBFeature;
import com.condation.cms.api.feature.features.EventBusFeature;
import com.condation.cms.api.feature.features.SitePropertiesFeature;
import com.condation.cms.api.module.SiteModuleContext;
import com.condation.cms.api.module.SiteRequestContext;
import com.condation.cms.modules.recommendations.RenderFunction;
import com.condation.cms.modules.recommendations.SearchRecommendation;
import com.condation.cms.modules.recommendations.SimpleRecommendation;
import com.condation.cms.modules.recommendations.index.FileIndexingVisitor;
import com.condation.cms.modules.recommendations.index.SearchEngine;
import com.condation.modules.api.ModuleLifeCycleExtension;
import com.condation.modules.api.annotation.Extension;
import java.io.IOException;
import java.nio.file.Files;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author t.marx
 */
@Slf4j
@Extension(ModuleLifeCycleExtension.class)
public class LifecycleExtension extends ModuleLifeCycleExtension<SiteModuleContext, SiteRequestContext> {

	public static SimpleRecommendation SIMPLE_RECOMMENDATION;
	public static SearchRecommendation SEARCH_RECOMMENDATION;
	public static RenderFunction RENDER_FUNCTION;

	public static SearchEngine searchEngine;

	@Override
	public void init() {

	}

	private String getLanguage() {
		String language = (String) getContext().get(SitePropertiesFeature.class).siteProperties().get("language");
		if (language == null) {
			language = "standard";
		}
		return language;
	}

	protected void reindexContext() {
		var contentPath = getContext().get(DBFeature.class).db().getFileSystem().resolve("content");
		try {
			searchEngine.clear();
			Files.walkFileTree(contentPath, new FileIndexingVisitor(
					contentPath,
					LifecycleExtension.searchEngine,
					getContext()
			));
			searchEngine.commit();
		} catch (IOException e) {
			log.error(null, e);
		}
	}

	@Override
	public void activate() {
		final DB db = getContext().get(DBFeature.class).db();
		SIMPLE_RECOMMENDATION = new SimpleRecommendation(db);
		RENDER_FUNCTION = new RenderFunction(getContext());
		
		searchEngine = new SearchEngine();
		try {
			searchEngine.open(
					configuration.getDataDir().toPath().resolve("index"), 
					getLanguage(),
					getContext()
			);

			// stat reindexing
			Thread.ofVirtual().start(() -> {
				reindexContext();
			});
		} catch (IOException e) {
			log.error("error opening serach engine", e);
			throw new RuntimeException(e);
		}
		
		getContext().get(EventBusFeature.class).eventBus().register(ContentChangedEvent.class, (event) -> {
			reindexContext();
		});
		getContext().get(EventBusFeature.class).eventBus().register(TemplateChangedEvent.class, (event) -> {
			reindexContext();
		});
		SEARCH_RECOMMENDATION = new SearchRecommendation(db, searchEngine);
	}

	@Override
	public void deactivate() {
		try {
			searchEngine.close();
		} catch (Exception e) {
			log.error("error closing serach engine", e);
			throw new RuntimeException(e);
		}
	}
}
