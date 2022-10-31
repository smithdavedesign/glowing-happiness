package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

public class VariationSortable {
    @Getter
    @Setter
    private ProductVariation variation;

    @Getter
    @Setter
    private int rawCapacity;

    public VariationSortable(ProductVariation variation, int rawCapacity) {
        this.variation = variation;
        this.rawCapacity = rawCapacity;
    }

    public static class SortVariations implements Comparator<VariationSortable> {
        public int compare(VariationSortable a, VariationSortable b)
        {
            return b.getRawCapacity() - a.getRawCapacity();
        }
    }
}
