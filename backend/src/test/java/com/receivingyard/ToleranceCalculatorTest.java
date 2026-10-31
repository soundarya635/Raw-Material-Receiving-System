package com.receivingyard;

import com.receivingyard.validation.ToleranceCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ToleranceCalculatorTest {

    @Test
    @DisplayName("Should validate inclusive tolerance boundaries for ±5%")
    void testToleranceBoundaries() {
        BigDecimal ordered = BigDecimal.valueOf(1000.0);
        BigDecimal tolerance = BigDecimal.valueOf(5.0);

        // Lower boundary inclusive: exactly 950.0 should PASS
        assertTrue(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(950.0), tolerance));

        // Upper boundary inclusive: exactly 1050.0 should PASS
        assertTrue(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(1050.0), tolerance));

        // Inside nominal: 1000.0 should PASS
        assertTrue(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(1000.0), tolerance));
        assertTrue(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(985.5), tolerance));
        assertTrue(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(1020.0), tolerance));

        // Outside lower boundary: 949.99 should FAIL
        assertFalse(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(949.99), tolerance));

        // Outside upper boundary: 1050.01 should FAIL
        assertFalse(ToleranceCalculator.isWithinTolerance(ordered, BigDecimal.valueOf(1050.01), tolerance));
    }

    @Test
    @DisplayName("Should validate inclusive min/max limits for quality specifications")
    void testSpecificationInclusiveLimits() {
        BigDecimal min = BigDecimal.valueOf(10.0);
        BigDecimal max = BigDecimal.valueOf(20.0);

        // Exactly on boundaries (inclusive)
        assertTrue(ToleranceCalculator.isSpecificationCompliant(BigDecimal.valueOf(10.0), min, max));
        assertTrue(ToleranceCalculator.isSpecificationCompliant(BigDecimal.valueOf(20.0), min, max));
        assertTrue(ToleranceCalculator.isSpecificationCompliant(BigDecimal.valueOf(15.5), min, max));

        // Violations
        assertFalse(ToleranceCalculator.isSpecificationCompliant(BigDecimal.valueOf(9.999), min, max));
        assertFalse(ToleranceCalculator.isSpecificationCompliant(BigDecimal.valueOf(20.001), min, max));
    }
}

