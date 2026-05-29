
package thuvien.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RulesController {

    @GetMapping("/rules")
    public String rulesPage(Model model) {
        // Nhi có thể gửi thêm các thông tin như ngày cập nhật cuối cùng từ Server nếu muốn
        model.addAttribute("lastUpdate", "01/01/2026");
        
        // Trả về file HTML tại: src/main/resources/templates/views/rules.html
        // (Nhi kiểm tra lại đường dẫn file của mình nhé)
        return "rules";
    }
}