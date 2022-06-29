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
import java.util.ArrayList;
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
    void testGetTitleEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getTitle()));
    }

    @Test
    void testGetSubtitleEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getSubtitle()));
    }

    @Test
    void testGetDescriptionEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header hero = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(hero.getDescription()));
    }

    @Test
    void testGetDesktopImageReferenceEmpty() {
        ctx.currentResource("/content/headerEmpty");
        Header header = ctx.request().adaptTo(Header.class);
        assertTrue(StringUtils.isEmpty(header.getDesktopImageReference()));
    }

    @Test
    void testGetMobileImageReferenceEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getMobileImageReference()));
    }

    @Test
    void testGetCallToActionsEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertNull(hero.getCallToActions());
    }

    @Test
    void testGetAnchorsEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertNull(hero.getAnchors());
    }

    @Test
    void testGetFilterEmpty() {
        String expected = "no_filter";
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getFilter());
    }

    @Test
    void testGetShowBreadcrumbEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertFalse(hero.getShowBreadcrumb());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertFalse(hero.hasContent());
    }

    @Test
    void testGetTitleAuthored() {
        String expected = "Test\r\nTitle";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getTitle());
    }

    @Test
    void testGetSubtitleAuthored() {
        String expected = "Test\r\nSubtitle";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getSubtitle());
    }

    @Test
    void testGetDescriptionAuthored() {
        String expected = "<p>Test Description</p>";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getDescription());
    }

    @Test
    void testGetDesktopImageReferenceAuthored() {
        String expected = "/desktop/image/reference";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getDesktopImageReference());
    }

    @Test
    void testGetMobileImageReferenceAuthored() {
        String expected = "/mobile/image/reference";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getMobileImageReference());
    }

    @Test
    void testGetCallToActionsAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        List<callToActionButton> callToActions = hero.getCallToActions();
        assertEquals(2, callToActions.size());
        assertEquals("First Button", callToActions.get(0).getButtonLabel());
        assertEquals("/first/link", callToActions.get(0).getButtonLink());
        assertEquals("transparent", callToActions.get(0).getButtonStyle());
        assertEquals("Second Button", callToActions.get(1).getButtonLabel());
        assertEquals("/second/link", callToActions.get(1).getButtonLink());
        assertEquals("opaque", callToActions.get(1).getButtonStyle());
    }

    @Test
    void testGetAnchorsAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        List<anchorLink> anchors = hero.getAnchors();
        assertEquals(2, anchors.size());
        assertEquals("First Anchor", anchors.get(0).getAnchorLabel());
        assertEquals("firstAnchor", anchors.get(0).getAnchorID());
        assertEquals("Second Anchor", anchors.get(1).getAnchorLabel());
        assertEquals("secondAnchor", anchors.get(1).getAnchorID());
    }

    @Test
    void testGetFilterAuthored() {
        String expected = "darken";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getFilter());
    }

    @Test
    void testGetShowBreadcrumbAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(hero.getShowBreadcrumb());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(hero.hasContent());
    }
}