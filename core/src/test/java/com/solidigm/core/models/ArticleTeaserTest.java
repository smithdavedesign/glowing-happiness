package com.solidigm.core.models;

import com.solidigm.core.beans.ArticlePage;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class ArticleTeaserTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() {
        //Load ctx for ArticleTeaser Model
        ctx.addModelsForClasses(ArticleTeaser.class);
        ctx.load().json("/com/solidigm/core/models/ArticleTeaserTest.json", "/content");
    }

    @Test
    void testHasContentEmpty() {
        ctx.currentResource("/content/articleTeaserEmpty");
        ArticleTeaser articleTeaser = ctx.request().adaptTo(ArticleTeaser.class);
        assertFalse(articleTeaser.hasContent());
    }

    @Test
    void testGetArticlesAuthored() {
        ctx.currentResource("/content/articleTeaserAuthored");
        ArticleTeaser articleTeaser = ctx.request().adaptTo(ArticleTeaser.class);
        ArticlePage actual = articleTeaser.getArticle();
        assertEquals("Test Page Title", actual.getArticleTitle());
        assertEquals("Test Teaser Image", actual.getTeaserImage());
        assertEquals("Alt Text", actual.getTeaserImageAltText());
        assertEquals("Products", actual.getTeaserCategory());
        assertEquals("/content/article", actual.getPath());
        assertEquals("Test Description", actual.getDescription());
    }

    @Test
    void testHasContentAuthored(){
        ctx.currentResource("/content/articleTeaserAuthored");
        ArticleTeaser articleTeaser = ctx.request().adaptTo(ArticleTeaser.class);
        assertTrue(articleTeaser.hasContent());
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/articleteaser";
        ctx.currentResource("/content/articleTeaserEmpty");
        ArticleTeaser articleTeaser = ctx.request().adaptTo(ArticleTeaser.class);
        assertEquals(expected, articleTeaser.getExportedType());
    }
}
