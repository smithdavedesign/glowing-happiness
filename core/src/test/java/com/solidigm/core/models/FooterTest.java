package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.solidigm.core.beans.Link;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.utils.json.JSONParser;
import org.apache.sling.resourcebuilder.api.ResourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(AemContextExtension.class)
public class FooterTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for FooterModel
        ctx.addModelsForClasses(Footer.class);
        ctx.load().json("/com/solidigm/core/models/FooterTest.json", "/content");
    }

    @Test
    void testGetSearchTitleEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getSearchTitle()));
    }

    @Test
    void testGetPlaceholderTextEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getPlaceholderText()));
    }

    @Test
    void testGetSearchResultsPageEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getSearchResultsPage()));
    }

    @Test
    void testGetDisclaimerEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getDisclaimer()));
    }

    @Test
    void testTrademarkEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getTrademark()));
    }

    @Test
    void testCopyrightEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertTrue(StringUtils.isEmpty(footer.getCopyright()));
    }

    @Test
    void testHomePageEmpty() {
        Link expected = new Link();
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getHomePage().getLink());
    }

    @Test
    void testFacebookEmpty() {
        Link expected = new Link();
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getFacebook().getLink());
    }

    @Test
    void testLinkedInEmpty() {
        Link expected = new Link();
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getLinkedIn().getLink());
    }

    @Test
    void testTrustArcEmpty() {
        Link expected = new Link();
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getTrustArc().getLink());
    }

    @Test
    void testLinksEmpty() {
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertNull(footer.getLinks());
    }

    @Test
    void testSearchTagLinksEmpty() {
        List<Link> expected = new ArrayList<>();
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getSearchTagsLinks());
    }

    @Test
    void testGetSearchTitleAuthored() {
        String expected = "Search Title";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getSearchTitle());
    }

    @Test
    void testGetPlaceholderTextAuthored() {
        String expected = "Placeholder Text";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getPlaceholderText());
    }

    @Test
    void testGetSearchResultsPageAuthored() {
        String expected = "/test/search/results/page";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getSearchResultsPage());
    }

    @Test
    void testGetDisclaimerAuthored() {
        String expected = "<p>Test Disclaimer</p>";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getDisclaimer());
    }

    @Test
    void testTrademarkAuthored() {
        String expected = "<p>Test Trademark</p>";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getTrademark());
    }

    @Test
    void testCopyrightAuthored() {
        String expected = "<p>Test Copyright</p>";
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getCopyright());
    }

    @Test
    void testHomePageAuthored() {
        Link expected = new Link();
        expected.setLink("/home/page");
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getHomePage().getLink());
        assertEquals(expected.getTarget(), footer.getHomePage().getTarget());
    }

    @Test
    void testFacebookAuthored() {
        Link expected = new Link();
        expected.setLink("https://facebook");
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getFacebook().getLink());
        assertEquals(expected.getTarget(), footer.getFacebook().getTarget());
    }

    @Test
    void testLinkedInAuthored() {
        Link expected = new Link();
        expected.setLink("https://linkedin");
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getLinkedIn().getLink());
        assertEquals(expected.getTarget(), footer.getLinkedIn().getTarget());
    }

    @Test
    void testTrustArcAuthored(){
        Link expected = new Link();
        expected.setLink("https://trustarc");
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected.getLink(), footer.getTrustArc().getLink());
        assertEquals(expected.getTarget(), footer.getTrustArc().getTarget());
    }

    @Test
    void testLinksAuthored() {
        List<Link> expected = new ArrayList<>();
        Link link1 = new Link();
        Link link2 = new Link();
        link1.setLink("/first/test/page");
        link1.setLabel("First Link");
        link2.setLink("/second/test/page");
        link2.setLabel("Second Link");
        expected.add(link1);
        expected.add(link2);
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        List<Link> actual = footer.getLinks();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++ ) {
            assertEquals(expected.get(i).getLabel(), actual.get(i).getLabel());
            assertEquals(expected.get(i).getLink(), actual.get(i).getLink());
        }
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testSearchTagLinksAuthored() {
        List<Link> expected = new ArrayList<>();
        Link link1 = new Link();
        Link link2 = new Link();
        link1.setLink("/test/search/results/page.solidigm%3Aproducts.html");
        link1.setLabel("Products");
        link2.setLink("/test/search/results/page.solidigm%3Aproducts--intel.html");
        link2.setLabel("Intel");
        expected.add(link1);
        expected.add(link2);
        ctx.currentResource("/content/footerAuthored");
        Footer footer = ctx.request().adaptTo(Footer.class);
        List<Link> actual = footer.getSearchTagsLinks();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++ ) {
            assertEquals(expected.get(i).getLabel(), actual.get(i).getLabel());
            assertEquals(expected.get(i).getLink(), actual.get(i).getLink());
        }
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/footer";
        ctx.currentResource("/content/footerEmpty");
        Footer footer = ctx.request().adaptTo(Footer.class);
        assertEquals(expected, footer.getExportedType());
    }
}
