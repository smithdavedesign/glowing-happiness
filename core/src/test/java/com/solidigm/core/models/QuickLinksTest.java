package com.solidigm.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class QuickLinksTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for QuickLinksModel
        ctx.addModelsForClasses(QuickLinks.class);
        ctx.load().json("/com/solidigm/core/models/QuickLinksTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/quicklinks";
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getExportedType());
    }

    @Test
    void testGetTitleEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getTitle()));
    }

    @Test
    void testGetLinkLabelEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getSeeMoreLinkLabel()));
    }

    @Test
    void testGetLinkEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getSeeMoreLink().getLink()));
    }

    @Test
    void testLinkInfoEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertNull(quicklinks.getLinks());
    }

    @Test
    void testHasContentEmpy() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertFalse(quicklinks.hasContent());
    }

    @Test
    void testGetTitleAuthored() {
        String expected = "Authored Title";
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getTitle());
    }

    @Test
    void testGetLinkLabelAuthored() {
        String expected = "See More Solutions";
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getSeeMoreLinkLabel());
    }

    @Test
    void testGetLinkAuthoredInternal() {
        String expected = "/content/solidigm/us/en.html";
        String expectedTarget = "_self";
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getSeeMoreLink().getLink());
        assertEquals(expectedTarget, quicklinks.getSeeMoreLink().getTarget());
    }

    @Test
    void testGetLinkAuthoredExternal() {
        String expected = "https://facebook.com";
        String expectedTarget = "_blank";
        ctx.currentResource("/content/quicklinksExternal");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getSeeMoreLink().getLink());
        assertEquals(expectedTarget, quicklinks.getSeeMoreLink().getTarget());
    }

    @Test
    void testLinkInfoAuthored() {
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(3, quicklinks.getLinks().size());
        assertEquals("Get Support", quicklinks.getLinks().get(0).getLinkTitle());
        assertEquals("Help Us to Help You", quicklinks.getLinks().get(0).getLinkSubTitle());
        assertEquals("/content/solidigm/us/en.html", quicklinks.getLinks().get(0).getLink());
        assertEquals("_self", quicklinks.getLinks().get(0).getTarget());
        assertEquals("", quicklinks.getLinks().get(0).getLinkType());

        assertEquals("Press Releases", quicklinks.getLinks().get(1).getLinkTitle());
        assertEquals("Visit news.solidigm.com", quicklinks.getLinks().get(1).getLinkSubTitle());
        assertEquals("https://www.externalLink.com", quicklinks.getLinks().get(1).getLink());
        assertEquals("_blank", quicklinks.getLinks().get(1).getTarget());
        assertEquals("cmp-quick-links__link--external", quicklinks.getLinks().get(1).getLinkType());

        assertEquals("Join\r\nOur Team", quicklinks.getLinks().get(2).getLinkTitle());
        assertNull(quicklinks.getLinks().get(2).getLinkSubTitle());
        assertEquals("/content/solidigm/us/en.html", quicklinks.getLinks().get(2).getLink());
        assertEquals("_self", quicklinks.getLinks().get(2).getTarget());
        assertEquals("", quicklinks.getLinks().get(2).getLinkType());
    }

    @Test
    void testHasContentAuthored() {
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(quicklinks.hasContent());
    }
}
