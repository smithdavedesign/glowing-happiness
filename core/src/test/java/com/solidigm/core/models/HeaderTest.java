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
        assertTrue(StringUtils.isEmpty(header.getNavItemTitle1()));
        assertTrue(StringUtils.isEmpty(header.getNavItemTitle2()));
        assertTrue(StringUtils.isEmpty(header.getNavItemTitle3()));
        assertTrue(StringUtils.isEmpty(header.getNavItemTitle4()));
        assertTrue(StringUtils.isEmpty(header.getNavItemTitle5()));
    }

    @Test
    void testNavItemLinkEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getNavItemLink1()));
        assertTrue(StringUtils.isEmpty(header.getNavItemLink2()));
        assertTrue(StringUtils.isEmpty(header.getNavItemLink3()));
        assertTrue(StringUtils.isEmpty(header.getNavItemLink4()));
        assertTrue(StringUtils.isEmpty(header.getNavItemLink5()));
    }

    @Test
    void testSubNavItemsEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> subNavItems1 = header.getSubNavItems1();
        List<Link> subNavItems2 = header.getSubNavItems2();
        List<Link> subNavItems3 = header.getSubNavItems3();
        List<Link> subNavItems4 = header.getSubNavItems4();
        List<Link> subNavItems5 = header.getSubNavItems5();
        assertEquals(0, subNavItems1.size());
        assertEquals(0, subNavItems2.size());
        assertEquals(0, subNavItems3.size());
        assertEquals(0, subNavItems4.size());
        assertEquals(0, subNavItems5.size());
    }

    @Test
    void testLangItemsEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        List<Link> langItems = header.getLangItems();
        assertEquals(0, langItems.size());
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
        assertTrue(StringUtils.isEmpty(header.getSearchResultsPage()));
    }

    @Test
    void testDashboardEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getDashboard()));
    }

    @Test
    void testHomepageEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getHomepage()));
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
        assertEquals(expected1, header.getNavItemTitle1());
        assertEquals(expected2, header.getNavItemTitle2());
        assertEquals(expected3, header.getNavItemTitle3());
        assertEquals(expected4, header.getNavItemTitle4());
        assertEquals(expected5, header.getNavItemTitle5());
    }

    @Test
    void testGetNavItemLinkAuthored() {
        String expected1 = "/content/solidigm/us/en/link-one";
        String expected2 = "/content/solidigm/us/en/link-two";
        String expected3 = "/content/solidigm/us/en/link-three";
        String expected4 = "/content/solidigm/us/en/link-four";
        String expected5 = "/content/solidigm/us/en/link-five";
        ctx.currentResource("/content/headerAuthored");
        Header header = ctx.request().adaptTo(Header.class);
        assertEquals(expected1, header.getNavItemLink1());
        assertEquals(expected2, header.getNavItemLink2());
        assertEquals(expected3, header.getNavItemLink3());
        assertEquals(expected4, header.getNavItemLink4());
        assertEquals(expected5, header.getNavItemLink5());
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
        List<Link> subNavItems2 = header.getSubNavItems1();
        assertEquals(1, subNavItems2.size());
        assertEquals("SubItem1 of Nav2", subNavItems2.get(0).getLabel());
        assertEquals("https://www.externalLink.com", subNavItems2.get(0).getLink());
        assertEquals("_blank", subNavItems2.get(0).getTarget());
    }
}