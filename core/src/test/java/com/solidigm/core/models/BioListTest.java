package com.solidigm.core.models;

import com.solidigm.core.beans.BioItem;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class BioListTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for BioListModel
        ctx.addModelsForClasses(BioList.class);
        ctx.load().json("/com/solidigm/core/models/BioListTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/biolist";
        ctx.currentResource("/content/biolistEmpty");
        BioList biolist = ctx.request().adaptTo(BioList.class);
        assertEquals(expected, biolist.getExportedType());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/biolistEmpty");
        BioList biolist = ctx.request().adaptTo(BioList.class);
        assertFalse(biolist.hasContent());
    }

    @Test
    void testGetBioItemsAuthored() {
        ctx.currentResource("/content/biolistAuthored");
        BioList biolist = ctx.request().adaptTo(BioList.class);
        List<BioItem> bioItems = biolist.getBioItems();
        assertNotNull(bioItems);
        assertEquals(bioItems.size(), 2);
        assertEquals("/content/image1", bioItems.get(0).getImageReference());
        assertEquals("Test Authored Alt", bioItems.get(0).getImageAltText());
        assertEquals("Test Name 1", bioItems.get(0).getName());
        assertEquals("Test Title 1", bioItems.get(0).getTitle());
        assertEquals("Test Description 1", bioItems.get(0).getDescription());
        assertEquals("/content/image2", bioItems.get(1).getImageReference());
        assertEquals("Test Alt from DAM", bioItems.get(1).getImageAltText());
        assertEquals("Test Name 2", bioItems.get(1).getName());
        assertEquals("Test Title 2", bioItems.get(1).getTitle());
        assertEquals("Test Description 2", bioItems.get(1).getDescription());
        assertNotEquals(bioItems.get(0).getModalID(), bioItems.get(1).getModalID());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/biolistAuthored");
        BioList biolist = ctx.request().adaptTo(BioList.class);
        assertTrue(biolist.hasContent());
    }
}
