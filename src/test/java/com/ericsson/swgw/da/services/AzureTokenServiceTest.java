package com.ericsson.swgw.da.services;

import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AzureTokenServiceTest {

    
	
	@Test
	public void doFilterInternal_shouldPopulateSecurityContext_whenTokenIsValid() { //
		AzureTokenService token = mock(AzureTokenService.class);
		// Map<String,String> expected = new HashMap<String, String>();
		// expected.put();

		// MockHttpServletRequest request = new MockHttpServletRequest("GET",
		// "/");
		// request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();
		MockFilterConfig filterConfig = new MockFilterConfig();
		// //// JwtTokenAuthenticationFilter filter = new
		// JwtTokenAuthenticationFilter();
		// filter.init(filterConfig);
		// filter.doFilter(request, response, filterChain);
		// filter.destroy();
		// Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication())
		// .satisfies(authentication -> {
		// //assertThat(authentication).isNotNull();
		// assertThat(authentication.getName()).isEqualTo("rajni.k"); // });
	}
	
	 
}
