package com.receivingyard.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Validates quantity variance between Purchase Orders and physical Goods Receipts.
 * Enforces manufacturing rule: Quantity tolerance boundaries are strictly inclusive.
 */
public final class ToleranceCalculator {

    private ToleranceCalculator() {
        // Utility class
    }

    /**
     * Checks if received quantity is within [ordered * (1 - tolerance/100), ordered * (1 + tolerance/100)].
     * Boundaries are INCLUSIVE.
     *
     * @param orderedQuantity  quantity contracted on PO line
     * @param receivedQuantity quantity actually delivered and weighed at gate
     * @param tolerancePercent allowed tolerance percentage (e.g. 5.0 for ±5.0%)
     * @return true if receivedQuantity is within inclusive allowable bounds
     */
    public static boolean isWithinTolerance(BigDecimal orderedQuantity, BigDecimal receivedQuantity, BigDecimal tolerancePercent) {
        if (orderedQuantity == null || receivedQuantity == null || tolerancePercent == null) {
            return false;
        }

        BigDecimal toleranceFraction = tolerancePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal minAllowed = orderedQuantity.multiply(BigDecimal.ONE.subtract(toleranceFraction));
        BigDecimal maxAllowed = orderedQuantity.multiply(BigDecimal.ONE.add(toleranceFraction));

        // Inclusive check: minAllowed <= receivedQuantity <= maxAllowed
        return receivedQuantity.compareTo(minAllowed) >= 0 && receivedQuantity.compareTo(maxAllowed) <= 0;
    }

    /**
     * Calculates the minimum acceptable quantity (inclusive).
     */
    public static BigDecimal calculateMinAllowed(BigDecimal orderedQuantity, BigDecimal tolerancePercent) {
        BigDecimal toleranceFraction = tolerancePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        return orderedQuantity.multiply(BigDecimal.ONE.subtract(toleranceFraction)).setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the maximum acceptable quantity (inclusive).
     */
    public static BigDecimal calculateMaxAllowed(BigDecimal orderedQuantity, BigDecimal tolerancePercent) {
        BigDecimal toleranceFraction = tolerancePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        return orderedQuantity.multiply(BigDecimal.ONE.add(toleranceFraction)).setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * Checks if a measured quality test value falls within inclusive min and max limits.
     */
    public static boolean isSpecificationCompliant(BigDecimal measured, BigDecimal minLimit, BigDecimal maxLimit) {
        if (measured == null) {
            return false;
        }
        if (minLimit != null && measured.compareTo(minLimit) < 0) {
            return false; // Below lower inclusive bound
        }
        if (maxLimit != null && measured.compareTo(maxLimit) > 0) {
            return false; // Above upper inclusive bound
        }
        return true;
    }
}

