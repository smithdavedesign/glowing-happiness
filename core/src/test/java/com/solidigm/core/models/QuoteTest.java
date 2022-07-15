package com.solidigm.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class QuoteTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        //Load ctx for QuoteModel
        ctx.addModelsForClasses(Quote.class);
        ctx.load().json("/com/solidigm/core/models/QuoteTest.json", "/content");
    }

    @Test
    void testGetExportedType() {
        String expected = "solidigm/components/quote";
        ctx.currentResource("/content/quoteEmpty");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertEquals(expected, quote.getExportedType());
    }

    @Test
    void testGetHasContentEmpty() {
        ctx.currentResource("/content/quoteEmpty");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertFalse(quote.hasContent());
    }

    @Test
    void testGetQuote() {
        String expected = "Test Quote";
        ctx.currentResource("/content/quoteAuthored");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertEquals(expected, quote.getQuote());
    }

    @Test
    void testGetName() {
        String expected = "Test Name";
        ctx.currentResource("/content/quoteAuthored");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertEquals(expected, quote.getName());
    }

    @Test
    void testGetTitle() {
        String expected = "Test Title";
        ctx.currentResource("/content/quoteAuthored");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertEquals(expected, quote.getTitle());
    }

    @Test
    void testGetHasContentAuthored() {
        ctx.currentResource("/content/quoteAuthored");
        Quote quote = ctx.request().adaptTo(Quote.class);
        assertTrue(quote.hasContent());
    }
}
