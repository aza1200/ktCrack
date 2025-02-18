package jhkim.mungnyangtoktok.backend.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import jhkim.mungnyangtoktok.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(HttpServletRequest request) {
//        if (!userService.isAdmin(adminId)) {
//            return ResponseEntity.status(403).build(); // 권한 없음
//        }

        List<UserResponse> users = userService.getAllUsers();
        System.out.println(users);
        return ResponseEntity.ok(users);
    }
}
