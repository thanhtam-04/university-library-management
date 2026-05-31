package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.DigitalDocument;
import thuvien.service.DigitalDocumentService;
// Đảm bảo bạn import đúng 2 Service của Tác giả và Thể loại/Danh mục vào đây
import thuvien.service.AuthorService;
import thuvien.service.CategoryService;

@Controller
@RequestMapping("/admin/digital")
@RequiredArgsConstructor
public class AdminDigitalController {

    private final DigitalDocumentService documentService;
    private final AuthorService authorService;       // THÊM: Inject AuthorService để lấy danh sách tác giả
    private final CategoryService categoryService; // THÊM: Inject CategoryService để lấy danh sách danh mục

    @GetMapping({"", "/", "/list"})
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) String type,
                       Model model) {
        model.addAttribute("documents", documentService.searchDocuments(q, type));
        model.addAttribute("query", q);
        model.addAttribute("selectedType", type);
        model.addAttribute("activePage", "digital");
        return "views/admin/digital/list";
    }

    @GetMapping("/add")
    public String addDigitalForm(Model model) {
        model.addAttribute("document", new DigitalDocument()); 
        
        // THÊM: Đổ dữ liệu danh sách Tác giả và Danh mục ra ngoài Form để Thymeleaf lặp (th:each)
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        
        return "views/admin/digital/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        DigitalDocument doc = documentService.getById(id);
        if (doc == null) return "redirect:/admin/digital/list";

        // SỬA: Đồng nhất biến dùng chung cho cả Form Add/Edit là "document" thay vì "doc" 
        // Điều này giúp bạn dùng chung 1 file form.html cho cả Thêm và Sửa cực kỳ tiện lợi
        model.addAttribute("document", doc);
        model.addAttribute("currentFile", doc.getFileUrl());
        
        // THÊM: Edit cũng cần danh sách để người dùng chọn lại dropdown
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        
        @SuppressWarnings("unused") 
        String activePage = "digital"; // Giữ trạng thái menu active
        model.addAttribute("activePage", "digital");
        
        return "views/admin/digital/edit"; // Thường Add và Edit sẽ dùng chung file form.html
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DigitalDocument document,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       RedirectAttributes ra) {

        System.out.println("=== DEBUG SAVE ===");
        System.out.println("ID: " + document.getId());

        // 1. Nếu là hành động SỬA (Đã có ID)
        if (document.getId() != null) {
            DigitalDocument oldDoc = documentService.getById(document.getId());
            // Nếu người dùng KHÔNG upload file mới, hãy giữ lại đường dẫn file cũ từ database
            if (file == null || file.isEmpty()) {
                if (oldDoc != null) {
                    document.setFileUrl(oldDoc.getFileUrl());
                    System.out.println("→ Giữ nguyên file cũ từ DB: " + oldDoc.getFileUrl());
                }
            }
        }

        // 2. Nếu người dùng CÓ tải lên file mới (Áp dụng cho cả Thêm và Sửa)
        if (file != null && !file.isEmpty()) {
            String newFileUrl = documentService.uploadAndGetUrl(file);
            if (newFileUrl != null) {
                document.setFileUrl(newFileUrl);
                System.out.println("→ ĐÃ CẬP NHẬT fileUrl mới: " + newFileUrl);
            } else {
                System.out.println("→ Upload thất bại!");
            }
        }

        documentService.save(document);
        ra.addFlashAttribute("successMsg", "Cập nhật tài liệu thành công!");
        return "redirect:/admin/digital/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        documentService.deleteById(id);
        ra.addFlashAttribute("successMsg", "Đã xóa tài liệu!");
        return "redirect:/admin/digital/list";
    }
}