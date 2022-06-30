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
    void testGetTarget() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertTrue(StringUtils.isEmpty(quicklinks.getTarget()));
    }

    @Test
    void testHasContent() {
        ctx.currentResource("/content/quicklinksEmpty");
        QuickLinks quicklinks = ctx.request().adaptTo(QuickLinks.class);
        assertFalse(quicklinks.hasContent());
    }
}
