package com.solidigm.core.models;

import com.solidigm.core.beans.Link;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(AemContextExtension.class)
public class EventInfoTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() {
        //Load ctx for EventInfo Model
        ctx.addModelsForClasses(EventInfo.class);
        ctx.load().json("/com/solidigm/core/models/EventInfoTest.json", "/content");
    }

    @Test
    void testGetDate(){
        String expected = "2022-07-05";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getDate());
    }

    @Test
    void testGetPostedDate(){
        String expected = "July 5, 2022";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getPostedDate());
    }

    @Test
    void testGetStartDate(){
        String expected = "July 6, 2022";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getStartDate());
    }

    @Test
    void testGetEndDate(){
        String expected = "July 7, 2022";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getEndDate());
    }

    @Test
    void testGetStartTime(){
        String expected = "6:30 AM";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getStartTime());
    }

    @Test
    void testGetEndTime(){
        String expected = "10:00 PM";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getEndTime());
    }

    @Test
    void testGetLocationTime(){
        String expected = "Ann Arbor";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getLocation());
    }

    @Test
    void testGetEventInfoTime(){
        String expected = "Test Information";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getEventInfo());
    }

    @Test
    void testGetTags(){
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        List<Link> actual = eventInfo.getTags();
        assertEquals(2, actual.size());
        assertEquals("Products", actual.get(0).getLabel());
        assertEquals("/content/tagged/articles/page.solidigm%3Aproducts.html", actual.get(0).getLink());
        assertEquals("Intel", actual.get(1).getLabel());
        assertEquals("/content/tagged/articles/page.solidigm%3Aproducts--intel.html", actual.get(1).getLink());
    }

    @Test
    void testGetTagsEmpty(){
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfoempty");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        List<Link> actual = eventInfo.getTags();
        assertNull(actual);
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/eventinfo";
        ctx.currentResource("/content/eventPage/jcr:content/root/container/eventinfo");
        EventInfo eventInfo = ctx.request().adaptTo(EventInfo.class);
        assertEquals(expected, eventInfo.getExportedType());
    }
}
