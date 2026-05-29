package thuvien.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import thuvien.entity.Author;
import thuvien.service.AuthorService;

import java.util.List;

@Controller("userAuthorController")
public class AuthorController {

    private final AuthorService authorService;
    private final ObjectMapper objectMapper;

    public AuthorController(AuthorService authorService, ObjectMapper objectMapper) {
        this.authorService = authorService;
        this.objectMapper = objectMapper;
    }

    @GetMapping({"/authors", "/tac-gia", "/author"})
    public String authorsPage(Model model) {

        List<Author> authors = authorService.findAll();
        List<Author> featuredAuthors = authorService.getFeaturedAuthors(6);

        // Tính toán các số liệu thống kê an toàn bằng Java Stream ở Backend
        long totalAuthors = authors != null ? authors.size() : 0;
        
        long vnAuthors = authors != null ? authors.stream()
                .filter(a -> a.getNationality() != null && 
                        (a.getNationality().equalsIgnoreCase("Việt Nam") || 
                         a.getNationality().equalsIgnoreCase("Viet Nam")))
                .count() : 0;

        long intlAuthors = totalAuthors - vnAuthors;

        // Gửi dữ liệu thống kê sang giao diện ThymeLeaf
        model.addAttribute("authors", authors);
        model.addAttribute("featuredAuthors", featuredAuthors);
        model.addAttribute("totalAuthors", totalAuthors);
        model.addAttribute("vnAuthors", vnAuthors);
        model.addAttribute("intlAuthors", intlAuthors);
        model.addAttribute("pageTitle", "Tác giả – LibManage");

        // Ép chuỗi JSON an toàn cho JavaScript Modal tránh vòng lặp tuần hoàn Jackson
        try {
            List<java.util.Map<String, Object>> plainAuthors = authors.stream().map(a -> {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", a.getId());
                map.put("fullName", a.getFullName());
                map.put("nationality", a.getNationality());
                map.put("bio", a.getBio());
                
                // ✅ ĐÃ SỬA: Thay thế a.getAvatar() thành a.getImageUrl()
                map.put("avatar", a.getImageUrl()); 
                
                map.put("bookCount", a.getBooks() != null ? a.getBooks().size() : 0);
                return map;
            }).toList();

            String authorsJson = objectMapper.writeValueAsString(plainAuthors);
            model.addAttribute("authorsJson", authorsJson);
        } catch (Exception e) {
            model.addAttribute("authorsJson", "[]");
        }

        return "author";
    }

    // ══ ĐÃ THÊM: LOGIC XỬ LÝ XEM CHI TIẾT TÁC GIẢ THEO ID ══
    @GetMapping("/author/{id}")
    public String authorDetailPage(@PathVariable("id") Long id, Model model) {
        // Tìm thông tin tác giả từ Database theo id truyền vào
        Author author = authorService.findById(id);
        
        // Nếu không tồn tại tác giả này, chuyển hướng an toàn về trang danh sách chính
        if (author == null) {
            return "redirect:/authors";
        }
        
        // Gửi đối tượng tác giả sang trang giao diện chi tiết (author-detail.html)
        model.addAttribute("author", author);
        model.addAttribute("pageTitle", author.getFullName() + " – Chi tiết tác giả");
        
        return "author-detail";
    }
}