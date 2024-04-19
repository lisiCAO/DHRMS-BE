package com.lisi.booknavigator.fileservice.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GCSUrlValidator implements ConstraintValidator<GCSUrl, String> {
    @Override
    public void initialize(GCSUrl constraint) {
        // initailize some resources
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null) {
            return false;  // null 值应由 @NotNull 或 @NotBlank 处理
        }
        return url.matches("^gs://[^/]+/.+$");  // 简单正则表达式验证 GCS URL 格式
    }
}
