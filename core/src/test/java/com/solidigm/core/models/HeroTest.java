package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.solidigm.core.beans.AnchorLink;
import com.solidigm.core.beans.CallToActionButton;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.List;

@ExtendWith(AemContextExtension.class)
public class HeroTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for HeroModel
        ctx.addModelsForClasses(Hero.class);
        ctx.load().json("/com/solidigm/core/models/HeroTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/hero";
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getExportedType());
    }

    @Test
    void testGetTitleEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getTitle()));
    }

    @Test
    void testGetSubtitleEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getSubtitle()));
    }

    @Test
    void testGetDescriptionEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getDescription()));
    }

    @Test
    void testGetDesktopImageReferenceEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getDesktopImageReference()));
    }

    @Test
    void testGetDesktopAltEmpty(){
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getDesktopAltText()));
    }

    @Test
    void testGetMobileImageReferenceEmpty() {
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getMobileImageReference()));
    }

    @Test
    void testGetMobileAltEmpty(){
        ctx.currentResource("/content/heroEmpty");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertTrue(StringUtils.isEmpty(hero.getMobileAltText()));
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
    void testGetDesktopAltAuthored(){
        String expected = "Test Desktop Alt Text";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getDesktopAltText());
    }

    @Test
    void testGetMobileImageReferenceAuthored() {
        String expected = "/mobile/image/reference";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getMobileImageReference());
    }

    @Test
    void testGetMobileAltAuthored(){
        String expected = "Test Mobile Alt Text";
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getMobileAltText());
    }

    @Test
    void testGetCallToActionsAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        List<CallToActionButton> callToActions = hero.getCallToActions();
        assertEquals(2, callToActions.size());
        assertEquals("First Button", callToActions.get(0).getLabel());
        assertEquals("/first/link.html", callToActions.get(0).getLink());
        assertEquals("transparent", callToActions.get(0).getButtonStyle());
        assertEquals("_self", callToActions.get(0).getTarget());
        assertEquals("Second Button", callToActions.get(1).getLabel());
        assertEquals("https://second/link", callToActions.get(1).getLink());
        assertEquals("opaque", callToActions.get(1).getButtonStyle());
        assertEquals("_blank", callToActions.get(1).getTarget());
    }

    @Test
    void testGetAnchorsAuthored() {
        ctx.currentResource("/content/heroAuthored");
        Hero hero = ctx.request().adaptTo(Hero.class);
        List<AnchorLink> anchors = hero.getAnchors();
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

    @Test
    void testGetDesktopAltFromDAM(){
        String expected = "Test Desktop Alt from DAM";
        ctx.currentResource("/content/heroAltTextFromDAM");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getDesktopAltText());
    }

    @Test
    void testGetMobileAltFromDAM(){
        String expected = "Test Mobile Alt from DAM";
        ctx.currentResource("/content/heroAltTextFromDAM");
        Hero hero = ctx.request().adaptTo(Hero.class);
        assertEquals(expected, hero.getMobileAltText());
    }
}
