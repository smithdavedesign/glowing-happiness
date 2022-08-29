package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;

@ExtendWith(AemContextExtension.class)
public class TileListTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for TileListModel
        ctx.addModelsForClasses(TileList.class);
        ctx.load().json("/com/solidigm/core/models/TileListTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/tilelist";
        ctx.currentResource("/content/tileListEmpty");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertEquals(expected, tilelist.getExportedType());
    }

    @Test
    void testGetTileInfoEmpty() {
        ctx.currentResource("/content/tileListEmpty");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertNull(tilelist.getTileInfo());
    }

    @Test
    void testHasContentEmpty() {
        ctx.currentResource("/content/tileListEmpty");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertFalse(tilelist.hasContent());
    }

    @Test
    void testGetTileInfoAuthoredExternalLink() {
        ctx.currentResource("/content/tileListAuthored");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertEquals(4, tilelist.getTileInfo().size());
        assertEquals("pto", tilelist.getTileInfo().get(0).getIcon());
        assertEquals("Title 1", tilelist.getTileInfo().get(0).getTitle());
        assertEquals("Description for Item 1", tilelist.getTileInfo().get(0).getDescription());
        assertEquals("Launch Title 1 External", tilelist.getTileInfo().get(0).getLabel());
        assertEquals("https://www.facebook.com", tilelist.getTileInfo().get(0).getLink());
        assertEquals("_blank", tilelist.getTileInfo().get(0).getTarget());
        assertEquals("solidigm-btn--external", tilelist.getTileInfo().get(0).getLinkType());
    }

    @Test
    void testGetTileInfoAuthoredInternalLink() {
        ctx.currentResource("/content/tileListAuthored");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertEquals(4, tilelist.getTileInfo().size());
        assertEquals("agility", tilelist.getTileInfo().get(1).getIcon());
        assertEquals("Title 2", tilelist.getTileInfo().get(1).getTitle());
        assertEquals("Description for Item 2", tilelist.getTileInfo().get(1).getDescription());
        assertEquals("Launch Title 2 Internal", tilelist.getTileInfo().get(1).getLabel());
        assertEquals("/content/solidigm/us/en.html", tilelist.getTileInfo().get(1).getLink());
        assertEquals("_self", tilelist.getTileInfo().get(1).getTarget());
        assertEquals("", tilelist.getTileInfo().get(1).getLinkType());
    }

    @Test
    void testGetTileInfoAuthoredInternalSuffixLink() {
        ctx.currentResource("/content/tileListAuthored");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertEquals(4, tilelist.getTileInfo().size());
        assertEquals("diamond", tilelist.getTileInfo().get(2).getIcon());
        assertEquals("Title 3", tilelist.getTileInfo().get(2).getTitle());
        assertEquals("Description for Item 3", tilelist.getTileInfo().get(2).getDescription());
        assertEquals("Launch Title 3 Internal Suffix", tilelist.getTileInfo().get(2).getLabel());
        assertEquals("/content/solidigm/us/en.html", tilelist.getTileInfo().get(2).getLink());
        assertEquals("_self", tilelist.getTileInfo().get(2).getTarget());
        assertEquals("", tilelist.getTileInfo().get(2).getLinkType());
    }

    //Since link is empty, target & linkType won't matter because the button won't show in html
    @Test
    void testGetTileInfoAuthoredBlankLink() {
        ctx.currentResource("/content/tileListAuthored");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertEquals(4, tilelist.getTileInfo().size());
        assertEquals("people", tilelist.getTileInfo().get(3).getIcon());
        assertEquals("Title 4", tilelist.getTileInfo().get(3).getTitle());
        assertEquals("Description for Item 4", tilelist.getTileInfo().get(3).getDescription());
        assertNull(tilelist.getTileInfo().get(3).getLabel());
        assertNull(tilelist.getTileInfo().get(3).getLink());
        assertEquals("_blank", tilelist.getTileInfo().get(3).getTarget());
        assertEquals("solidigm-btn--external", tilelist.getTileInfo().get(3).getLinkType());
    }

    @Test
    void testHasContentAuthored() {
        ctx.currentResource("/content/tileListAuthored");
        TileList tilelist = ctx.request().adaptTo(TileList.class);
        assertTrue(tilelist.hasContent());
    }
}
