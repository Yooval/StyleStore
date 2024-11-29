package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Cart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This file implements the Cart interface, managing product operations.

public class DefaultCart implements Cart {

    private Map<Product, Integer> productMap;

    public DefaultCart() {
        productMap = new HashMap<>();
    }

    @Override
    public void addProduct(Product product) {
        if (product.getType() == ProductType.COMPOSITE) {
            for (Product subProduct : product.getSubProducts()) {
                addProduct(subProduct); // Add all sub-products individually
            }
        } else {
            productMap.put(product, productMap.getOrDefault(product, 0) + 1);
        }
    }

    @Override
    public boolean removeProduct(Product product) {
        if (product.getType() == ProductType.COMPOSITE) {
            boolean allRemoved = true;
            for (Product subProduct : product.getSubProducts()) {
                allRemoved &= removeProduct(subProduct); // Remove all sub-products
            }
            return allRemoved;
        } else {
            if (productMap.containsKey(product)) {
                int quantity = productMap.get(product);
                if (quantity > 1) {
                    productMap.put(product, quantity - 1);
                } else {
                    productMap.remove(product);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        productMap.clear();
    }

    @Override
    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                productList.add(entry.getKey());
            }
        }
        return productList;
    }

    @Override
    public double getTotalBill() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (product.getType() == ProductType.COMPOSITE) {
                total += product.calculateTotalPrice() * quantity; // Use composite price
            } else {
                total += product.getPrice() * quantity;
            }
        }
        return total;
    }

    @Override
    public boolean containsProduct(Product product) {
        if (product.getType() == ProductType.COMPOSITE) {
            for (Product subProduct : product.getSubProducts()) {
                if (!containsProduct(subProduct)) {
                    return false;
                }
            }
            return true;
        } else {
            return productMap.containsKey(product);
        }
    }

    @Override
    public int getProductQuantity(Product product) {
        if (product.getType() == ProductType.COMPOSITE) {
            int totalQuantity = 0;
            for (Product subProduct : product.getSubProducts()) {
                totalQuantity += getProductQuantity(subProduct);
            }
            return totalQuantity;
        } else {
            return productMap.getOrDefault(product, 0);
        }
    }

    @Override
    public boolean isEmpty() {
        return productMap.isEmpty();
    }
}
