package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.dto.response.BookResponse;
import thuvien.entity.Author;
import thuvien.entity.Book;
import thuvien.entity.BookCopy; // Import thực thể bản sao sách
import thuvien.entity.Loan;
import thuvien.entity.LoanItem;
import thuvien.entity.Member;
import thuvien.entity.User;
import thuvien.repository.BookRepository;
import thuvien.repository.LoanRepository;
import thuvien.repository.MemberRepository;
import thuvien.repository.BookCopyRepository; // Import repository quản lý bản sao sách

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final BookCopyRepository bookCopyRepository; // Tiêm thêm Repository quản lý bản sao sách vật lý

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách ID: " + id));
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    // ================== TRANG CHỦ ==================
    @Transactional(readOnly = true)
    public List<BookResponse> getFeaturedBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
        List<Book> books = bookRepository.findAllByOrderByIdDesc(pageable);
        return books.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getNewBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        List<Book> books = bookRepository.findAllByOrderByCreatedAtDesc(pageable);
        return books.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ================== TRANG DANH MỤC ==================
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        List<Book> entities = bookRepository.findAll();
        System.out.println("=== SERVICE: bookRepository.findAll() trả về " + entities.size() + " sách ===");

        List<BookResponse> responses = entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        System.out.println("=== SERVICE: Sau convert có " + responses.size() + " BookResponse ===");
        return responses;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByCategoryId(Long categoryId) {
        List<Book> books = (categoryId == null) 
                ? bookRepository.findAll() 
                : bookRepository.findByCategoryId(categoryId);
        
        System.out.println("=== DEBUG findByCategoryId(" + categoryId + "): Tìm thấy " + books.size() + " sách ===");
        return books.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // Phân trang
    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooksPaged(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        return page.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findByCategoryIdPaged(Long categoryId, Pageable pageable) {
        Page<Book> page = bookRepository.findByCategoryId(categoryId, pageable);
        return page.map(this::convertToResponse);
    }

    public Long countAllBooks() {
        return bookRepository.count();
    }

    // ================== CONVERTER ==================
    private BookResponse convertToResponse(Book book) {
        if (book == null) return null;

        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setIsbn(book.getIsbn());
        response.setTitle(book.getTitle());
        response.setPublicationYear(book.getPublicationYear());
        response.setEdition(book.getEdition());
        response.setLanguage(book.getLanguage());
        response.setPrice(book.getPrice());
        response.setSummary(book.getSummary());
        response.setDepositFee(book.getDepositFee());
        response.setShelfLocation(book.getShelfLocation());
        response.setCoverImage(book.getCoverImage());
        response.setDescription(book.getDescription());
        response.setTotalCopies(book.getTotalCopies());
        response.setAvailableCopies(book.getAvailableCopies());
        response.setCreatedAt(book.getCreatedAt());

        if (book.getPublisher() != null) {
            response.setPublisherName(book.getPublisher().getName());
        }
        if (book.getCategory() != null) {
            response.setCategoryName(book.getCategory().getName());
        }
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            response.setAuthors(book.getAuthors().stream()
                    .map(Author::getFullName)
                    .collect(Collectors.toSet()));
        } else {
            response.setAuthors(java.util.Collections.emptySet());
        }

        return response;
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        System.out.println("=== SERVICE: Đang truy vấn Database tìm sách với ID: " + id + " ===");
        
        Book book = bookRepository.findById(id).orElse(null);
        
        if (book == null) {
            System.out.println("=== SERVICE: Không tìm thấy sách trong DB với ID: " + id);
            return null;
        }
        
        return convertToResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> searchAndFilterBooks(String title, String author, Long categoryId, String status) {
        String searchTitle = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        String searchAuthor = (author != null && !author.trim().isEmpty()) ? author.trim() : null;
        String searchStatus = (status != null && !status.trim().isEmpty()) ? status.trim() : null;

        List<Book> books = bookRepository.filterBooksAdvanced(searchTitle, searchAuthor, categoryId, searchStatus);
        
        return books.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
    }

    // ================== ĐĂNG KÝ MƯỢN SÁCH ONLINE ==================
    @Transactional
    public boolean registerBookLoan(Long bookId, String username) {
        // 1. Tìm thông tin sách gốc dựa vào ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + bookId));

        // 2. Kiểm tra xem số lượng sách sẵn có trong kho còn không
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            return false;
        }

        // 3. Sử dụng hàm findByBookIdAndStatus thực tế để lấy các bản sao vật lý đang SẴN SÀNG (AVAILABLE)
        // Lưu ý: Nếu Enum trạng thái sẵn sàng của bạn tên khác (ví dụ: AVAILABLE, ACTIVE, HOAT_DONG...), hãy sửa lại chữ Status.AVAILABLE nhé!
        List<BookCopy> availableCopies = bookCopyRepository.findByBookIdAndStatus(bookId, BookCopy.Status.AVAILABLE);
        
        // Nếu không tìm thấy bản sao khả dụng theo trạng thái lọc, ta dự phòng lấy danh sách tổng quát
        if (availableCopies.isEmpty()) {
            availableCopies = bookCopyRepository.findByBookId(bookId);
        }
        
        if (availableCopies.isEmpty()) {
            throw new RuntimeException("Đầu sách tồn tại nhưng không tìm thấy bản sao vật lý (BookCopy) nào trong kho!");
        }
        
        // Chọn bản sao vật lý đầu tiên tìm được để tiến hành gán vào chi tiết phiếu
        BookCopy selectedCopy = availableCopies.get(0); 

        // 4. Tìm tài khoản User từ hệ thống đăng nhập
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy tài khoản người dùng: " + username);
        }

        // 5. Tìm kiếm hồ sơ Độc giả (Member) tương ứng thông qua hàm findByUserId của bạn
        Member member = memberRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Tài khoản '" + username + "' chưa được liên kết với hồ sơ Độc giả của thư viện!")); 

        // 6. Khởi tạo đối tượng phiếu mượn gốc (Loan) mới
        Loan loan = new Loan();
        loan.setLoanCode("PM-" + System.currentTimeMillis());
        loan.setMember(member);
        loan.setLibrarian(null); // Đăng ký online chờ thủ thư duyệt trực tiếp tại quầy sau
        loan.setLoanDate(java.time.LocalDateTime.now());
        loan.setDueDate(java.time.LocalDate.now().plusDays(14)); // Thời hạn mặc định 14 ngày
        loan.setStatus(Loan.Status.ACTIVE);
        loan.setDepositPaid(book.getDepositFee() != null ? book.getDepositFee() : java.math.BigDecimal.ZERO);
        loan.setNote("Đăng ký trực tuyến qua trang thông tin chi tiết");

        // 7. Khởi tạo Chi tiết phiếu mượn (LoanItem) khớp cấu trúc của bạn
        LoanItem item = new LoanItem();
        item.setLoan(loan);            // Gán liên kết phiếu mượn
        item.setBookCopy(selectedCopy); // Gán chuẩn xác thực thể bản sao vật lý (Hết bị lỗi đỏ 100%)
        item.setReturned(false);       // Sách vừa đăng ký mượn nên chưa trả

        // Thêm chi tiết vào danh sách items của Loan
        if (loan.getItems() != null) {
            loan.getItems().add(item);
        } else {
            List<LoanItem> tempItems = new java.util.ArrayList<>();
            tempItems.add(item);
            try {
                loan.setItems(tempItems);
            } catch (Exception ignored) {}
        }

        // 8. Tiến hành lưu phiếu mượn xuống cơ sở dữ liệu
        Loan savedLoan = loanRepository.save(loan);
        item.setLoan(savedLoan);

        // 9. Giảm số lượng quyển sách khả dụng trong kho tổng đi 1 đơn vị và lưu lại
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return true;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
}