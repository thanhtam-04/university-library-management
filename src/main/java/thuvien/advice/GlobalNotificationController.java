package thuvien.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import thuvien.entity.Loan;
import thuvien.repository.LoanRepository;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalNotificationController {

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Phương thức này sẽ tự động chạy trước khi render bất kỳ View nào.
     * Nó thêm thuộc tính 'pendingAlertCount' vào Model chung.
     */
    @ModelAttribute
    public void addNotificationAttributes(Model model) {
        // Chỉ thực hiện truy vấn nếu user đã đăng nhập (để tránh lỗi khi chưa login)
        // Bạn có thể thêm kiểm tra SecurityContextHolder nếu cần
        
        try {
            // Định nghĩa mốc thời gian: 24 giờ trước
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            
            // Đếm số lượng phiếu PENDING tạo trước mốc đó
            long count = loanRepository.countByStatusAndLoanDateBefore(Loan.Status.PENDING, yesterday);
            
            // Đẩy vào model để dùng trong layout.html
            model.addAttribute("pendingAlertCount", count);
        } catch (Exception e) {
            // Ghi log hoặc bỏ qua nếu có lỗi kết nối DB
            model.addAttribute("pendingAlertCount", 0);
        }
    }
}