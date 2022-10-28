package com.solidigm.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;

import com.solidigm.core.services.BrightcoveVideoService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class VideoTest {
    private final AemContext ctx = new AemContext();

    @Mock
    BrightcoveVideoService brightcoveVideoService;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        //Load ctx for VideoModel
        ctx.addModelsForClasses(Video.class);
        ctx.load().json("/com/solidigm/core/models/VideoTest.json", "/content");
        ctx.registerService(
          BrightcoveVideoService.class, brightcoveVideoService, org.osgi.framework.Constants.SERVICE_RANKING,
          Integer.MAX_VALUE);
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/video";
        ctx.currentResource("/content/videoEmpty");
        Video video = ctx.request().adaptTo(Video.class);
        assertEquals(expected, video.getExportedType());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/videoEmpty");
        Video video = ctx.request().adaptTo(Video.class);
        assertFalse(video.hasContent());
    }

    @Test
    void testGetThumbnailImageReferenceAuthored() {
        String expected = "/content/image";
        ctx.currentResource("/content/videoAuthored");
        Video video = ctx.request().adaptTo(Video.class);
        assertEquals(expected, video.getThumbnailImageReference());
    }

    @Test
    void testGetThumbnailAltTextAuthored(){
        String expected = "Test Alt Text";
        ctx.currentResource("/content/videoAuthored");
        Video video = ctx.request().adaptTo(Video.class);
        assertEquals(expected, video.getThumbnailAltText());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/videoAuthored");
        Video video = ctx.request().adaptTo(Video.class);
        assertTrue(video.hasContent());
    }

    @Test
    void testGetAltTextFromDAM(){
        String expected = "Test Image Alt from DAM";
        ctx.currentResource("/content/videoAltTextFromDAM");
        Video video = ctx.request().adaptTo(Video.class);
        assertEquals(expected, video.getThumbnailAltText());
    }
}
