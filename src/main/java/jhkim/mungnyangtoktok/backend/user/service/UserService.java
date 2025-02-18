package jhkim.mungnyangtoktok.backend.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jhkim.mungnyangtoktok.backend.user.dto.JoinRequest;
import jhkim.mungnyangtoktok.backend.user.dto.LoginRequest;
import jhkim.mungnyangtoktok.backend.user.dto.LoginResponse;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import jhkim.mungnyangtoktok.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService; // ✅ 세션 서비스 추가

    public LoginResponse join(JoinRequest req, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.existsByLoginId(req.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }

        if (userRepository.existsByNickname(req.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User savedUser = userRepository.save(req.toEntity());

        // ✅ 세션 생성 및 세션 ID 반환
        String sessionId = sessionService.createSession(request, savedUser.getId());

        // ✅ 클라이언트가 세션을 유지할 수 있도록 `Set-Cookie` 설정
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setPath("/");           // 모든 경로에서 유효
        sessionCookie.setHttpOnly(true);      // JavaScript에서 접근 불가 (보안 강화)
        sessionCookie.setMaxAge(60 * 60 * 24); // 1일 유지 (초 단위)
        response.addCookie(sessionCookie);

        return new LoginResponse(sessionId);
    }

    public LoginResponse login(LoginRequest req, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByLoginId(req.getLoginId());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 로그인 ID입니다.");
        }

        if (!user.get().getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // ✅ 세션 생성 및 세션 ID 반환
        String sessionId = sessionService.createSession(request, user.get().getId());

        // ✅ 클라이언트에 `Set-Cookie` 헤더로 JSESSIONID 설정
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(sessionCookie);

        return new LoginResponse(sessionId);
    }
    public UserResponse getUserSessionInfo(HttpServletRequest request) {
        Long userId = sessionService.getUserIdFromSession(request); // ✅ 세션에서 userId 가져오기
        System.out.println("userId : " + userId);
        if (userId == null) {
            return null;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.isUser(),
                user.isAdmin(),
                user.isPetsitter()
        );
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // ✅ 세션 무효화
        request.getSession().invalidate();

        // ✅ JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0); // 쿠키 즉시 만료
        cookie.setPath("/"); // 모든 경로에서 유효
        response.addCookie(cookie);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getLoginId(),
                        user.getNickname(),
                        user.isUser(),
                        user.isAdmin(),
                        user.isPetsitter()
                ))
                .collect(Collectors.toList());
    }

    public boolean isAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(User::isAdmin)
                .orElse(false);
    }

    public void deleteUser(Long userId, HttpServletRequest request, HttpServletResponse response) {
        // 현재 로그인한 사용자 확인
        Long sessionUserId = sessionService.getUserIdFromSession(request);

        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            throw new IllegalArgumentException("회원 탈퇴 권한이 없습니다.");
        }

        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 사용자 삭제
        userRepository.deleteById(userId);

        // 세션 무효화
        request.getSession().invalidate();

        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void updateNickname(Long userId, String newNickname, HttpServletRequest request) {
        // 현재 로그인한 사용자 확인
        Long sessionUserId = sessionService.getUserIdFromSession(request);
        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            throw new IllegalArgumentException("닉네임 변경 권한이 없습니다.");
        }

        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

//        // 닉네임 중복 확인
//        if (userRepository.existsByNickname(newNickname)) {
//            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
//        }

        // 닉네임 업데이트
        user.updateNickname(newNickname);
        userRepository.save(user);
    }

    // ✅ 프로필 이미지 업데이트
    public void updateProfileImage(Long userId, MultipartFile file, HttpServletRequest request) throws IOException {
        Long sessionUserId = sessionService.getUserIdFromSession(request);
        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            throw new IllegalArgumentException("프로필 이미지 변경 권한이 없습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setProfileImage(file.getBytes()); // bytea 형태로 저장
        userRepository.save(user);
    }

    public byte[] getProfileImage(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        System.out.println("User 존재 여부: " + user.isPresent()); // 사용자 존재 여부 확인
        if (user.isPresent()) {
            System.out.println("User ID: " + user.get().getId());
            System.out.println("User Profile Image: " + Arrays.toString(user.get().getProfileImage()));
        } else {
            System.out.println("User not found for userId: " + userId);
        }
        return user.map(User::getProfileImage).orElse(null);
    }


}
