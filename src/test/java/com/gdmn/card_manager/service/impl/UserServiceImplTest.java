package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.enums.Role;
import com.gdmn.card_manager.exseption.UserNotFoundException;
import com.gdmn.card_manager.exseption.UserStatusConflictException;
import com.gdmn.card_manager.exseption.UsernameAlreadyExistsException;
import com.gdmn.card_manager.mapper.UserMapper;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User()
                .setId(1L)
                .setUsername("test@email.com")
                .setPassword("test-Password.123")
                .setFirstName("First")
                .setMidlName("Midl")
                .setLastName("Last")
                .setEnabled(true)
                .setRole(Role.ROLE_USER);
    }

    @Test
    void testAddUser_shouldSaveUser() {
        Register register = mock(Register.class);
        when(userMapper.map(register)).thenReturn(testUser);

        userServiceImpl.addUser(register);

        verify(userRepository, times(1)).existsByUsername(any());
        verify(userRepository, times(1)).save(testUser);
        verify(userMapper, times(1)).map(register);
    }

    @Test
    void testAddUser_whenUserExists_shouldThrowException() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThatThrownBy(() -> userServiceImpl.addUser(mock(Register.class)))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");
        verify(userRepository, times(1)).existsByUsername(any());
    }

    @Test
    void testDisable_shouldDisableUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        userServiceImpl.disable(testUser.getId());

        verify(userRepository, times(1)).findById(anyLong());
        assertThat(testUser.isEnabled()).isFalse();
    }

    @Test
    void testDisable_whenUserNotExist_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.disable(testUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testDisable_whenUserIsDisable_shouldThrowException() {
        testUser.setEnabled(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userServiceImpl.disable(testUser.getId()))
                .isInstanceOf(UserStatusConflictException.class)
                .hasMessageContaining("User account already disable");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testEnable_shouldEnableUser() {
        testUser.setEnabled(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        userServiceImpl.enable(testUser.getId());

        verify(userRepository, times(1)).findById(anyLong());
        assertThat(testUser.isEnabled()).isTrue();
    }

    @Test
    void testEnable_whenUserNotExist_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.disable(testUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testEnable_whenUserIsEnable_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userServiceImpl.enable(testUser.getId()))
                .isInstanceOf(UserStatusConflictException.class)
                .hasMessageContaining("User account already enable");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void tesGetById_shouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        User actual = userServiceImpl.getById(testUser.getId());

        verify(userRepository, times(1)).findById(anyLong());
        assertThat(actual).isEqualTo(testUser);
        assertThat(actual.getId()).isEqualTo(testUser.getId());
        assertThat(actual.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(actual.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(actual.getMidlName()).isEqualTo(testUser.getMidlName());
        assertThat(actual.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(actual.isEnabled()).isEqualTo(testUser.isEnabled());
        assertThat(actual.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    void tesGetById_whenUserNotExist_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.getById(testUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserById_shouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        User actual = userServiceImpl.getById(testUser.getId());

        verify(userRepository, times(1)).findById(anyLong());
        assertThat(actual).isEqualTo(testUser);
        assertThat(actual.getId()).isEqualTo(testUser.getId());
        assertThat(actual.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(actual.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(actual.getMidlName()).isEqualTo(testUser.getMidlName());
        assertThat(actual.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(actual.isEnabled()).isEqualTo(testUser.isEnabled());
        assertThat(actual.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    void testGetUserById_whenUserNotExist_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.getById(testUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testExistsByUsername_whenUsernameExists_shouldReturnTrue() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        boolean actual = userServiceImpl.existsByUsername(anyString());

        verify(userRepository, times(1)).existsByUsername(anyString());
        assertThat(actual).isTrue();
    }

    @Test
    void testExistsByUsername_whenUsernameNotExists_shouldReturnFalse() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        boolean actual = userServiceImpl.existsByUsername(anyString());

        verify(userRepository, times(1)).existsByUsername(anyString());
        assertThat(actual).isFalse();
    }

    @Test
    void testGetByUsername_shouldReturnUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        User actual = userServiceImpl.getByUsername(testUser.getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());
        assertThat(actual).isEqualTo(testUser);
        assertThat(actual.getId()).isEqualTo(testUser.getId());
        assertThat(actual.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(actual.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(actual.getMidlName()).isEqualTo(testUser.getMidlName());
        assertThat(actual.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(actual.isEnabled()).isEqualTo(testUser.isEnabled());
        assertThat(actual.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    void testGetByUsername_whenUserNotExist_shouldThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.getByUsername(testUser.getUsername()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testDeleteById_shouldDeleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        userServiceImpl.deleteById(testUser.getId());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void testDeleteById_whenUserNotExist_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.deleteById(testUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).delete(testUser);
    }

    @Nested
    class UserServiceImplTestsWithAuth {
        @BeforeEach
        void setUp() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn(testUser.getUsername());

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);
        }

        @Test
        void testCurrentUser_shouldReturnCurrentUser() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

            User actual = userServiceImpl.currentUser();

            verify(userRepository, times(1)).findByUsername(anyString());
            assertThat(actual).isEqualTo(testUser);
            assertThat(actual.getId()).isEqualTo(testUser.getId());
            assertThat(actual.getUsername()).isEqualTo(testUser.getUsername());
            assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
            assertThat(actual.getFirstName()).isEqualTo(testUser.getFirstName());
            assertThat(actual.getMidlName()).isEqualTo(testUser.getMidlName());
            assertThat(actual.getLastName()).isEqualTo(testUser.getLastName());
            assertThat(actual.isEnabled()).isEqualTo(testUser.isEnabled());
            assertThat(actual.getRole()).isEqualTo(testUser.getRole());
        }

        @Test
        void testCurrentUser_whenUserNotExist_shouldThrowException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.currentUser())
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found");
            verify(userRepository, times(1)).findByUsername(anyString());
        }

        @Test
        void testGetCurrentUser_shouldReturnCurrentUserData() {
            UserData expected = mock(UserData.class);
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
            when(userMapper.map(any(User.class))).thenReturn(expected);

            UserData actual = userServiceImpl.getCurrentUser();

            verify(userMapper, times(1)).map(any(User.class));
            assertThat(actual).isEqualTo(expected);
        }

    }
}