package com.solidigm.core.models;

import com.day.cq.tagging.Tag;
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
        assertEquals("solidigm:partners/countries/usa", partners.get(0).getCountryID());
        assertEquals("USA", partners.get(0).getCountryName());
        assertEquals("Test Additional Info", partners.get(0).getAdditionalInfo());
        assertEquals("https://test.com", partners.get(0).getLink());
        assertEquals("solidigm:partners/products/intel", partners.get(0).getProducts()[0]);
        assertEquals("solidigm:partners/products/consumer", partners.get(0).getProducts()[1]);
        assertEquals("Test Corporation", partners.get(1).getCompanyName());
        assertEquals("solidigm:partners/countries/germany", partners.get(1).getCountryID());
        assertEquals("Germany", partners.get(1).getCountryName());
        assertNull( partners.get(1).getAdditionalInfo());
        assertEquals("https://test2.com", partners.get(1).getLink());
        assertEquals("solidigm:partners/products/intel", partners.get(1).getProducts()[0]);
    }

    @Test
    void testGetCountries() {
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        List<Tag> countries = partnerList.getCountries();
        assertEquals(2, countries.size());
        assertEquals("USA", countries.get(0).getTitle());
        assertEquals("solidigm:partners/countries/usa", countries.get(0).getTagID());
        assertEquals("Germany", countries.get(1).getTitle());
        assertEquals("solidigm:partners/countries/germany", countries.get(1).getTagID());
    }

    @Test
    void testGetProducts() {
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        List<Tag> products = partnerList.getProducts();
        assertEquals(2, products.size());
        assertEquals("Intel", products.get(0).getTitle());
        assertEquals("solidigm:partners/products/intel", products.get(0).getTagID());
        assertEquals("Consumer", products.get(1).getTitle());
        assertEquals("solidigm:partners/products/consumer", products.get(1).getTagID());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/partnerListAuthored");
        PartnerList partnerList = ctx.request().adaptTo(PartnerList.class);
        assertTrue(partnerList.hasContent());
    }
}
