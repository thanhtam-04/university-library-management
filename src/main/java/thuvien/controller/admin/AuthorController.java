package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Author;
import thuvien.service.AuthorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("activePage", "author");
        return "views/admin/author/list";           // ✅ bỏ "admin/" ở giữa
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("activePage", "author");
        return "views/admin/author/add";            // ✅ sửa
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("author") Author author,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       RedirectAttributes ra) {
        try {
            // Xử lý upload ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/authors/";
                Path uploadPath = Paths.get(uploadDir);

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Tạo tên file ngẫu nhiên tránh trùng: uuid + phần mở rộng (.png, .jpg...)
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String fileName = UUID.randomUUID().toString() + extension;

                // Lưu file vật lý vào thư mục static
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Gán tên file ảnh mới vào entity để lưu xuống DB
                author.setImageUrl(fileName);
            } else if (author.getId() != null) {
                // Nếu là update (có ID) và không chọn ảnh mới, giữ nguyên ảnh cũ đã có sẵn trong DB
                Author existingAuthor = authorService.findById(author.getId());
                if (existingAuthor != null) {
                    author.setImageUrl(existingAuthor.getImageUrl());
                }
            }

            authorService.save(author);
            ra.addFlashAttribute("successMsg", "Lưu thông tin tác giả thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMsg", "Có lỗi xảy ra khi lưu file ảnh!");
        }
        return "redirect:/admin/author/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.findById(id));
        model.addAttribute("activePage", "author");
        return "views/admin/author/edit";           // ✅ sửa
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("author") Author author,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         RedirectAttributes ra) {
        // Gọi lại hàm save để tận dụng chung logic xử lý ảnh và lưu DB
        return save(author, imageFile, ra);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            authorService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa tác giả!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg",
                "Không thể xóa tác giả này (có thể đang có sách của tác giả này).");
        }
        return "redirect:/admin/author/list";
    }
}