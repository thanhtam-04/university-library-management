package thuvien.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import thuvien.dto.response.BookResponse;
import thuvien.entity.Category;
import thuvien.service.BookService;
import thuvien.service.CategoryService;

import java.util.List;

@Controller("userCategoryController")
public class CategoryController {

    private final BookService bookService;
    private final CategoryService categoryService;

    public CategoryController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    // 1. TRANG DANH MỤC (LỌC THEO SÁCH)
    @GetMapping("/category")
    public String categoryPage(@RequestParam(value = "id", required = false) Long categoryId, Model model) {

        // Luôn lấy tất cả danh mục để hiển thị ở thanh Sidebar bên trái
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        // Tính tổng số lượng tất cả sách để hiển thị ở nút "Tất cả danh mục"
        List<BookResponse> allBooks = bookService.getAllBooks();
        model.addAttribute("totalBooks", allBooks.size());

        List<BookResponse> books;
        String selectedCatName;

        // Logic kiểm tra và lọc theo Danh mục
        if (categoryId != null) {
            books = bookService.findByCategoryId(categoryId); 
            
            selectedCatName = categories.stream()
                    .filter(cat -> cat.getId().equals(categoryId))
                    .map(Category::getName)
                    .findFirst()
                    .orElse("Danh mục không tồn tại");
                    
            model.addAttribute("selectedCat", categoryId); // Highlight màu danh mục
        } else {
            books = allBooks;
            selectedCatName = "Tất cả sách";
            model.addAttribute("selectedCat", null);
        }

        System.out.println("=== CONTROLLER: Truyền vào template " + books.size() + " sách ===");

        model.addAttribute("books", books);
        model.addAttribute("selectedCatName", selectedCatName);

        return "category";
    }

    // 2. TRANG CHI TIẾT SÁCH (ĐƯỢC GỌI KHI BẤM NÚT XEM CHI TIẾT)
    @GetMapping("/books/{id}")
    public String bookDetailPage(@PathVariable("id") Long id, Model model) {
        System.out.println("=== CONTROLLER: Bắt đầu gọi lấy chi tiết đầu sách ID: " + id + " ===");
        
        // Lấy dữ liệu sách theo ID
        BookResponse book = bookService.getBookById(id); 
        
        if (book == null) {
            System.out.println("=== CONTROLLER: Không tìm thấy sách với ID: " + id + " -> Chuyển hướng!");
            return "redirect:/category"; 
        }
        
        model.addAttribute("book", book);
        System.out.println("=== CONTROLLER: Tìm thấy sách [" + book.getTitle() + "]. Đang tiến hành render template book-detail ===");
        
        return "book-detail"; // File này phải nằm tại src/main/resources/templates/book-detail.html
    }
}