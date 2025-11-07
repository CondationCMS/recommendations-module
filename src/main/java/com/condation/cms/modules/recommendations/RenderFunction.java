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



import com.condation.cms.api.feature.features.IsDevModeFeature;
import com.condation.cms.api.feature.features.TemplateEngineFeature;
import java.util.Map;
import com.condation.cms.api.module.SiteModuleContext;
import com.condation.cms.api.module.SiteRequestContext;
import com.condation.cms.api.template.TemplateEngine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RenderFunction {

    private final String templateExtension;
	
	public RenderFunction (SiteModuleContext context) {
		this.templateExtension = Helpers.getTemplateFileExtension(context);
	}

    public String render (String name, Map<String, Object> data, SiteRequestContext requestContext) {

		var templateEngine = requestContext.get(TemplateEngineFeature.class).templateEngine();
		var devMode = requestContext.has(IsDevModeFeature.class);

        var templateFile = "%s.%s".formatted(name, templateExtension);

		TemplateEngine.Model model = new TemplateEngine.Model(null, null, requestContext);
        model.values.putAll(data);

        try {
            return templateEngine.render(templateFile, model);
        } catch (Exception e) {
            log.error("", e);

            if (devMode) {
                return "[template %s not found]".formatted(name);
            }
        }

        return "";
    }
}

