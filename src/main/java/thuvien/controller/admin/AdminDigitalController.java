package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.DigitalDocument;
import thuvien.service.DigitalDocumentService;

@Controller
@RequestMapping("/admin/digital")
@RequiredArgsConstructor
public class AdminDigitalController {

    private final DigitalDocumentService documentService;

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
    public String addForm(Model model) {
        model.addAttribute("document", new DigitalDocument());
        model.addAttribute("activePage", "digital");
        return "views/admin/digital/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        DigitalDocument doc = documentService.getById(id);
        if (doc == null) return "redirect:/admin/digital/list";

        // QUAN TRỌNG: Truyền thêm biến này để trang HTML nhận diện
        model.addAttribute("document", doc);
        model.addAttribute("currentFile", doc.getFileUrl()); 
        model.addAttribute("activePage", "digital");
        return "views/admin/digital/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DigitalDocument document,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       RedirectAttributes ra) {

        System.out.println("=== DEBUG SAVE ===");
        System.out.println("ID: " + document.getId());
        System.out.println("Incoming fileUrl: [" + document.getFileUrl() + "]");

        if (file != null && !file.isEmpty()) {
            String newFileUrl = documentService.uploadAndGetUrl(file);
            if (newFileUrl != null) {
                document.setFileUrl(newFileUrl);
                System.out.println("→ ĐÃ CẬP NHẬT fileUrl mới: " + newFileUrl);
            } else {
                System.out.println("→ Upload thất bại!");
            }
        } else {
            System.out.println("→ Giữ nguyên file cũ");
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