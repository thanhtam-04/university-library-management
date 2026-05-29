package thuvien.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import thuvien.service.StatisticsService;

@Controller
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ObjectMapper      objectMapper;

    @GetMapping
    public String index(Model model) throws JsonProcessingException {

        /* ── Tổng quan ── */
        model.addAttribute("totalBooks",       statisticsService.totalBooks());
        model.addAttribute("totalCopies",      statisticsService.totalCopies());
        model.addAttribute("totalMembers",     statisticsService.totalMembers());
        model.addAttribute("totalLoans",       statisticsService.totalLoans());
        model.addAttribute("activeLoans",      statisticsService.activeLoans());
        model.addAttribute("overdueLoans",     statisticsService.overdueLoans());
        model.addAttribute("unpaidFines",      statisticsService.unpaidFines());
        model.addAttribute("totalDebt",        statisticsService.totalDebt());
        model.addAttribute("totalFineAmount",  statisticsService.totalFineAmount());
        model.addAttribute("totalPaidAmount",  statisticsService.totalPaidAmount());

        /* ── Biểu đồ mượn/trả theo tháng (JSON cho Chart.js) ── */
        var loansByMonth   = statisticsService.loansByMonth();
        var returnsByMonth = statisticsService.returnsByMonth();
        model.addAttribute("chartLabels",  objectMapper.writeValueAsString(
                loansByMonth.keySet().stream().toList()));
        model.addAttribute("chartLoans",   objectMapper.writeValueAsString(
                loansByMonth.values().stream().toList()));
        model.addAttribute("chartReturns", objectMapper.writeValueAsString(
                returnsByMonth.values().stream().toList()));

        /* ── Top sách mượn nhiều ── */
        var topBooks = statisticsService.topBorrowedBooks(10);
        model.addAttribute("topBookTitles", objectMapper.writeValueAsString(
                topBooks.stream().map(b -> b.get("title")).toList()));
        model.addAttribute("topBookCounts", objectMapper.writeValueAsString(
                topBooks.stream().map(b -> b.get("count")).toList()));
        model.addAttribute("topBooks", topBooks);

        /* ── Phiếu phạt ── */
        model.addAttribute("fineDist",      objectMapper.writeValueAsString(
                statisticsService.fineStatusDistribution().values().stream().toList()));
        model.addAttribute("fineDistLabels", objectMapper.writeValueAsString(
                statisticsService.fineStatusDistribution().keySet().stream().toList()));
        model.addAttribute("topDebtMembers", statisticsService.topDebtMembers(5));

        model.addAttribute("activePage", "statistics");
        return "views/admin/statistics/index";
    }
}