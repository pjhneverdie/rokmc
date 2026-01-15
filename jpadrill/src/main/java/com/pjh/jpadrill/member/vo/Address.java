package com.pjh.jpadrill.member.vo;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String city;

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String street;

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String zipcode;

    private Address(String city, String street, String zipcode) {
        Assert.notNull(city, "City must not be null");
        Assert.hasText(city, "City must not be blank");
        Assert.isTrue(city.length() <= 30, "city length must be <= 30");

        Assert.notNull(street, "Street must not be null");
        Assert.hasText(street, "Street must not be blank");
        Assert.isTrue(street.length() <= 30, "street length must be <= 30");

        Assert.notNull(zipcode, "Zipcode must not be null");
        Assert.hasText(zipcode, "Zipcode must not be blank");
        Assert.isTrue(zipcode.length() <= 30, "zipcode length must be <= 30");

        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public static Address createAddress(String city, String street, String zipcode) {
        return new Address(city, street, zipcode);
    }

}