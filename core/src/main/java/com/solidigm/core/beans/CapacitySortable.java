package com.solidigm.core.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

public class CapacitySortable {
    @Getter
    @Setter
    private int rawCapacity;

    @Getter
    @Setter
    private String capacity;

    public CapacitySortable(int rawCapacity, String capacity) {
        this.rawCapacity = rawCapacity;
        this.capacity = capacity;
    }

    public static class SortCapacities implements Comparator<CapacitySortable> {
        public int compare(CapacitySortable a, CapacitySortable b)
        {
            return a.getRawCapacity() - b.getRawCapacity();
        }
    }
}
