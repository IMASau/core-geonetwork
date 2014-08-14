package org.fao.geonet.kernel.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import jeeves.server.context.ServiceContext;
import jeeves.utils.Xml;

import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.ThesaurusManager;
import org.fao.geonet.kernel.search.classifier.BroaderTerm;
import org.fao.geonet.kernel.search.classifier.Split;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;

public class ConfigLoaderTest {
	private Element testDimensions;

	@Before
	public void loadTestData() throws IOException, JDOMException {
		URL url = this.getClass().getResource("/org/fao/geonet/kernel/search/config-summary.xml");
		Element configSummary = Xml.loadFile(url);
		testDimensions = Xml.selectElement(configSummary, "dimensions");
	}

	@Test
	public void testNewInstance() throws Exception {
		Element keywordElement = Xml.selectElement(testDimensions, "dimension[@name='Keyword']");
		@SuppressWarnings("unchecked")
		List<Element> keywordParams = (List <Element>) keywordElement.getChildren();
		ConfigLoader loader = new ConfigLoader(null);

		Split splitClassifier = (Split) loader.newInstance(
			"org.fao.geonet.kernel.search.classifier.Split",
			keywordParams
		);

		List<String> categories = splitClassifier.classify("one-two", "eng");

		assertEquals(categories.size(), 2);
		assertEquals(categories.get(0), "one");
		assertEquals(categories.get(1), "two");
	}

	@Test
	public void testThesaurusManagerWiring() throws Exception {
		List<Element> orgParams = mockParams();
		ServiceContext context = mockContext();
		
		ConfigLoader loader = new ConfigLoader(context);

		@SuppressWarnings("unused")
		BroaderTerm broaderClassifier = (BroaderTerm) loader.newInstance(
			"org.fao.geonet.kernel.search.classifier.BroaderTerm",
			orgParams
		);

		verify(getGeonetContext(context)).getThesaurusManager();
	}

	private GeonetContext getGeonetContext(ServiceContext context) {
		return (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
	}

	private List<Element> mockParams() throws JDOMException {
		Element orgElement = Xml.selectElement(testDimensions, "dimension[@name='organisation']");
		@SuppressWarnings("unchecked")
		List<Element> orgParams = (List <Element>) orgElement.getChildren();
		return orgParams;
	}

	private ServiceContext mockContext() {
		GeonetContext geonetContext = mock(GeonetContext.class);
		ThesaurusManager mockManager = mock(ThesaurusManager.class);
		when(geonetContext.getThesaurusManager()).thenReturn(mockManager);
		when(mockManager.getThesaurusByConceptScheme("http://geonetwork-opensource.org/regions")).thenReturn(null);
		
		ServiceContext context = mock(ServiceContext.class);
		when(context.getHandlerContext(Geonet.CONTEXT_NAME)).thenReturn(geonetContext);
		return context;
	}
}
