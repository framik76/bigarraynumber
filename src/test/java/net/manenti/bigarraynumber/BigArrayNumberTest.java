package net.manenti.bigarraynumber;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class BigArrayNumberTest {

    @Test
    public void test_zero() {
        var zero = new BigArrayNumber(0);
        Assert.assertTrue(Arrays.equals(new int[]{0}, zero.getDigits()));
    }

    @Test
    public void test_2() {
        var number = new BigArrayNumber(2);
        Assert.assertTrue(Arrays.equals(new int[]{2}, number.getDigits()));
    }

    @Test
    public void test_15() {
        var number = new BigArrayNumber(15);
        Assert.assertTrue(Arrays.equals(new int[]{5, 1}, number.getDigits()));
    }

    @Test
    public void test_100() {
        var number = new BigArrayNumber(100);
        Assert.assertTrue(Arrays.equals(new int[]{0, 0, 1}, number.getDigits()));
    }

    @Test
    public void test_15_bigArrayNumber() {
        var number = new BigArrayNumber(15);
        var number2 = new BigArrayNumber(new int[]{5, 1});
        Assert.assertTrue(Arrays.equals(number.getDigits(), number2.getDigits()));
    }

    @Test
    public void test_leading_zeros() {
        BigArrayNumber n = new BigArrayNumber(new int[]{5,1,0,0});
        BigArrayNumber n1 = new BigArrayNumber(new int[]{5,1});
        Assert.assertTrue(Arrays.equals(n.getDigits(), n1.getDigits()));
    }

    @Test
    public void test_digit_gt9_throws() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new BigArrayNumber(new int[]{10}));
    }

    @Test
    public void test_digit_lt0_throws() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new BigArrayNumber(new int[]{-1}));
    }

    @Test
    public void test_multiply() {
        BigArrayNumber first  = new BigArrayNumber(new int[]{5,1});
        BigArrayNumber second = new BigArrayNumber(new int[]{2});
        BigArrayNumber result = first.multiply(second);

        Assert.assertTrue(Arrays.equals(new int[]{0, 3}, result.getDigits()));
    }

    @Test
    public void test_multiply2() {
        BigArrayNumber first  = new BigArrayNumber(new int[]{5,1});
        BigArrayNumber second = new BigArrayNumber(new int[]{0,1});
        BigArrayNumber result = first.multiply(second);

        Assert.assertTrue(Arrays.equals(new int[]{0, 5, 1}, result.getDigits()));
    }

}