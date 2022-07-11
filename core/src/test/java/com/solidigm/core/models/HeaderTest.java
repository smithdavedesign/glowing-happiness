package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.solidigm.core.beans.Link;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.List;

@ExtendWith(AemContextExtension.class)
public class HeaderTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for HeaderModel
        ctx.addModelsForClasses(Header.class);
        ctx.load().json("/com/solidigm/core/models/HeaderTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/header";
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getExportedType());
    }

    @Test
    void testNavItemTitleEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getNavItem1().getLabel()));
        assertTrue(StringUtils.isEmpty(header.getNavItem2().getLabel()));
        assertTrue(StringUtils.isEmpty(header.getNavItem3().getLabel()));
        assertTrue(StringUtils.isEmpty(header.getNavItem4().getLabel()));
        assertTrue(StringUtils.isEmpty(header.getNavItem5().getLabel()));
    }

    @Test
    void testNavItemLinkEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getNavItem1().getLink()));
        assertTrue(StringUtils.isEmpty(header.getNavItem2().getLink()));
        assertTrue(StringUtils.isEmpty(header.getNavItem3().getLink()));
        assertTrue(StringUtils.isEmpty(header.getNavItem4().getLink()));
        assertTrue(StringUtils.isEmpty(header.getNavItem5().getLink()));
    }

    @Test
    void testSubNavItemsEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertNull(header.getSubNavItems1());
        assertNull(header.getSubNavItems2());
        assertNull(header.getSubNavItems3());
        assertNull(header.getSubNavItems4());
        assertNull(header.getSubNavItems5());
    }

    @Test
    void testLangItemsEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertNull(header.getLangItems());
    }

    @Test
    void testGreetingEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getGreeting()));
    }

    @Test
    void testPlaceHolderTextEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getPlaceholderText()));
    }

    @Test
    void testSearchResultsPageEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getSearchResultsPage().getLink()));
    }

    @Test
    void testDashboardEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getDashboard().getLink()));
    }

    @Test
    void testHomepageEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getHomepage().getLink()));
    }

    @Test
    void testGetNavItemTitleAuthored() {
        String expected1 = "TopNav1 Title";
        String expected2 = "TopNav2";
        String expected3 = "TopNav3";
        String expected4 = "TopNav4";
        String expected5 = "TopNav5";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected1, header.getNavItem1().getLabel());
        assertEquals(expected2, header.getNavItem2().getLabel());
        assertEquals(expected3, header.getNavItem3().getLabel());
        assertEquals(expected4, header.getNavItem4().getLabel());
        assertEquals(expected5, header.getNavItem5().getLabel());
    }

    @Test
    void testGetNavItemLinkAuthored() {
        String expected1 = "/content/solidigm/us/en/link-one.html";
        String expectedTarget1 = "_self";
        String expected2 = "/content/solidigm/us/en/link-two.html";
        String expected3 = "https://www.externalLink.com";
        String expectedTarget3 = "_blank";
        String expected4 = "/content/solidigm/us/en/link-four.html";
        String expected5 = "/content/solidigm/us/en/link-five.html";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected1, header.getNavItem1().getLink());
        assertEquals(expectedTarget1, header.getNavItem1().getTarget());
        assertEquals(expected2, header.getNavItem2().getLink());
        assertEquals(expected3, header.getNavItem3().getLink());
        assertEquals(expectedTarget3, header.getNavItem3().getTarget());
        assertEquals(expected4, header.getNavItem4().getLink());
        assertEquals(expected5, header.getNavItem5().getLink());
    }

    @Test
    void testSubNavItems1AuthoredNoTopNavLink() {
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems1 = header.getSubNavItems1();
        assertEquals(2, subNavItems1.size());
        assertEquals("SubItem1 of Nav1", subNavItems1.get(0).getLabel());
        assertEquals("/first/link.html", subNavItems1.get(0).getLink());
        assertEquals("_self", subNavItems1.get(0).getTarget());
        assertEquals("SubItem2 of Nav1", subNavItems1.get(1).getLabel());
        assertEquals("/second/link.html", subNavItems1.get(1).getLink());
        assertEquals("_self", subNavItems1.get(1).getTarget());
    }

    @Test
    void testSubNavItems2AuthoredNoTopNavLink() {
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems2 = header.getSubNavItems2();
        assertEquals(1, subNavItems2.size());
        assertEquals("SubItem1 of Nav2", subNavItems2.get(0).getLabel());
        assertEquals("https://www.externalLink.com", subNavItems2.get(0).getLink());
        assertEquals("_blank", subNavItems2.get(0).getTarget());
    }

    @Test
    void testSubNavItems3AuthoredNoTopNavLink() {
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems3 = header.getSubNavItems3();
        assertEquals(2, subNavItems3.size());
        assertEquals("SubItem1 of Nav3", subNavItems3.get(0).getLabel());
        assertEquals("/first/link.html", subNavItems3.get(0).getLink());
        assertEquals("_self", subNavItems3.get(0).getTarget());
        assertEquals("SubItem2 of Nav3", subNavItems3.get(1).getLabel());
        assertEquals("/second/link.html", subNavItems3.get(1).getLink());
        assertEquals("_self", subNavItems3.get(1).getTarget());
    }

    @Test
    void testSubNavItems4AuthoredNoTopNavLink() {
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems4 = header.getSubNavItems4();
        assertEquals(2, subNavItems4.size());
        assertEquals("SubItem1 of Nav4", subNavItems4.get(0).getLabel());
        assertEquals("/content/solidigm/us/en.html", subNavItems4.get(0).getLink());
        assertEquals("_self", subNavItems4.get(0).getTarget());
        assertEquals("SubItem2 of Nav4", subNavItems4.get(1).getLabel());
        assertEquals("/content/solidigm/us/en.html", subNavItems4.get(1).getLink());
        assertEquals("_self", subNavItems4.get(1).getTarget());
    }

    @Test
    void testSubNavItems5AuthoredNoTopNavLink() {
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems5 = header.getSubNavItems5();
        assertEquals(2, subNavItems5.size());
        assertEquals("SubItem1 of Nav5", subNavItems5.get(0).getLabel());
        assertEquals("/content/solidigm/us/en.html", subNavItems5.get(0).getLink());
        assertEquals("_self", subNavItems5.get(0).getTarget());
        assertEquals("SubItem2 of Nav5", subNavItems5.get(1).getLabel());
        assertEquals("/content/solidigm/us/en.html", subNavItems5.get(1).getLink());
        assertEquals("_self", subNavItems5.get(1).getTarget());
    }

    @Test
    void testLangItems() {
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> langItems = header.getLangItems();
        assertEquals(2, langItems.size());
        assertEquals("English", langItems.get(0).getLabel());
        assertEquals("/content/solidigm/us/en.html", langItems.get(0).getLink());
        assertEquals("_self", langItems.get(0).getTarget());
        assertEquals("Spanish", langItems.get(1).getLabel());
        assertEquals("/content/solidigm/us/es.html", langItems.get(1).getLink());
        assertEquals("_self", langItems.get(1).getTarget());
    }

    @Test
    void testGetGreetingAuthored() {
        String expected = "Greetings";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getGreeting());
    }

    @Test
    void testGetPlaceholderTextAuthored() {
        String expected = "Search Here...";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getPlaceholderText());
    }

    @Test
    void testGetSearchResultsPageAuthored() {
        String expected = "/content/solidigm/us/en/search-results.html";
        String expectedTarget = "_self";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getSearchResultsPage().getLink());
        assertEquals(expectedTarget, header.getSearchResultsPage().getTarget());
    }

    @Test
    void testGetDashboardAuthored() {
        String expected = "/content/solidigm/us/en/customer-dashboard.html";
        String expectedTarget = "_self";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getDashboard().getLink());
        assertEquals(expectedTarget, header.getDashboard().getTarget());
    }

    @Test
    void testGetDashboardAuthoredExternal() {
        String expected = "https://www.externalLink.com";
        String expectedTarget = "_blank";
        ctx.currentResource("/content/headerAuthoredNoTopNavLinks");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected, header.getDashboard().getLink());
        assertEquals(expectedTarget, header.getDashboard().getTarget());
    }

    @Test
    void testGetHomepageAuthored() {
        String expected = "/content/solidigm/us/en.html";
        String expectedLabel = "Solidigm";
        String expectedTarget = "_self";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expectedLabel, header.getHomepage().getLabel());
        assertEquals(expected, header.getHomepage().getLink());
        assertEquals(expectedTarget, header.getHomepage().getTarget());
    }
}