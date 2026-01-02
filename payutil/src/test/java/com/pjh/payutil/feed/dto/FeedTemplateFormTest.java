package com.pjh.payutil.feed.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class FeedTemplateFormTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidFeedTemplateForm() {
        // 유효한 데이터를 가진 FeedTemplateForm 객체
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title", // contentTitle
                "Valid Description", // contentDescription
                "https://example.com/image.png", // contentImageURL
                "Button", // buttonTitle
                "https://qr.kakaopay.com/validURL" // kakaopayURL
        );

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // 검증 결과가 없으면 올바른 객체
        assertTrue(violations.isEmpty());
    }

    @Test
    void testContentTitleNotBlank() {
        FeedTemplateForm form = new FeedTemplateForm(
                "", // contentTitle (빈 값)
                "Valid Description",
                "https://example.com/image.png",
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentTitle이 빈 값이면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testContentTitleSize() {
        FeedTemplateForm form = new FeedTemplateForm(
                "This is a very long title", // contentTitle (길이가 15자를 초과)
                "Valid Description",
                "https://example.com/image.png",
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentTitle이 15자를 초과하면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testContentDescriptionNotBlank() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "", // contentDescription (빈 값)
                "https://example.com/image.png",
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentDescription이 빈 값이면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testContentDescriptionSize() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "This description is way too long and exceeds the maximum length allowed", // contentDescription (길이가
                                                                                           // 20자를 초과)
                "https://example.com/image.png",
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentDescription이 20자를 초과하면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testContentImageURLNotBlank() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "", // contentImageURL (빈 값)
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentImageURL이 빈 값이면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testContentImageURLValidURL() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "invalid-url", // contentImageURL (잘못된 URL)
                "Button",
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // contentImageURL이 유효하지 않은 URL이면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testButtonTitleNotBlank() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "https://example.com/image.png",
                "", // buttonTitle (빈 값)
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // buttonTitle이 빈 값이면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testButtonTitleSize() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "https://example.com/image.png",
                "ThisButtonIsTooLong", // buttonTitle (길이가 10자를 초과)
                "https://qr.kakaopay.com/validURL");

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // buttonTitle이 10자를 초과하면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testKakaoPayURLPattern() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "https://example.com/image.png",
                "Button",
                "https://wrongurl.com/invalid" // kakaopayURL (잘못된 URL)
        );

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // kakaopayURL이 정규식에 맞지 않으면 실패해야 함
        assertFalse(violations.isEmpty());
    }

    @Test
    void testKakaoPayURLValid() {
        FeedTemplateForm form = new FeedTemplateForm(
                "Valid Title",
                "Valid Description",
                "https://example.com/image.png",
                "Button",
                "https://qr.kakaopay.com/validURL" // kakaopayURL (정규식에 맞는 URL)
        );

        Set<ConstraintViolation<FeedTemplateForm>> violations = validator.validate(form);

        // kakaopayURL이 정규식에 맞으면 검증이 통과해야 함
        assertTrue(violations.isEmpty());
    }
}
