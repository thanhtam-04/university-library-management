package thuvien.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import thuvien.dto.response.BookResponse;
import thuvien.service.BookService;
import thuvien.service.CategoryService;
import thuvien.service.StatisticsService;
import thuvien.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final BookService       bookService;
    private final CategoryService   categoryService;
    private final StatisticsService statisticsService;
    private final UserService       userService;

    public HomeController(BookService bookService,
                          CategoryService categoryService,
                          StatisticsService statisticsService,
                          UserService userService) {
        this.bookService       = bookService;
        this.categoryService   = categoryService;
        this.statisticsService = statisticsService;
        this.userService       = userService;
    }

    @GetMapping({"/", "/home"})
    public String homepage(@RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "author", required = false) String author,
                           @RequestParam(value = "category", required = false) Long categoryId,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                           Model model) {

        // Kiểm tra xem có tham số lọc nào được gửi lên không
        boolean isSearching = (title != null && !title.trim().isEmpty()) ||
                              (author != null && !author.trim().isEmpty()) ||
                              (categoryId != null) ||
                              (status != null && !status.trim().isEmpty());

        if (isSearching) {
            List<BookResponse> searchResults = bookService.searchAndFilterBooks(title, author, categoryId, status);
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("isSearching", true);
            model.addAttribute("totalSearchBooks", searchResults.size());
        } else {
            model.addAttribute("isSearching", false);
            List<BookResponse> featuredBooks = bookService.getFeaturedBooks(8);
            List<BookResponse> newBooks      = bookService.getNewBooks(6);
            if (featuredBooks == null || featuredBooks.size() < 4) {
                featuredBooks = bookService.getNewBooks(8);
            }
            model.addAttribute("featuredBooks", featuredBooks != null ? featuredBooks : List.of());
            model.addAttribute("newBooks",      newBooks      != null ? newBooks      : List.of());
        }

        // Nếu là yêu cầu AJAX từ Fetch API, chỉ trả về mảnh giao diện chứa danh sách sách
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "home :: #bookContainerDynamic";
        }

        model.addAttribute("pageTitle",  "Thư Viện Đại Học - LibManage");
        model.addAttribute("activePage", "home");
        model.addAttribute("statistics", statisticsService.getStatistics());
        model.addAttribute("categories", categoryService.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            try {
                model.addAttribute("currentUser", userService.findByUsername(auth.getName()));
            } catch (Exception ignored) {}
        }

        return "home";
    }
}