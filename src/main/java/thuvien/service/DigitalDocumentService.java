package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import thuvien.entity.DigitalDocument;
import thuvien.repository.DigitalDocumentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DigitalDocumentService {

    private final DigitalDocumentRepository repository;

    // ĐÃ SỬA: Đưa đường dẫn lưu file vào đúng thư mục static bên trong src theo cấu trúc cũ của bạn
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/documents/";

    // ── Tìm kiếm ─────────
    public List<DigitalDocument> searchDocuments(String q, String type) {
        String filterType = (type == null || type.isEmpty() || type.equals("all")) ? null : type;
        return repository.searchDigitalDocs(q, filterType);
    }

    // ── CRUD ─────────────
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

    // ── Xử lý File ───────
    public String uploadAndGetUrl(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 1. Lấy tên file gốc
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) originalFileName = "document.pdf";
            
            // 2. KHỬ SẠCH DẤU TIẾNG VIỆT & KHOẢNG TRẮNG (Giúp trình duyệt không bị lỗi link %20)
            String cleanFileName = originalFileName.toLowerCase();
            cleanFileName = Normalizer.normalize(cleanFileName, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            cleanFileName = pattern.matcher(cleanFileName).replaceAll("");
            cleanFileName = cleanFileName.replaceAll("đ", "d");
            cleanFileName = cleanFileName.replaceAll("\\s+", "-"); // Thay khoảng trắng bằng dấu -
            cleanFileName = cleanFileName.replaceAll("[^a-z0-9_.-]", ""); // Bỏ toàn bộ ký tự lạ
            
            // 3. Tạo tên file bằng UUID tránh trùng
            String fileName = UUID.randomUUID().toString() + "_" + cleanFileName;
            Path filePath = uploadPath.resolve(fileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Đường dẫn URL trả về để khớp với /uploads/** của WebConfig
            return "/uploads/documents/" + fileName; 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}