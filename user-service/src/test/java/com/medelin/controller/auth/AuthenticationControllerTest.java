package com.medelin.controller.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.medelin.dto.AuthenticationRequest;
import com.medelin.dto.AuthenticationResponse;
import com.medelin.dto.CreateUserRequest;
import com.medelin.exception.handler.GlobalExceptionHandler;
import com.medelin.model.enumeration.Role;
import com.medelin.service.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthenticationController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest
{
    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockitoBean
    private IAuthenticationService iAuthenticationService;

    /**
     * Test {@link AuthenticationController#register(CreateUserRequest)}.
     *
     * <p>Method under test: {@link AuthenticationController#register(CreateUserRequest)}
     */
    @Test
    @DisplayName("Test register(CreateUserRequest)")
    @Disabled("TODO: Complete this test")
    void testRegister() throws Exception
    {
        // Arrange
        MockHttpServletRequestBuilder contentTypeResult =
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON);
        CreateUserRequest createUserRequest =
                new CreateUserRequest(
                        "Dr Jane Doe", "jane.doe@example.org", "6625550144", Role.ADMIN, "iloveyou");
        String content =
                JsonMapper.builder().findAndAddModules().build().writeValueAsString(createUserRequest);

        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Something went wrong"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.details").isArray());

    }

    /**
     * Test {@link AuthenticationController#register(CreateUserRequest)}.
     *
     * <ul>
     *   <li>Then StatusCode return {@link HttpStatus}.
     * </ul>
     *
     * <p>Method under test: {@link AuthenticationController#register(CreateUserRequest)}
     */
    @Test
    @DisplayName("Test register(CreateUserRequest); then StatusCode return HttpStatus")
    void testRegister_thenStatusCodeReturnHttpStatus()
    {
        // Arrange
        IAuthenticationService IAuthenticationService = mock(IAuthenticationService.class);
        when(IAuthenticationService.createUser(Mockito.<CreateUserRequest>any()))
                .thenReturn(AuthenticationResponse.builder().expiresIn(1L).token("ABC123").build());
        AuthenticationController authenticationController =
                new AuthenticationController(IAuthenticationService);
        CreateUserRequest request =
                new CreateUserRequest(
                        "Dr Jane Doe", "jane.doe@example.org", "6625550144", Role.ADMIN, "iloveyou");

        // Act
        ResponseEntity<AuthenticationResponse> actualRegisterResult =
                authenticationController.register(request);

        // Assert
        verify(IAuthenticationService).createUser(isA(CreateUserRequest.class));
        HttpStatusCode statusCode = actualRegisterResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        AuthenticationResponse body = actualRegisterResult.getBody();
        assertEquals("ABC123", body.getToken());
        assertNull(body.getHashedId());
        assertEquals(1L, body.getExpiresIn().longValue());
        assertEquals(201, actualRegisterResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualRegisterResult.hasBody());
        assertTrue(actualRegisterResult.getHeaders().isEmpty());
    }

    /**
     * Test {@link AuthenticationController#authenticate(AuthenticationRequest)}.
     *
     * <p>Method under test: {@link AuthenticationController#authenticate(AuthenticationRequest)}
     */
    @Test
    @DisplayName("Test authenticate(AuthenticationRequest)")
    void testAuthenticate() throws Exception {
        // Arrange
        when(iAuthenticationService.authenticate(Mockito.<AuthenticationRequest>any()))
                .thenReturn(AuthenticationResponse.builder().expiresIn(1L).token("ABC123").build());

        MockHttpServletRequestBuilder contentTypeResult =
                MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON);

        JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
        String content =
                jsonMapper.writeValueAsString(
                        new AuthenticationRequest("jane.doe@example.org", "iloveyou"));

        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("{\"hashedId\":null,\"token\":\"ABC123\",\"expiresIn\":1}"));
    }
}
