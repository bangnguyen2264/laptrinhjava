package com.project.ShopKoi.model.form;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackForm {
    @Size(min = 0, max = 5)
    private double rating;
    private String feedbackMessage;
}
