package thuvien.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import thuvien.entity.User;
import thuvien.entity.Role;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String uri = request.getRequestURI();

        // 1. Nếu chưa đăng nhập thì tự động đá về trang login
        if (currentUser == null) {
            response.sendRedirect("/login?error=timeout");
            return false;
        }

        // Kiểm tra xem User hiện tại có phải Thủ thư không
        boolean isLibrarian = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == Role.RoleName.ROLE_LIBRARIAN);

        // 2. KHÓA CHẶT: Nếu là Thủ thư và URL có chứa từ khóa hành động nguy hiểm (delete, remove, duyệt nhân sự...)
        if (isLibrarian) {
            if (uri.contains("/delete") || uri.contains("/remove") || uri.startsWith("/admin/user/update-role")) {
                // Chuyển hướng thẳng sang trang thông báo không có quyền truy cập
                response.sendRedirect("/admin/403");
                return false; // Chặn đứng, không cho Controller chạy tiếp
            }
        }

        return true; // Cho phép đi tiếp nếu là ADMIN hoặc các request hợp lệ khác
    }
}