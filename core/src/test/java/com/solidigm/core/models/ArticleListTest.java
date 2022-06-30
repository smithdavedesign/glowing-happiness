package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.solidigm.core.beans.ArticlePage;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
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
        List<ArticlePage> expected = new ArrayList<>();
        expected.add(
                new ArticlePage(
                        "Test Page Title 1",
                        "Test Teaser Image 1",
                        "Test Teaser Category 1",
                        "/content/article1"
                )
        );
        expected.add(
                new ArticlePage(
                        "Test Page Title 2",
                        "Test Teaser Image 2",
                        "Test Teaser Category 2",
                        "/content/article2"
                )
        );
        ctx.currentResource("/content/articleListAuthored");
        ArticleList articleList = ctx.request().adaptTo(ArticleList.class);
        List<ArticlePage> actual = articleList.getArticles();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++ ) {
            assertEquals(expected.get(i).getTitle(), actual.get(i).getTitle());
            assertEquals(expected.get(i).getImage(), actual.get(i).getImage());
            assertEquals(expected.get(i).getCategory(), actual.get(i).getCategory());
            assertEquals(expected.get(i).getPath(), actual.get(i).getPath());
        }
    }

    @Test
    void testNoContentAuthored(){
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
