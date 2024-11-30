package com.itbulls.learnit.javacore.oop.exam.onlineshop.factories.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultUser;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.factories.UserFactory;

// This file is the concrete implementation of UserFactory, creating DefaultUser and Seller instances.
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createUser(UserRole role, String firstName, String lastName, String email, String password) {
        if (role == UserRole.SELLER) {
            return new Seller(firstName, lastName, email, password);
        } else {
            return new DefaultUser(firstName, lastName, password, email, role);
        }
    }
}
