/**
 * UI Formatting Utilities
 */

export function formatDateTime(isoString) {
  if (!isoString) return 'N/A';
  try {
    const d = new Date(isoString);
    return d.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  } catch {
    return isoString;
  }
}

export function formatSecondsToUptime(seconds) {
  if (!seconds || seconds <= 0) return '0s';
  const hrs = Math.floor(seconds / 3600);
  const mins = Math.floor((seconds % 3600) / 60);
  const secs = Math.floor(seconds % 60);

  const parts = [];
  if (hrs > 0) parts.push(`${hrs}h`);
  if (mins > 0 || hrs > 0) parts.push(`${mins}m`);
  parts.push(`${secs}s`);
  return parts.join(' ');
}

export function evaluateTolerance(ordered, received, tolerancePercent = 5.0) {
  const ord = parseFloat(ordered);
  const rec = parseFloat(received);
  const tol = parseFloat(tolerancePercent);

  if (isNaN(ord) || isNaN(rec) || isNaN(tol) || ord <= 0) {
    return { status: 'INVALID', label: 'Invalid inputs', min: 0, max: 0, variance: 0 };
  }

  const fraction = tol / 100.0;
  const minAllowed = ord * (1.0 - fraction);
  const maxAllowed = ord * (1.0 + fraction);
  const variance = ((rec - ord) / ord) * 100.0;

  // Inclusive check
  const isWithin = rec >= minAllowed && rec <= maxAllowed;

  return {
    isWithin,
    minAllowed: minAllowed.toFixed(2),
    maxAllowed: maxAllowed.toFixed(2),
    variancePercent: variance.toFixed(2),
    status: isWithin ? 'PASS' : rec < minAllowed ? 'SHORT_DELIVERY' : 'OVER_DELIVERY',
    label: isWithin 
      ? `Within Allowed Bracket [${minAllowed.toFixed(2)} - ${maxAllowed.toFixed(2)}]`
      : rec < minAllowed 
        ? `Under-delivery below -${tol}% minimum (${minAllowed.toFixed(2)})`
        : `Over-delivery above +${tol}% maximum (${maxAllowed.toFixed(2)})`
  };
}

