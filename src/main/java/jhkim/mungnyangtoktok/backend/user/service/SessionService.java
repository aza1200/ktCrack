package jhkim.mungnyangtoktok.backend.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    /**
     * 새로운 세션을 생성하고 userId를 저장
     */
    public String createSession(HttpServletRequest request, Long userId) {
        HttpSession session = request.getSession(true); // 세션이 없으면 생성
        session.setAttribute("userId", userId);
        session.setMaxInactiveInterval(60 * 60 * 24); // 30분 유지
        return session.getId(); // 세션 ID 반환
    }


    /**
     * 현재 요청에서 userId를 가져옴
     */
    public Long getUserIdFromSession(HttpServletRequest request) {
        String sessionId = getSessionIdFromCookie(request); // 🔹 쿠키에서 세션 ID 가져오기
        if (sessionId == null) {
            return null;
        }

        HttpSession session = request.getSession(false); // 🔹 기존 세션 가져오기 (없으면 null 반환)
        if (session == null || !sessionId.equals(session.getId()) || session.getAttribute("userId") == null) {
            return null;
        }

        return (Long) session.getAttribute("userId");
    }
    /**
     * 세션 무효화 (로그아웃 시 사용)
     */
    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // ✅ 쿠키에서 세션 ID 가져오는 메서드 추가
    private String getSessionIdFromCookie(HttpServletRequest request) {
        System.out.println("request.getCookies() : " + request.getCookies());
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // System.out.println("cookie.getName() : " + cookie.getName());
                if ("JSESSIONID".equals(cookie.getName())) { // ✅ 쿠키 이름이 "SESSION"이라고 가정
                    System.out.println("cookie.getValue : "+ cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
