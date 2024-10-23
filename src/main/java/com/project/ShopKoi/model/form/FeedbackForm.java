package com.project.ShopKoi.model.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class FeedbackForm {
    @Min(0)
    @Max(5)
    private double rating;

    private String feedbackMessage;
}
