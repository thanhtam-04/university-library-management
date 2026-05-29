package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Book;
import thuvien.service.*;

@Controller
@RequestMapping("/admin/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        java.util.List<Book> allBooks = bookService.findAll();
        
        if ("outofstock".equalsIgnoreCase(status)) {
            java.util.List<Book> outOfStockBooks = allBooks.stream()
                    .filter(book -> book.getAvailableCopies() == null || book.getAvailableCopies() <= 0)
                    .collect(java.util.stream.Collectors.toList());
                    
            model.addAttribute("books", outOfStockBooks);
            model.addAttribute("filterStatus", "outofstock"); // Dòng này cực kỳ quan trọng để ẩn/hiện nút Quay lại
        } else {
            model.addAttribute("books", allBooks);
            model.addAttribute("filterStatus", "normal");
        }
        
        model.addAttribute("activePage", "book");
        return "views/admin/book/list";
    }

    // Mở form thêm sách mới
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("activePage", "book");
        return "views/admin/book/add";
            }

    // Lưu sách (Dùng chung cho cả thêm và sửa)
 // Lưu sách (Dùng chung cho cả thêm và sửa)
    @PostMapping("/save")
    public String save(@ModelAttribute("book") Book book, 
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, 
                       RedirectAttributes ra) {
        try {
            // Kiểm tra nếu người dùng thực sự có bấm chọn tệp tải lên từ máy tính
            if (imageFile != null && !imageFile.isEmpty()) {
                // 1. Đường dẫn thư mục lưu ảnh cục bộ
                String uploadDir = "src/main/resources/static/uploads/books/";
                
                // Tự động kiểm tra cấu trúc thư mục, nếu chưa có thì code tự tạo mới luôn để tránh lỗi FileNotFound
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // 2. Tạo tên file duy nhất tránh trùng lặp tài nguyên kho
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                
                // 3. Thực hiện sao chép file vào tài nguyên static của hệ thống
                Path path = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                
                // 4. Ghi đè đường dẫn file nội bộ này vào cơ sở dữ liệu để Thymeleaf kết xuất
                book.setCoverImage("/uploads/books/" + fileName);
            } 
            // NẾU KHÔNG CHỌN FILE: Tự động giữ nguyên giá trị link URL nhập ở ô `coverImage` (không làm gì thêm)

            // Thực hiện ghi dữ liệu xuống MySQL/HeidiSQL thông qua Service
            bookService.save(book);
            ra.addFlashAttribute("successMsg", "Đã lưu thông tin sách thành công!");
        } catch (IOException e) {
            ra.addFlashAttribute("errorMsg", "Lỗi hệ thống khi upload cấu trúc ảnh: " + e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể lưu sách. Nguyên nhân: " + e.getMessage());
        }
        return "redirect:/admin/book/list";
    }

    // Mở form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("activePage", "book");
        return "views/admin/book/edit";
            }

    // Xóa sách
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Xóa sách thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa sách này!");
        }
        return "redirect:/admin/book/list";
            }
}