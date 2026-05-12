package net.manenti.bigarraynumber;

import java.util.Arrays;

/**
 * Represents an arbitrary-precision non-negative integer as an array of single digits.
 *
 * Storage convention:
 *   index 0 → least-significant digit  (units)
 *   index 1 → next digit               (tens)
 *   …
 *
 * Example: 15  →  [5, 1]
 *          30  →  [0, 3]
 */

public class BigArrayNumber {
    private final int[] digits;

    /** Build from a trimmed digit array (least-significant first). */
    public BigArrayNumber(int[] digits) {
        if (digits == null || digits.length == 0) {
            this.digits = new int[]{0};
        } else {
            this.digits = trimLeadingZeros(digits.clone());
        }
        validateDigits(this.digits);
    }

    public BigArrayNumber(long value) {
        if (value < 0) throw new IllegalArgumentException("Only non-negative integers are supported.");
        if (value == 0) {
            this.digits = new int[]{0};
        } else {
            // Count digits
            long tmp = value;
            int len = 0;
            while (tmp > 0) { tmp /= 10; len++; }

            int[] d = new int[len];
            tmp = value;
            for (int i = 0; i < len; i++) {
                d[i] = (int)(tmp % 10);   // least-significant first
                tmp /= 10;
            }
            this.digits = d;
        }
    }

    /**
     * Returns the digit array (least-significant digit first, each in [0,9]).
     * The returned array is a defensive copy.
     */
    public int[] getDigits() {
        return digits.clone();
    }

    /**
     * Returns the number of digits.
     */
    public int length() {
        return digits.length;
    }

    /**
     * Returns a NEW BigArrayNumber = this + other.
     * Addition is performed digit-by-digit using only the + operator
     * between single digits [0,9] (plus a carry of 0 or 1).
     */
    public BigArrayNumber add(BigArrayNumber other) {
        int maxLen = Math.max(this.digits.length, other.digits.length);
        // +1 in case of final carry
        int[] result = new int[maxLen + 1];

        int carry = 0;
        for (int i = 0; i < maxLen + 1; i++) {
            int a = (i < this.digits.length)  ? this.digits[i]  : 0;
            int b = (i < other.digits.length) ? other.digits[i] : 0;

            // a, b, carry are all single-digit values [0,9] or carry [0,1]
            int sum = a + b + carry;   // only + is used; sum ≤ 19
            result[i] = sum % 10;      // store the unit digit
            carry      = sum / 10;     // propagate the carry (0 or 1)
        }

        return new BigArrayNumber(trimLeadingZeros(result));
    }

    /**
     * Returns a NEW BigArrayNumber = this × other.
     *
     * Multiplication is implemented purely through repeated addition.
     * To keep things tractable for large multipliers we use the standard
     * "shift-and-add" approach (analogous to long multiplication):
     *
     *   a × b  =  a × b₀×10⁰  +  a × b₁×10¹  +  …
     *
     * where each partial product  a × bᵢ  is computed by adding `a`
     * exactly bᵢ times (bᵢ is a single digit, so at most 9 additions).
     * The shift (×10ⁱ) is applied by prepending i zeros to the digit array.
     *
     * This means multiplication ONLY ever calls add() internally.
     */
    public BigArrayNumber multiply(BigArrayNumber other) {
        BigArrayNumber result = new BigArrayNumber(0);

        for (int i = 0; i < other.digits.length; i++) {
            int digit = other.digits[i]; // a single digit [0,9]

            // Partial product: this × digit  (via repeated addition, at most 9 times)
            BigArrayNumber partial = new BigArrayNumber(0);
            for (int j = 0; j < digit; j++) {
                partial = partial.add(this);
            }

            // Shift partial left by i positions (multiply by 10^i)
            BigArrayNumber shifted = shiftLeft(partial, i);

            // Accumulate into result
            result = result.add(shifted);
        }

        return result;
    }

    /** Prepend `positions` zero-digits (= multiply by 10^positions). */
    private static BigArrayNumber shiftLeft(BigArrayNumber n, int positions) {
        if (positions == 0) return n;
        // Is n == 0? Then shifting is still 0.
        if (n.digits.length == 1 && n.digits[0] == 0) return n;

        int[] shifted = new int[n.digits.length + positions];
        // Fill the low positions with zeros
        for (int i = 0; i < positions; i++) {
            shifted[i] = 0;
        }
        // Copy original digits starting at `positions`
        System.arraycopy(n.digits, 0, shifted, positions, n.digits.length);
        return new BigArrayNumber(shifted);
    }

    /** Remove leading zeros (remembering that index 0 is the *least* significant). */
    private static int[] trimLeadingZeros(int[] d) {
        int last = d.length - 1;
        while (last > 0 && d[last] == 0) {
            last--;
        }
        if (last == d.length - 1) return d;
        return Arrays.copyOf(d, last + 1);
    }

    /** Sanity-check: every element must be a single digit. */
    private static void validateDigits(int[] d) {
        for (int i = 0; i < d.length; i++) {
            if (d[i] < 0 || d[i] > 9) {
                throw new IllegalArgumentException(
                        "Digit at index " + i + " is " + d[i] + " – must be in [0, 9].");
            }
        }
    }
}
