package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import thuvien.entity.DigitalDocument;
import thuvien.repository.DigitalDocumentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DigitalDocumentService {

    private final DigitalDocumentRepository repository;
    // Đường dẫn này trỏ vào thư mục trong project
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/documents/";

    // ── Tìm kiếm ─────────
 // Trong DigitalDocumentService.java

    public List<DigitalDocument> searchDocuments(String q, String type) {
        String filterType = (type == null || type.isEmpty() || type.equals("all")) ? null : type;
        
        // Sửa lại chỗ này: Gọi đúng tên hàm là searchDigitalDocs
        return repository.searchDigitalDocs(q, filterType);
    }

    // ── CRUD cho Admin ────────────────────────────────────────

    public List<DigitalDocument> getAll() {
        return repository.findAll();
    }

    public DigitalDocument getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public DigitalDocument save(DigitalDocument document) {
        return repository.save(document);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // ── Xử lý File ───────────────────────────────────────────

    public String uploadAndGetUrl(MultipartFile file) {
        try {
            // Kiểm tra thư mục, nếu chưa có thì tạo mới
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Tạo tên file ngẫu nhiên để tránh trùng lặp
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            
            // Trả về đường dẫn để lưu vào database
            return "/uploads/documents/" + fileName; 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}