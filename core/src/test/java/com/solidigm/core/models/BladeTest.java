package com.solidigm.core.models;

import com.solidigm.core.beans.CallToActionButton;
import com.solidigm.core.beans.Link;
import com.solidigm.core.services.BrightcoveVideoService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class BladeTest {

    private final AemContext ctx = new AemContext();

    @Mock
    BrightcoveVideoService brightcoveVideoService;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for bladeModel
        MockitoAnnotations.initMocks(this);
        ctx.addModelsForClasses(Blade.class);
        ctx.load().json("/com/solidigm/core/models/BladeTest.json", "/content");
        Mockito.when(brightcoveVideoService.getAccountId()).thenReturn("account");
        Mockito.when(brightcoveVideoService.getPlayerId()).thenReturn("player");
        ctx.registerService(
          BrightcoveVideoService.class, brightcoveVideoService, org.osgi.framework.Constants.SERVICE_RANKING,
          Integer.MAX_VALUE);
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/blade";
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getExportedType());
    }

    @Test
    void testGetId(){
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertFalse(StringUtils.isEmpty(blade.getModalID()));
    }

    @Test
    void testGetTitleEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(StringUtils.isEmpty(blade.getTitle()));
    }

    @Test
    void testGetDescriptionEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(StringUtils.isEmpty(blade.getDescription()));
    }

    @Test
    void testGetImageReferenceEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(StringUtils.isEmpty(blade.getImageReference()));
    }

    @Test
    void testGetAltTextEmpty(){
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(StringUtils.isEmpty(blade.getAltText()));
    }

    @Test
    void testGetCallToActionsEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertNull(blade.getCallToActions());
    }

    @Test
    void testGetBulletsEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertNull(blade.getBullets());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/bladeEmpty");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertFalse(blade.hasContent());
    }

    @Test
    void testGetTitleAuthored() {
        String expected = "Test Title";
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getTitle());
    }

    @Test
    void testGetDescriptionAuthored() {
        String expected = "Test Description";
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getDescription());
    }

    @Test
    void testGetImageReferenceAuthored() {
        String expected = "/image/reference";
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getImageReference());
    }

    @Test
    void testGetAltTextAuthored(){
        String expected = "Test Alt Text";
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getAltText());
    }
    @Test
    void testGetCallToActionsAuthored() {
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        List<CallToActionButton> callToActions = blade.getCallToActions();
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
    void testGetBulletsAuthored() {
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        List<Link> bullets = blade.getBullets();
        assertEquals(2, bullets.size());
        assertEquals("First Bullet", bullets.get(0).getLabel());
        assertEquals("/first/bullet.html", bullets.get(0).getLink());
        assertEquals("_self", bullets.get(0).getTarget());
        assertEquals("Second Bullet", bullets.get(1).getLabel());
        assertEquals("https://secondBullet", bullets.get(1).getLink());
        assertEquals("_blank", bullets.get(1).getTarget());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(blade.hasContent());
    }

    @Test
    void testShowVideoAuthored(){
        ctx.currentResource("/content/bladeAuthored");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertTrue(blade.showVideo());
    }

    @Test
    void testGetAltTextFromDAM(){
        String expected = "Test Image Alt from DAM";
        ctx.currentResource("/content/bladeAltTextFromDAM");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertEquals(expected, blade.getAltText());
    }

    @Test
    void testShowVideoEmpty(){
        //Image authored but not video should display image
        ctx.currentResource("/content/bladeAltTextFromDAM");
        Blade blade = ctx.request().adaptTo(Blade.class);
        assertFalse(blade.showVideo());
    }
}
