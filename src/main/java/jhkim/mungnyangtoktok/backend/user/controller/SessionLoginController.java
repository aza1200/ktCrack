package jhkim.mungnyangtoktok.backend.user.controller;


import jhkim.mungnyangtoktok.backend.user.dto.LoginRequest;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jhkim.mungnyangtoktok.backend.user.dto.JoinRequest;
import jhkim.mungnyangtoktok.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")

public class SessionLoginController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> join(@Valid @RequestBody JoinRequest joinRequest) {
        UserResponse userResponse = userService.join(joinRequest);
        return ResponseEntity.ok().body(userResponse);
    }

    // 로그인 (세션 기반)
    @PostMapping("/login")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserResponse userResponse = userService.login(loginRequest, request);

        // 로그인 성공 시 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", userResponse);
        session.setMaxInactiveInterval(60 * 30); // 세션 유지 시간 (30분)


        return ResponseEntity.ok().body(userResponse);
    }

    // 로그아웃 (세션 삭제)
    @PostMapping("/logout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기 (없으면 null)
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok("로그아웃 성공");
    }
}
