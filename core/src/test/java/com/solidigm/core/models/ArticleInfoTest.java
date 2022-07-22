package com.solidigm.core.models;

import com.solidigm.core.beans.Link;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class ArticleInfoTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() {
        //Load ctx for ArticleList Model
        ctx.addModelsForClasses(ArticleInfo.class);
        ctx.load().json("/com/solidigm/core/models/ArticleInfoTest.json", "/content");
    }

    @Test
    void testGetDate(){
        String expected = "July 5, 2022";
        ctx.currentResource("/content/articlePage/jcr:content/root/container/articleinfo");
        ArticleInfo articleInfo = ctx.request().adaptTo(ArticleInfo.class);
        assertEquals(expected, articleInfo.getDate());
    }

    @Test
    void testGetAuthor(){
        String expected = "Test Author";
        ctx.currentResource("/content/articlePage/jcr:content/root/container/articleinfo");
        ArticleInfo articleInfo = ctx.request().adaptTo(ArticleInfo.class);
        assertEquals(expected, articleInfo.getAuthor());
    }

    @Test
    void testGetTags(){
        ctx.currentResource("/content/articlePage/jcr:content/root/container/articleinfo");
        ArticleInfo articleInfo = ctx.request().adaptTo(ArticleInfo.class);
        List<Link> actual = articleInfo.getTags();
        assertEquals(2, actual.size());
        assertEquals("Products", actual.get(0).getLabel());
        assertEquals("/content/tagged/articles/page.html?tag=solidigm%3Aproducts", actual.get(0).getLink());
        assertEquals("Intel", actual.get(1).getLabel());
        assertEquals("/content/tagged/articles/page.html?tag=solidigm%3Aproducts%2Fintel", actual.get(1).getLink());
    }

    @Test
    void testGetTagsEmpty(){
        ctx.currentResource("/content/articlePage/jcr:content/root/container/articleinfoempty");
        ArticleInfo articleInfo = ctx.request().adaptTo(ArticleInfo.class);
        List<Link> actual = articleInfo.getTags();
        assertNull(actual);
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/articleinfo";
        ctx.currentResource("/content/articlePage/jcr:content/root/container/articleinfo");
        ArticleInfo articleInfo = ctx.request().adaptTo(ArticleInfo.class);
        assertEquals(expected, articleInfo.getExportedType());
    }
}
