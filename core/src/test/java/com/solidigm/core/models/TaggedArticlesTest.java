package com.solidigm.core.models;

import com.day.cq.tagging.Tag;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
public class TaggedArticlesTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() {
        //Load ctx for TaggedArticles Model
        ctx.addModelsForClasses(TaggedArticles.class);
        ctx.load().json("/com/solidigm/core/models/TaggedArticlesTest.json", "/content");
    }

    @Test
    void testGetSelectorTag() {
        String expected = "solidigm:articles";
        ctx.currentResource("/content/solidigm/us/en/taggedArticles");
        ctx.requestPathInfo().setSelectorString("solidigm%3Aarticles");
        TaggedArticles taggedArticles = ctx.request().adaptTo(TaggedArticles.class);
        assertEquals(expected, taggedArticles.getSelectorTag());
    }

    @Test
    void testGetDropdownTags() {
        ctx.currentResource("/content/solidigm/us/en/taggedArticles");
        TaggedArticles taggedArticles = ctx.request().adaptTo(TaggedArticles.class);
        List<Tag> actual = taggedArticles.getDropdownTags();
        assertEquals(2, actual.size());
        assertEquals("Innovation", actual.get(0).getTitle());
        assertEquals("solidigm:articles/categories/innovation", actual.get(0).getTagID());
        assertEquals("Products", actual.get(1).getTitle());
        assertEquals("solidigm:articles/categories/products", actual.get(1).getTagID());
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/taggedarticles";
        ctx.currentResource("/content/solidigm/us/en/taggedArticles");
        TaggedArticles taggedArticles = ctx.request().adaptTo(TaggedArticles.class);
        assertEquals(expected, taggedArticles.getExportedType());
    }
}
