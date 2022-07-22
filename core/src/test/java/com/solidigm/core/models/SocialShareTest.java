package com.solidigm.core.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.solidigm.core.services.SocialService;
import com.solidigm.core.services.impl.SocialServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This unit test does not test the query performed by this component, to validate query logic got to:
// (AEM)/libs/cq/search/content/querydebug.html
@ExtendWith(AemContextExtension.class)
public class SocialShareTest {
    private final AemContext ctx = new AemContext();

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private Query query;

    @Mock
    private SearchResult searchResult;

    @Mock
    private Hit firstHit;

    @Mock
    private Hit secondHit;

    private AutoCloseable closeable;

    private List<Hit> queryHits;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        //Initialize variables
        closeable = MockitoAnnotations.openMocks(this);
        queryHits = new ArrayList<>();

        //Set up Mocks
        Mockito.when(queryBuilder.createQuery(any(PredicateGroup.class), eq(null))).thenReturn(query);
        Mockito.when(query.getResult()).thenReturn(searchResult);
        Mockito.when(searchResult.getHits()).thenReturn(queryHits);
        Mockito.when(firstHit.getPath()).thenReturn("/content/solidigm/articlePage1");
        Mockito.when(secondHit.getPath()).thenReturn("/content/solidigm/articlePage2");
        ctx.registerService(QueryBuilder.class, queryBuilder);

        //Configure and register SocialService
        Map<String, Object> socialConfig = new HashMap<>();
        socialConfig.put("getTwitterUrl", "https://twitter.com/test?page=");
        socialConfig.put("getLinkedInUrl", "https://linkedin.com/test?page=");
        ctx.registerInjectActivateService(new SocialServiceImpl(), socialConfig);

        //Load ctx for ArticleList Model
        ctx.addModelsForClasses(SocialShare.class);
        ctx.load().json("/com/solidigm/core/models/SocialShareTest.json", "/content/solidigm");
    }

    @AfterEach
    public void cleanUp() throws Exception {
        //Close mocks before next test
        closeable.close();
    }

    @Test
    public void testTwoHits() {
        queryHits.add(firstHit);
        queryHits.add(secondHit);
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertEquals("/content/solidigm/articlePage2", socialShare.getPrevious());
        assertEquals("/content/solidigm/articlePage2", socialShare.getNext());
    }

    @Test
    public void testSelfHit() {
        queryHits.add(firstHit);
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertTrue(StringUtils.isEmpty(socialShare.getNext()));
        assertTrue(StringUtils.isEmpty(socialShare.getPrevious()));
    }

    @Test
    public void testNoHit() {
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertTrue(StringUtils.isEmpty(socialShare.getNext()));
        assertTrue(StringUtils.isEmpty(socialShare.getPrevious()));
    }

    @Test
    public void testTwitterShare(){
        String expected = "https://twitter.com/test?page=http%3A%2F%2Flocalhost%2F";
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertEquals(expected, socialShare.getTwitterShare());
    }

    @Test
    public void testLinkedInShare(){
        String expected = "https://linkedin.com/test?page=http%3A%2F%2Flocalhost%2F";
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertEquals(expected, socialShare.getLinkedInShare());
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/socialshare";
        ctx.currentResource("/content/solidigm/articlePage1/jcr:content/root/container/socialshare");
        SocialShare socialShare = ctx.request().adaptTo(SocialShare.class);
        assertEquals(expected, socialShare.getExportedType());
    }
}
