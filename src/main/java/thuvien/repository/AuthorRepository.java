package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thuvien.entity.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    // Tìm kiếm theo tên (Cũ của Nhi - Giữ nguyên)
    List<Author> findByFullNameContainingIgnoreCase(String name);

    /**
     * BỔ SUNG: Truy vấn lấy tác giả kèm theo số lượng sách của họ.
     * Giúp xử lý triệt để việc hiển thị số lượng tác phẩm bằng 0.
     * (Yêu cầu: Nhi đã thêm Set<Book> books vào file Author.java như mình gửi trước đó)
     */
    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books")
    List<Author> findAllWithBooks();
}