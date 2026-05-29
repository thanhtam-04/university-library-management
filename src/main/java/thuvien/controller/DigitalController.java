package thuvien.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import thuvien.entity.DigitalDocument;
import thuvien.service.DigitalDocumentService; // Giả định Nhi có Service này

@Controller
@RequiredArgsConstructor
public class DigitalController {

    private final DigitalDocumentService documentService;

    @GetMapping("/digital")
    public String digitalPage(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "type", required = false) String type,
            Model model) {

        // 1. Xử lý tìm kiếm và lọc tài liệu
        // Nếu có từ khóa hoặc loại tài liệu, gọi hàm tìm kiếm. Nếu không lấy tất cả.
        var documents = documentService.searchDocuments(query, type);

        // 2. Gửi dữ liệu sang giao diện
        model.addAttribute("documents", documents);
        model.addAttribute("query", query);
        model.addAttribute("selectedType", type);
        
        // Trả về file HTML (Nhi kiểm tra đường dẫn file templates/views/digital.html hay templates/digital.html)
        return "digital"; 
    }
    @GetMapping("/digital/download/{id}")
    public String downloadDocument(@PathVariable Long id) {
        // 1. Tăng lượt tải trong DB
       
        
        // 2. Lấy thông tin tài liệu để lấy URL
        DigitalDocument doc = documentService.getById(id);
        
        // 3. Chuyển hướng đến file thực tế
        return "redirect:" + (doc != null ? doc.getFileUrl() : "/digital");
    }
    
    
}