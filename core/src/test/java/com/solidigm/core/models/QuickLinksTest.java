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
        assertTrue(StringUtils.isEmpty(quicklinks.getLinkLabel()));
    }

    @Test
    void testGetLinkEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getLink()));
    }

    @Test
    void testTileInfoEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertNull(quicklinks.getTileInfo());
    }

    @Test
    void testGetTargetEmpty() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getTarget()));
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
        assertEquals(expected, quicklinks.getLinkLabel());
    }

    @Test
    void testGetLinkAuthoredInternal() {
        String expected = "/content/solidigm/us/en.html";
        String expectedTarget = "_self";
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getLink());
        assertEquals(expectedTarget, quicklinks.getTarget());
    }

    @Test
    void testGetLinkAuthoredExternal() {
        String expected = "https://facebook.com";
        String expectedTarget = "_blank";
        ctx.currentResource("/content/quicklinksExternal");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(expected, quicklinks.getLink());
        assertEquals(expectedTarget, quicklinks.getTarget());
    }

    @Test
    void testTileInfoAuthored() {
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertEquals(3, quicklinks.getTileInfo().size());
        assertEquals("Get Support", quicklinks.getTileInfo().get(0).getTileTitle());
        assertEquals("Help Us to Help You", quicklinks.getTileInfo().get(0).getTileSubTitle());
        assertEquals("/content/solidigm/us/en.html", quicklinks.getTileInfo().get(0).getTileLink());
        assertEquals("_self", quicklinks.getTileInfo().get(0).getTarget());
        assertEquals("", quicklinks.getTileInfo().get(0).getLinkType());

        assertEquals("Press Releases", quicklinks.getTileInfo().get(1).getTileTitle());
        assertEquals("Visit news.solidigm.com", quicklinks.getTileInfo().get(1).getTileSubTitle());
        assertEquals("https://www.externalLink.com", quicklinks.getTileInfo().get(1).getTileLink());
        assertEquals("_blank", quicklinks.getTileInfo().get(1).getTarget());
        assertEquals("cmp-quick-links__link--external", quicklinks.getTileInfo().get(1).getLinkType());

        assertEquals("Join\r\nOur Team", quicklinks.getTileInfo().get(2).getTileTitle());
        assertNull(quicklinks.getTileInfo().get(2).getTileSubTitle());
        assertEquals("/content/solidigm/us/en.html", quicklinks.getTileInfo().get(2).getTileLink());
        assertEquals("_self", quicklinks.getTileInfo().get(2).getTarget());
        assertEquals("", quicklinks.getTileInfo().get(2).getLinkType());
    }

    @Test
    void testHasContentAuthored() {
        ctx.currentResource("/content/quicklinksAuthored");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(quicklinks.hasContent());
    }
}
