package jhkim.mungnyangtoktok.backend.user.controller;


import jhkim.mungnyangtoktok.backend.user.dto.*;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jhkim.mungnyangtoktok.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class SessionLoginController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<LoginResponse> join(@Valid @RequestBody JoinRequest joinRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        LoginResponse loginResponse = userService.join(joinRequest, request, response);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        LoginResponse loginResponse = userService.login(loginRequest, request, response);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserSessionInfo(HttpServletRequest request) {
        UserResponse userResponse = userService.getUserSessionInfo(request);

        if (userResponse == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(userId, request, response);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<?> updateNickname(@PathVariable Long userId, @RequestBody Map<String, String> body, HttpServletRequest request) {
        String newNickname = body.get("nickname");
        userService.updateNickname(userId, newNickname, request);
        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }

    // ✅ 프로필 이미지 업로드 API 추가
    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            userService.updateProfileImage(userId, file, request);
            return ResponseEntity.ok("프로필 이미지가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("프로필 이미지 업데이트 실패: " + e.getMessage());
        }
    }

    // ✅ 프로필 이미지 가져오기 API 추가
    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<?> getProfileImage(@PathVariable Long userId) {
        byte[] imageBytes = userService.getProfileImage(userId);
        if (imageBytes == null) {
            return ResponseEntity.notFound().build();
        }

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return ResponseEntity.ok().body(Map.of("image", base64Image));
    }
}
