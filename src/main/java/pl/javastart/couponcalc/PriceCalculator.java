package pl.javastart.couponcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PriceCalculator {

    public double calculatePrice(List<Product> products, List<Coupon> coupons) {
        double sum;
        if (products == null) {
            return 0;
        }
        if (coupons == null) {
            sum = calculateSumForProducts(products, null);
        } else if (coupons.size() == 1) {
            Coupon coupon = coupons.get(0);
            sum = calculateSumForProducts(products, coupon);
            if (coupon.getCategory() == null) {
                sum *= convertDiscountToDecimal(coupon);
            }
        } else {
            Coupon biggestDiscount = getBiggestDiscount(products, coupons);
            sum = calculateSumForProducts(products, biggestDiscount);
        }
        return getRoundedValue(sum);
    }

    private static double getRoundedValue(double sum) {
        BigDecimal roundedValue = BigDecimal.valueOf(sum)
                .setScale(2, RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }

    private static double convertDiscountToDecimal(Coupon coupon) {
        return 1 - (coupon.getDiscountValueInPercents() / 100.0);
    }

    private static double calculateSumForProducts(List<Product> products, Coupon coupon) {
        double sum = 0;

        for (Product product : products) {
            if (coupon == null || coupon.getCategory() == null) {
                sum += product.getPrice();
            } else {
                if (product.getCategory().equals(coupon.getCategory())) {
                    sum += product.getPrice() * convertDiscountToDecimal(coupon);
                } else {
                    sum += product.getPrice();
                }
            }
        }
        return sum;
    }

    private static Coupon getBiggestDiscount(List<Product> products, List<Coupon> coupons) {
        Coupon result = null;
        double minimum = 0;
        double discount = 0;
        for (Coupon coupon : coupons) {
            for (Product product : products) {
                if (product.getCategory().equals(coupon.getCategory())) {
                    discount += product.getPrice() * (1 - (coupon.getDiscountValueInPercents() / 100.0));
                }
            }
            if (discount > minimum) {
                minimum = discount;
                result = coupon;
            }
        }
        return result;
    }

}