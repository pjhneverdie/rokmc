package com.pjh.jpadrill.member.vo;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 30)
    private String street;

    @Column(nullable = false, length = 30)
    private String zipcode;

    @Builder(access = AccessLevel.PROTECTED)
    private Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public static Address createAddress(String city, String street, String zipcode) {
        // 1. 필수 값 검증
        Assert.hasText(city, "시는 필수 입력 값입니다.");
        Assert.hasText(street, "도로명 주소는 필수 입력 값입니다.");
        Assert.hasText(zipcode, "우편번호는 필수 입력 값입니다.");

        // 2. 길이 검증
        Assert.isTrue(city.length() <= 30, "시는 30자를 초과할 수 없습니다.");
        Assert.isTrue(street.length() <= 30, "도로명 주소는 30자를 초과할 수 없습니다.");
        Assert.isTrue(zipcode.length() <= 30, "우편번호는 30자를 초과할 수 없습니다.");

        return Address.builder()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .build();
    }

}