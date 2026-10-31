import { useState, useEffect, useCallback } from 'react';
import { checkSystemHealth } from '../services/api';

export function useHealthCheck(pollIntervalMs = 15000) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastChecked, setLastChecked] = useState(null);

  const fetchHealth = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const res = await checkSystemHealth();
      if (res && res.success) {
        setData(res.data);
      } else {
        setError(res?.message || 'Unrecognized health payload');
      }
    } catch (err) {
      setError(err.message || 'Unable to connect to backend service');
    } finally {
      setLoading(false);
      setLastChecked(new Date());
    }
  }, []);

  useEffect(() => {
    fetchHealth();
    if (pollIntervalMs > 0) {
      const interval = setInterval(fetchHealth, pollIntervalMs);
      return () => clearInterval(interval);
    }
  }, [fetchHealth, pollIntervalMs]);

  return { data, loading, error, lastChecked, refetch: fetchHealth };
}

