package thuvien.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import thuvien.dto.response.LoanResponse;
import thuvien.dto.response.StatisticsResponse;
import thuvien.service.LoanService;
import thuvien.service.StatisticsService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final StatisticsService statisticsService;
    private final LoanService loanService;

    // Constructor Injection
    public DashboardController(StatisticsService statisticsService, LoanService loanService) {
        this.statisticsService = statisticsService;
        this.loanService = loanService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        StatisticsResponse stats = statisticsService.getStatistics();
        
        // Lấy 5 phiếu mượn gần nhất
        List<LoanResponse> recentLoans = loanService.getRecentLoans(5);

      
        model.addAttribute("recentLoans", recentLoans);
        model.addAttribute("pageTitle", "Dashboard");

        return "views/admin/dashboard";
    }
}