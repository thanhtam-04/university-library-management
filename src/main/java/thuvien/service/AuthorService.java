package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.Author;
import thuvien.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tác giả ID: " + id));
    }

    @Transactional
    public void save(Author author) {
        authorRepository.save(author);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    /**
     * Lấy danh sách tác giả nổi bật (dùng cho phần Featured)
     */
    @Transactional(readOnly = true)
    public List<Author> getFeaturedAuthors(int limit) {
        List<Author> allAuthors = authorRepository.findAll();
        
        // Có thể sắp xếp theo số lượng sách hoặc createdAt sau này
        // Hiện tại lấy ngẫu nhiên hoặc theo thứ tự ID
        return allAuthors.stream()
                .limit(limit)
                .toList();
    }

    public List<Author> getAll() {
        return authorRepository.findAll(); // Đổi từ "return null;" sang dòng này
    }
}