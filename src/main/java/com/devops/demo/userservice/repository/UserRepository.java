package com.devops.demo.userservice.repository;

import com.devops.demo.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository tương tác với database thông qua Spring Data JPA
 * Không cần viết SQL thủ công, chỉ cần khai báo tên method theo quy tắc
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email (để kiểm tra email đã tồn tại chưa)
    Optional<User> findByEmail(String email);

    // Tìm tất cả user theo role
    List<User> findByRole(User.Role role);

    // Tìm tất cả user đang active
    List<User> findByActiveTrue();

    // Kiểm tra email đã tồn tại chưa (dùng EXISTS, nhanh hơn findByEmail)
    boolean existsByEmail(String email);

    // Custom JPQL query: tìm kiếm user theo tên (không phân biệt hoa thường)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByName(@Param("keyword") String keyword);

    // Đếm số user theo role
    long countByRole(User.Role role);
}
