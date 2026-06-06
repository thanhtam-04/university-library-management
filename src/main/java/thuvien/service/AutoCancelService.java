package thuvien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.Loan;
import thuvien.repository.LoanRepository;
import thuvien.repository.BookRepository;
import thuvien.entity.Book;
import thuvien.entity.LoanItem;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoCancelService {

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private BookRepository bookRepository;

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void autoCancelPendingLoans() {
        // Test: Hủy những phiếu PENDING đã tạo quá 5 phút minusDays(3)minusMinutes(5)
        LocalDateTime threshold = LocalDateTime.now().minusDays(3);
        List<Loan> overduePendingLoans = loanRepository.findByStatusAndLoanDateBefore(Loan.Status.PENDING, threshold);

        if (!overduePendingLoans.isEmpty()) {
            System.out.println("Đang tìm thấy " + overduePendingLoans.size() + " phiếu cần hủy...");
            
            for (Loan loan : overduePendingLoans) {
                loan.setStatus(Loan.Status.CANCELLED);
                loan.setNote("Hệ thống tự động hủy do quá hạn 3 ngày.");

                // TRẢ SÁCH VÀO KHO
                for (LoanItem item : loan.getItems()) {
                    // Lấy thông tin sách từ LoanItem -> BookCopy -> Book
                    Book book = item.getBookCopy().getBook();
                    
                    // Tăng số lượng sách có sẵn trở lại
                    int newAvailable = book.getAvailableCopies() + 1;
                    book.setAvailableCopies(newAvailable);
                    
                    bookRepository.save(book);
                }
                
                // Lưu lại trạng thái phiếu đã hủy
                loanRepository.save(loan);
                System.out.println("Đã hủy phiếu: " + loan.getLoanCode());
            }
        }
    }
}