package com.solidigm.core.models;

import com.solidigm.core.beans.Image;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class ProductPhotoGalleryTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for ProductPhotoGalleryModel
        ctx.addModelsForClasses(ProductPhotoGallery.class);
        ctx.load().json("/com/solidigm/core/models/ProductPhotoGalleryTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/productphotogallery";
        ctx.currentResource("/content/productPhotoGalleryEmpty");
        ProductPhotoGallery productPhotoGallery = ctx.request().adaptTo(ProductPhotoGallery.class);
        assertEquals(expected, productPhotoGallery.getExportedType());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/productPhotoGalleryEmpty");
        ProductPhotoGallery productPhotoGallery = ctx.request().adaptTo(ProductPhotoGallery.class);
        assertFalse(productPhotoGallery.hasContent());
    }

    @Test
    void testGetImages() {
        ctx.currentResource("/content/productPhotoGalleryAuthored");
        ProductPhotoGallery productPhotoGallery = ctx.request().adaptTo(ProductPhotoGallery.class);
        List<Image> actual = productPhotoGallery.getImages();
        assertEquals(2, actual.size());
        assertEquals("/content/image1", actual.get(0).getImageReference());
        assertEquals("Test Image Alt from DAM", actual.get(0).getAltText());
        assertEquals("/content/image2", actual.get(1).getImageReference());
        assertEquals("Test Alt Text", actual.get(1).getAltText());
    }

    @Test
    void testGetMainImage() {
        ctx.currentResource("/content/productPhotoGalleryAuthored");
        ProductPhotoGallery productPhotoGallery = ctx.request().adaptTo(ProductPhotoGallery.class);
        assertEquals("/content/image1", productPhotoGallery.getMainImage().getImageReference());
        assertEquals("Test Image Alt from DAM", productPhotoGallery.getMainImage().getAltText());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/productPhotoGalleryAuthored");
        ProductPhotoGallery productPhotoGallery = ctx.request().adaptTo(ProductPhotoGallery.class);
        assertTrue(productPhotoGallery.hasContent());
    }
}
