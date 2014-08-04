//===	Copyright (C) 2001-2007 Food and Agriculture Organization of the
//===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===	and United Nations Environment Programme (UNEP)
//===
//===	This program is free software; you can redistribute it and/or modify
//===	it under the terms of the GNU General Public License as published by
//===	the Free Software Foundation; either version 2 of the License, or (at
//===	your option) any later version.
//===
//===	This program is distributed in the hope that it will be useful, but
//===	WITHOUT ANY WARRANTY; without even the implied warranty of
//===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===	General Public License for more details.
//===
//===	You should have received a copy of the GNU General Public License
//===	along with this program; if not, write to the Free Software
//===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
//===
//===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===	Rome - Italy. email: geonetwork@osgeo.org
//==============================================================================

package org.fao.geonet.kernel.search.facet;

import jeeves.server.context.ServiceContext;

import org.jdom.Element;

public class DimensionFormatter implements Formatter {
	private ItemConfig config;

	public DimensionFormatter(ServiceContext context, ItemConfig config) {
		this.config = config;
	}

	@Override
	public Element buildDimensionTag(String value, String count, String langCode) {
		Element dimensionTag = new Element("dimension");
		dimensionTag.setAttribute("name", config.getDimension().getName());
		dimensionTag.setAttribute("value", value);
		dimensionTag.setAttribute("count", count);
		return dimensionTag;
	}

	@Override
	public Element buildCategoryTag(String value, String count, String langCode) {
		Element categoryTag = new Element("category");
		categoryTag.setAttribute("value", value);
		categoryTag.setAttribute("count", count);
		return categoryTag;
	}
}
