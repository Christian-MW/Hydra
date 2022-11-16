package com.mwgroup.hydra.service;

import org.springframework.stereotype.Component;

import com.mwgroup.hydra.dto.request.*;
import com.mwgroup.hydra.dto.response.*;

@Component
public interface HydraService {
	void sendPostTweet(SendPostTWRequest request);
}
