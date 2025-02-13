package jhkim.mungnyangtoktok.backend.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jhkim.mungnyangtoktok.backend.user.dto.JoinRequest;
import jhkim.mungnyangtoktok.backend.user.dto.LoginRequest;
import jhkim.mungnyangtoktok.backend.user.dto.UserResponse;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import jhkim.mungnyangtoktok.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse join(JoinRequest req) {

        if (userRepository.existsByLoginId(req.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }

        // 2. 닉네임 중복 체크
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User savedUser = userRepository.save(req.toEntity());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getLoginId(),
                savedUser.getNickname(),
                savedUser.isUser(),
                false,
                savedUser.isPetsitter(),
                null // 회원가입 시 세션 ID는 필요하지 않으므로 null
        );
    }

    public UserResponse login(LoginRequest req, HttpServletRequest request) {
        Optional<User> user = userRepository.findByLoginId(req.getLoginId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 로그인 ID입니다.");
        }

        if (!user.get().getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 세션 생성 및 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", user.get());
        session.setMaxInactiveInterval(60 * 30); // 30분 유지

        // 세션 ID 포함하여 반환
        return new UserResponse(
                user.get().getId(),
                user.get().getLoginId(),
                user.get().getNickname(),
                user.get().isUser(),
                user.get().isAdmin(),
                user.get().isPetsitter(),
                session.getId() // 세션 ID 추가
        );
    }
}
