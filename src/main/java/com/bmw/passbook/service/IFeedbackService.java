package com.bmw.passbook.service;

import com.bmw.passbook.dto.FeedBack;
import com.bmw.passbook.dto.Response;

public interface IFeedbackService {
    Response createFeedback(FeedBack feedback);
    Response getFeedback(Long userId);
}
