package com.condation.cms.modules.recommendations.extensions;

/*-
 * #%L
 * components-module
 * %%
 * Copyright (C) 2024 CondationCMS
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

import com.condation.cms.api.feature.features.DBFeature;
import com.condation.cms.api.module.CMSModuleContext;
import com.condation.cms.api.module.CMSRequestContext;
import com.condation.cms.modules.recommendations.RenderFunction;
import com.condation.cms.modules.recommendations.SimpleRecommendation;
import com.condation.modules.api.ModuleLifeCycleExtension;
import com.condation.modules.api.annotation.Extension;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author t.marx
 */
@Slf4j
@Extension(ModuleLifeCycleExtension.class)
public class LifecycleExtension extends ModuleLifeCycleExtension<CMSModuleContext, CMSRequestContext> {

	public static SimpleRecommendation SIMPLE_RECOMMENDATION;
	public static RenderFunction RENDER_FUNCTION;

	@Override
	public void init() {

	}

	@Override
	public void activate() {
		SIMPLE_RECOMMENDATION = new SimpleRecommendation(getContext().get(DBFeature.class).db());
		RENDER_FUNCTION = new RenderFunction(getContext());
	}

}
