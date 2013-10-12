package org.indy256.calc;

import java.math.BigInteger;
import java.util.TreeMap;

public final class Functions {

	public static double partitions(double a) {
		if (a > 10000) {
			return Double.POSITIVE_INFINITY;
		}
		int n = (int) a;
		double[] p = new double[n + 1];
		p[0] = 1;
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= n - i; j++) {
				p[j + i] += p[j];
			}
		}
		return p[n];
	}

	public static double factorial(double a) {
		long n = (long) a;
		if (n > 1000) {
			return Double.POSITIVE_INFINITY;
		}
		double res = 1;
		for (; n > 1; n--) {
			res *= n;
		}
		return res;
	}

	public static TreeMap<Long, Integer> factorize(double v) {
		long n = (long) v;
		TreeMap<Long, Integer> factors = new TreeMap<Long, Integer>();
		n = Math.abs(n);
		for (long d = 2; n > 1; ) {
			int cnt = 0;
			while (n % d == 0) {
				cnt++;
				n /= d;
			}
			if (cnt > 0) {
				factors.put(d, cnt);
			}
			d += (d == 2) ? 1 : 2;
			if (d * d > n) {
				d = n;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (long key : factors.keySet()) {
			int value = factors.get(key);
			if (value == 1) {
				sb.append(key + " * ");
			} else {
				sb.append(key + "^" + value + " * ");
			}
		}
		if (sb.length() == 0) {
			sb.append("1");
		} else {
			sb.delete(sb.length() - 2, sb.length());
		}
		throw new RuntimeException(sb.toString());
	}

	public static void isPrime(double a) {
		throw new RuntimeException("" + BigInteger.valueOf((long) a).isProbablePrime(100));
	}

	public static double nextPrime(double a) {
		return BigInteger.valueOf((long) a).nextProbablePrime().doubleValue();
	}

	public static double gcd(double x, double y) {
		long a = (long) x;
		long b = (long) y;
		return b == 0 ? Math.abs(a) : gcd(b, a % b);
	}

	static long gcd2(long a, long b) {
		return b == 0 ? Math.abs(a) : gcd2(b, a % b);
	}

	public static double lcm(double x, double y) {
		long a = (long) x;
		long b = (long) y;
		return Math.abs(a / gcd(a, b) * b);
	}

	public static double binomial(double a, double b) {
		long n = (long) a;
		long m = (long) b;
		if (n < m || n < 0 || m < 0) {
			return 0;
		}
		long res = 1;
		for (long i = 0; i < Math.min(m, n - m); i++) {
			res = res / gcd2(res, i + 1) * ((n - i) / ((i + 1) / gcd2(res, i + 1)));
			// res = res * (n - i) / (i + 1);
			if (res < 0) {
				return Double.POSITIVE_INFINITY;
			}
		}
		return res;
	}

	public static double catalan(double v) {
		int n = (int) v;
		return binomial(2 * n, n) - binomial(2 * n, n - 1);
	}
}
