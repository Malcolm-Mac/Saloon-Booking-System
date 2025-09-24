package com.medelin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.medelin.dto.UserDetailResponse;
import com.medelin.repository.UserRepository;
import com.medelin.service.UserService;
import com.medelin.util.IdHasherUtil;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@AllArgsConstructor
class UserServiceApplicationTests
{
	@MockitoBean
	private IdHasherUtil idHasherUtil;

	@MockitoBean
	private UserRepository userRepository;

	private UserService userService;

	/**
	 * Test {@link UserService#getUsers(Pageable)}.
	 *
	 * <ul>
	 *   <li>Given {@link IdHasherUtil}.
	 *   <li>Then return toList Empty.
	 * </ul>
	 *
	 * <p>Method under test: {@link UserService#getUsers(Pageable)}
	 */
	@Test
	@DisplayName("Test getUsers(Pageable); given IdHasherUtil; then return toList Empty")
	@Tag("MaintainedByDiffblue")
	void testGetUsers_givenIdHasherUtil_thenReturnToListEmpty()
	{
		// Arrange
		when(userRepository.findAll(Mockito.<Pageable>any()))
				.thenReturn(new PageImpl<>(new ArrayList<>()));

		// Act
		Page<UserDetailResponse> actualUsers = userService.getUsers(null);

		// Assert
		verify(userRepository).findAll((Pageable) isNull());
		//assertTrue(actualUsers instanceof PageImpl);
		assertTrue(actualUsers.toList().isEmpty());
	}
}

