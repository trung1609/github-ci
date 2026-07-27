package com.devops.demo.userservice.service;

import com.devops.demo.userservice.dto.UserDto;
import com.devops.demo.userservice.entity.User;
import com.devops.demo.userservice.exception.ResourceNotFoundException;
import com.devops.demo.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Unit Test cho UserService
 *
 * Dùng Mockito để giả lập (mock) UserRepository, tách biệt hoàn toàn
 * với database thật. Kiểm thử đúng business logic của Service layer.
 *
 * Đây là loại test chạy nhanh nhất trong CI pipeline!
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("Nguyễn Văn Test")
                .email("test@example.com")
                .role(User.Role.STUDENT)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ==========================
    // TEST: getAllUsers()
    // ==========================
    @Test
    @DisplayName("Phải trả về danh sách users đang active")
    void getAllUsers_ShouldReturnActiveUsers() {
        // Given: Mock repository trả về danh sách users
        given(userRepository.findByActiveTrue()).willReturn(List.of(sampleUser));

        // When: Gọi service method
        List<UserDto.Response> result = userService.getAllUsers();

        // Then: Kiểm tra kết quả
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");

        // Đảm bảo repository được gọi đúng 1 lần
        then(userRepository).should(times(1)).findByActiveTrue();
    }

    // ==========================
    // TEST: getUserById() - Happy Path
    // ==========================
    @Test
    @DisplayName("Phải trả về user khi tìm thấy ID hợp lệ")
    void getUserById_WhenExists_ShouldReturnUser() {
        // Given
        given(userRepository.findById(1L)).willReturn(Optional.of(sampleUser));

        // When
        UserDto.Response result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Nguyễn Văn Test");
        assertThat(result.getRole()).isEqualTo(User.Role.STUDENT);
    }

    // ==========================
    // TEST: getUserById() - Not Found
    // ==========================
    @Test
    @DisplayName("Phải ném ResourceNotFoundException khi không tìm thấy ID")
    void getUserById_WhenNotExists_ShouldThrowException() {
        // Given: Repository không tìm thấy gì
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then: Kiểm tra exception được ném ra đúng kiểu
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ==========================
    // TEST: createUser() - Happy Path
    // ==========================
    @Test
    @DisplayName("Phải tạo user thành công khi email chưa tồn tại")
    void createUser_WhenEmailNotExists_ShouldCreateAndReturn() {
        // Given
        UserDto.CreateRequest request = UserDto.CreateRequest.builder()
                .name("User Mới")
                .email("new@example.com")
                .role(User.Role.STUDENT)
                .build();

        User savedUser = User.builder()
                .id(2L)
                .name("User Mới")
                .email("new@example.com")
                .role(User.Role.STUDENT)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(userRepository.existsByEmail("new@example.com")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // When
        UserDto.Response result = userService.createUser(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("new@example.com");

        then(userRepository).should(times(1)).save(any(User.class));
    }

    // ==========================
    // TEST: createUser() - Duplicate Email
    // ==========================
    @Test
    @DisplayName("Phải ném IllegalArgumentException khi email đã tồn tại")
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Given: Email đã tồn tại trong DB
        UserDto.CreateRequest request = UserDto.CreateRequest.builder()
                .name("User Trùng")
                .email("test@example.com")
                .build();

        given(userRepository.existsByEmail("test@example.com")).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("test@example.com");

        // Đảm bảo không gọi save vì đã fail sớm
        then(userRepository).should(never()).save(any());
    }

    // ==========================
    // TEST: deleteUser() - Soft Delete
    // ==========================
    @Test
    @DisplayName("Phải thực hiện soft delete (active=false) thay vì xóa cứng")
    void deleteUser_ShouldSetActiveFalse_NotHardDelete() {
        // Given
        given(userRepository.findById(1L)).willReturn(Optional.of(sampleUser));
        given(userRepository.save(any(User.class))).willReturn(sampleUser);

        // When
        userService.deleteUser(1L);

        // Then: Kiểm tra user đã bị đặt active = false
        assertThat(sampleUser.isActive()).isFalse();

        // Đảm bảo KHÔNG gọi deleteById (không xóa cứng)
        then(userRepository).should(never()).deleteById(anyLong());
        then(userRepository).should(times(1)).save(sampleUser);
    }

    // ==========================
    // TEST: searchUsers()
    // ==========================
    @Test
    @DisplayName("Phải trả về danh sách users khớp từ khóa tìm kiếm")
    void searchUsers_ShouldReturnMatchingUsers() {
        // Given
        given(userRepository.searchByName("Nguyễn")).willReturn(List.of(sampleUser));

        // When
        List<UserDto.Response> results = userService.searchUsers("Nguyễn");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("Nguyễn");
    }
}
