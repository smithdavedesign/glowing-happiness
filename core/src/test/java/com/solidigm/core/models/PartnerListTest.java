package com.solidigm.core.models;

import com.solidigm.core.beans.Partner;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class PartnerListTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for PartnerListModel
        ctx.addModelsForClasses(PartnerList.class);
        ctx.load().json("/com/solidigm/core/models/PartnerListTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/partnerlist";
        ctx.currentResource("/content/partnerListEmpty");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        assertEquals(expected, partnerList.getExportedType());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/partnerListEmpty");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        assertFalse(partnerList.hasContent());
    }

    @Test
    void testGetPartnerType() {
        String expected = "Test Partner Type";
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        assertEquals(expected, partnerList.getPartnerType());
    }

    @Test
    void testGetPartners() {
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        List<Partner> partners = partnerList.getPartners();
        assertEquals(2, partners.size());
        assertEquals("Test Company", partners.get(0).getCompanyName());
        assertEquals("solidigm:products", partners.get(0).getCountryID());
        assertEquals("Products", partners.get(0).getCountryName());
        assertEquals("Test Additional Info", partners.get(0).getAdditionalInfo());
        assertEquals("https://test.com", partners.get(0).getLink());
        assertEquals("solidigm:products/intel", partners.get(0).getProducts()[0]);
        assertEquals("solidigm:products", partners.get(0).getProducts()[1]);
        assertEquals("Test Corporation", partners.get(1).getCompanyName());
        assertEquals("solidigm:products/intel", partners.get(1).getCountryID());
        assertEquals("Intel", partners.get(1).getCountryName());
        assertNull( partners.get(1).getAdditionalInfo());
        assertEquals("https://test2.com", partners.get(1).getLink());
        assertEquals("solidigm:products", partners.get(1).getProducts()[0]);
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        assertTrue(partnerList.hasContent());
    }
}
