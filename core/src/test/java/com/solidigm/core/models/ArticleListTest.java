package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.solidigm.core.beans.ArticlePage;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(AemContextExtension.class)
public class ArticleListTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() {
        //Load ctx for ArticleList Model
        ctx.addModelsForClasses(ArticleList.class);
        ctx.load().json("/com/solidigm/core/models/ArticleListTest.json", "/content");
    }

    @Test
    void testGetTitleEmpty() {
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertTrue(StringUtils.isEmpty(articleList.getTitle()));
    }

    @Test
    void testGetLinkTextEmpty() {
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertTrue(StringUtils.isEmpty(articleList.getLinkText()));
    }

    @Test
    void testGetLinkEmpty() {
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertTrue(StringUtils.isEmpty(articleList.getLink()));
    }

    @Test
    void testGetArticlesEmpty() {
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertTrue(articleList.getArticles().isEmpty());
    }

    @Test
    void testHasContentEmpty() {
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertFalse(articleList.hasContent());
    }

    @Test
    void testGetTitleAuthored() {
        String expected = "Test Title";
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertEquals(expected, articleList.getTitle());
    }

    @Test
    void testGetLinkTextAuthored() {
        String expected = "Test Link Text";
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertEquals(expected, articleList.getLinkText());
    }

    @Test
    void testGetLinkAuthored() {
        String expected = "/test/link";
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertEquals(expected, articleList.getLink());
    }

    @Test
    void testGetArticlesAuthored() {
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        List<ArticlePage> actual = articleList.getArticles();
        assertEquals(2, actual.size());
        assertEquals("Test Page Title 1", actual.get(0).getArticleTitle());
        assertEquals("Test Teaser Image 1", actual.get(0).getTeaserImage());
        assertEquals("Alt Text 1", actual.get(0).getTeaserImageAltText());
        assertEquals("Products", actual.get(0).getTeaserCategory());
        assertEquals("/content/article1", actual.get(0).getPath());

        assertEquals("Test Page Title 2", actual.get(1).getArticleTitle());
        assertEquals("Test Teaser Image 2", actual.get(1).getTeaserImage());
        assertEquals("Alt Text 2", actual.get(1).getTeaserImageAltText());
        assertEquals("Innovation", actual.get(1).getTeaserCategory());
        assertEquals("/content/article2", actual.get(1).getPath());
    }

    @Test
    void testHasContentAuthored(){
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertTrue(articleList.hasContent());
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/articlelist";
        ctx.currentResource("/content/articleListEmpty");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        assertEquals(expected, articleList.getExportedType());
    }
}
