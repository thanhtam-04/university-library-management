package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.Book;
import thuvien.entity.Member;
import thuvien.entity.Reservation;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Tìm người đặt sớm nhất theo thời gian của cuốn sách đó
    Reservation findFirstByBookAndStatusOrderByReservationDateAsc(Book book, String status);

    // Kiểm tra tồn tại dựa trên danh sách trạng thái
    boolean existsByMemberAndBookAndStatusIn(Member member, Book book, List<String> statuses);

    // Phục vụ việc hiển thị danh sách "Đợi sách"
    List<Reservation> findByMemberId(Long memberId);
}