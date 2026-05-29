CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_db;

-- Nhà xuất bản
CREATE TABLE publishers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tác giả
CREATE TABLE authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    bio TEXT,
    nationality VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Danh mục sách
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    parent_id BIGINT NULL,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Đầu sách (book title)
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(500) NOT NULL,
    publisher_id BIGINT,
    category_id BIGINT,
    publication_year INT,
    edition VARCHAR(50),
    language VARCHAR(50) DEFAULT 'Tiếng Việt',
    total_copies INT DEFAULT 0,
    available_copies INT DEFAULT 0,
    shelf_location VARCHAR(50),
    cover_image VARCHAR(500),
    description TEXT,
    price DECIMAL(12,2),
    deposit_fee DECIMAL(12,2) DEFAULT 0,  -- phí cọc
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES publishers(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Bảng quan hệ sách - tác giả (nhiều-nhiều)
CREATE TABLE book_authors (
    book_id BIGINT,
    author_id BIGINT,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (author_id) REFERENCES authors(id)
);

-- Bản sao vật lý từng quyển
CREATE TABLE book_copies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    barcode VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('AVAILABLE','BORROWED','LOST','DAMAGED','MAINTENANCE') DEFAULT 'AVAILABLE',
    condition_note TEXT,
    acquired_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Phân quyền người dùng
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ROLE_ADMIN','ROLE_LIBRARIAN','ROLE_STUDENT') NOT NULL UNIQUE
);

-- Tài khoản người dùng
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    avatar VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Thẻ sinh viên / bạn đọc
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    student_code VARCHAR(20) UNIQUE,
    department VARCHAR(200),
    course VARCHAR(50),
    card_issued_date DATE NOT NULL,
    card_expiry_date DATE NOT NULL,
    max_borrow_limit INT DEFAULT 5,
    status ENUM('ACTIVE','SUSPENDED','EXPIRED') DEFAULT 'ACTIVE',
    total_borrowed INT DEFAULT 0,
    current_debt DECIMAL(12,2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Phiếu mượn sách
CREATE TABLE loans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_code VARCHAR(30) UNIQUE NOT NULL,
    member_id BIGINT NOT NULL,
    librarian_id BIGINT NOT NULL,
    loan_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    due_date DATE NOT NULL,
    return_date DATETIME NULL,
    status ENUM('ACTIVE','RETURNED','OVERDUE','LOST') DEFAULT 'ACTIVE',
    deposit_paid DECIMAL(12,2) DEFAULT 0,     -- tiền cọc đã nộp
    deposit_refunded DECIMAL(12,2) DEFAULT 0, -- tiền cọc đã hoàn
    note TEXT,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (librarian_id) REFERENCES users(id)
);

-- Chi tiết từng quyển trong phiếu mượn
CREATE TABLE loan_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id BIGINT NOT NULL,
    book_copy_id BIGINT NOT NULL,
    returned BOOLEAN DEFAULT FALSE,
    return_date DATETIME NULL,
    FOREIGN KEY (loan_id) REFERENCES loans(id),
    FOREIGN KEY (book_copy_id) REFERENCES book_copies(id)
);

-- Phiếu phạt quá hạn
CREATE TABLE fines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    fine_amount DECIMAL(12,2) NOT NULL,
    days_overdue INT NOT NULL,
    fine_per_day DECIMAL(10,2) DEFAULT 2000,
    reason VARCHAR(500),
    status ENUM('UNPAID','PAID','WAIVED') DEFAULT 'UNPAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_at DATETIME NULL,
    paid_by BIGINT NULL,  -- librarian id
    FOREIGN KEY (loan_id) REFERENCES loans(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (paid_by) REFERENCES users(id)
);

-- Lịch sử thanh toán
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    fine_id BIGINT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_type ENUM('FINE','DEPOSIT','DEPOSIT_REFUND') NOT NULL,
    payment_method ENUM('CASH','TRANSFER') DEFAULT 'CASH',
    note TEXT,
    processed_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (fine_id) REFERENCES fines(id),
    FOREIGN KEY (processed_by) REFERENCES users(id)
);

-- Dữ liệu mặc định
INSERT INTO roles (name) VALUES ('ROLE_ADMIN'),('ROLE_LIBRARIAN'),('ROLE_STUDENT');
INSERT INTO categories (name) VALUES 
('Công nghệ thông tin'),('Toán học'),('Vật lý'),('Hóa học'),
('Kinh tế'),('Luật'),('Y học'),('Văn học'),('Lịch sử');
INSERT INTO publishers (name, address, phone, email, website) VALUES 
('NXB Trẻ', '161B Lý Chính Thắng, Quận 3, TP.HCM', '02839316211', 'hopthu@nxbtre.com.vn', 'https://www.nxbtre.com.vn'),
('NXB Giáo Dục', '81 Trần Hưng Đạo, Hà Nội', '02438220801', 'contact@nxbgd.vn', 'http://www.nxbgd.vn'),
('NXB Kim Đồng', '55 Quang Trung, Hai Bà Trưng, Hà Nội', '1900571595', 'cskh@nxbkimdong.com.vn', 'https://nxbkimdong.com.vn');
INSERT INTO authors (full_name, bio, nationality) VALUES 
('Nguyễn Nhật Ánh', 'Nhà văn chuyên viết cho tuổi mới lớn.', 'Việt Nam'),
('Robert C. Martin', 'Tác giả sách Clean Code nổi tiếng thế giới.', 'USA'),
('Nam Cao', 'Nhà văn hiện thực phê phán xuất sắc.', 'Việt Nam');
INSERT INTO books (isbn, title, publisher_id, category_id, publication_year, total_copies, available_copies, price, shelf_location) VALUES 
('978604118', 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 1, 8, 2023, 5, 5, 85000.00, 'Kệ A1-01'),
('978013235', 'Clean Code', 2, 1, 2008, 3, 3, 450000.00, 'Kệ CNTT-05'),
('978604222', 'Dế Mèn Phiêu Lưu Ký', 3, 8, 2022, 10, 10, 55000.00, 'Kệ A2-03');
INSERT INTO book_copies (book_id, barcode, status, condition_note) VALUES 
(1, 'BAR-CAH-001', 'AVAILABLE', 'Sách mới nhập'),
(1, 'BAR-CAH-002', 'AVAILABLE', 'Sách mới nhập'),
(2, 'BAR-CC-001', 'AVAILABLE', 'Nguyên seal'),
(3, 'BAR-DM-001', 'DAMAGED', 'Hơi cũ, rách trang cuối');
-- Password mẫu '123456' (sau này dùng Spring Security sẽ cần mã hóa BCrypt sau)
INSERT INTO users (username, password, full_name, email, phone) VALUES 
('admin', '123456', 'Quản trị viên Nhi', 'nhi@university.edu.vn', '0912345678'),
('thuthu01', '123456', 'Nguyễn Văn A', 'vana@university.edu.vn', '0987654321');

-- Gán quyền Admin cho user 1
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (2, 2);
INSERT INTO members (
    user_id, 
    card_number, 
    student_code, 
    department, 
    course, 
    card_issued_date, 
    card_expiry_date, 
    max_borrow_limit, 
    status, 
    total_borrowed, 
    current_debt
) VALUES 
(
    1, 
    'LIB-2026-001', 
    'SV2101001', 
    'Công nghệ thông tin', 
    'K64', 
    '2024-10-01', 
    '2028-10-01', 
    5, 
    'ACTIVE', 
    3, 
    0.00
);
INSERT INTO categories (name, description) VALUES
('Công nghệ thông tin1', 'Sách khoa học máy tính, lập trình, an toàn thông tin'),
('Kinh tế', 'Sách kinh tế học, tài chính doanh nghiệp, quản trị'),
('Văn học', 'Sách tiểu thuyết, truyện ngắn, thơ ca trong và ngoài nước'),
('Toán học', 'Sách giáo trình toán cao cấp, toán ứng dụng'),
('Vật lý', 'Sách nghiên cứu vật lý lý thuyết và ứng dụng'),
('Hóa học', 'Sách hóa học cơ bản, hóa hữu cơ và vô cơ'),
('Luật', 'Sách văn bản luật, giáo trình luật dân sự, hình sự'),
('Y học', 'Sách nghiên cứu y khoa, chăm sóc sức khỏe, tự truyện y học'),
('Lịch sử', 'Sách lịch sử Việt Nam và lịch sử thế giới'),
('Triết học', 'Sách triết học phương Đông, phương Tây và tư tưởng'),
('Tâm lý học', 'Sách nghiên cứu tâm lý hành vi, tâm lý học xã hội'),
('Ngoại ngữ', 'Sách giáo trình, tài liệu luyện thi chứng chỉ tiếng Anh, Trung, Nhật'),
('Âm nhạc - Nghệ thuật', 'Sách lịch sử hội họa, âm nhạc, điêu khắc'),
('Thể thao - Sức khỏe', 'Sách hướng dẫn rèn luyện thể chất, dinh dưỡng'),
('Nông nghiệp', 'Sách kỹ thuật canh tác, nông nghiệp tự nhiên'),
('Môi trường', 'Sách nghiên cứu sinh thái, bảo vệ môi trường toàn cầu'),
('Chính trị', 'Sách lý luận chính trị, khoa học chính trị kinh điển'),
('Xã hội học', 'Sách nghiên cứu cấu trúc, xu hướng xã hội'),
('Du lịch', 'Sách cẩm nang, du ký, khám phá văn hóa các vùng miền'),
('Báo chí - Truyền thông', 'Sách kỹ thuật viết báo, truyền thông đại chúng'),
('Điện tử - Viễn thông', 'Sách kỹ thuật mạch, phần cứng, mạng viễn thông'),
('Xây dựng - Kiến trúc', 'Sách thiết kế không gian, kỹ thuật thi công công trình'),
('Thực phẩm - Đồ uống', 'Văn hóa ẩm thực, nghệ thuật pha chế, thưởng trà'),
('Thời trang - May mặc', 'Lịch sử thiết kế, xu hướng và kỹ thuật may mặc');
INSERT INTO publishers (name, address, phone, email, website) VALUES
('NXB Trẻ', '161B Lý Chính Thắng, Q3, TP.HCM', '02839316289', 'hopthu@nxbtre.com.vn', 'www.nxbtre.com.vn'),
('NXB Giáo Dục Việt Nam', '81 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', '02438220801', 'lienhe@nxbgianduc.vn', 'www.nxbgd.vn'),
('NXB Kim Đồng', '55 Quang Trung, Hai Bà Trưng, Hà Nội', '02439434730', 'info@nxbkimdong.com.vn', 'www.nxbkimdong.com.vn'),
('NXB Thông Tin Và Truyền Thông', '115 Trần Duy Hưng, Cầu Giấy, Hà Nội', '02435563875', 'nxb.tttt@mic.gov.vn', 'www.nxbthongtintruyenthong.vn'),
('NXB Thế Giới', '46 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', '02438253841', 'thegioi@hn.vnn.vn', 'www.nxbthegioi.vn'),
('NXB Hội Nhà Văn', '65 Nguyễn Du, Hai Bà Trưng, Hà Nội', '02438222135', 'nxbhoinhavan@gmail.com', 'www.nxbhoinhavan.vn'),
('NXB Chính Trị Quốc Gia Sự Thật', '6/86 Duy Tân, Cầu Giấy, Hà Nội', '02438221581', 'suthat@nxbctqg.vn', 'www.nxbctqg.org.vn'),
('NXB Tổng Hợp TP.HCM', '62 Nguyễn Thị Minh Khai, Q1, TP.HCM', '02838225340', 'tonghop@nxbhcm.com.vn', 'www.nxbhcm.com.vn'),
('NXB Lao Động', '175 Tây Sơn, Đống Đa, Hà Nội', '02438515380', 'nxblaodong@yahoo.com', 'www.nxblaodong.com.vn');
INSERT INTO authors (full_name, bio, nationality) VALUES
('Nguyễn Nhật Ánh', 'Nhà văn nổi tiếng tại Việt Nam với các tác phẩm tuổi học trò', 'Việt Nam'),
('Robert C. Martin', 'Chuyên gia phần mềm kiến trúc sạch (Uncle Bob)', 'Mỹ'),
('Nguyễn Du', 'Đại thi hào dân tộc Việt Nam', 'Việt Nam'),
('Paulo Coelho', 'Tiểu thuyết gia nổi tiếng người Brazil', 'Brazil'),
('Yuval Noah Harari', 'Nhà nghiên cứu lịch sử triết học người Israel', 'Israel'),
('Dale Carnegie', 'Nhà văn, nhà thuyết trình phát triển bản thân', 'Mỹ'),
('J.K. Rowling', 'Tác giả loạt truyện phù thủy Harry Potter', 'Anh'),
('Cal Newport', 'Giáo sư khoa học máy tính chuyên nghiên cứu về sự tập trung', 'Mỹ'),
('Angela Duckworth', 'Nhà tâm lý học nghiên cứu về sự kiên trì', 'Mỹ'),
('John Zelle', 'Tác giả sách giáo trình lập trình Python kinh điển', 'Mỹ'),
('Richard H. Thaler', 'Nhà kinh tế học đoạt giải Nobel', 'Mỹ'),
('George Orwell', 'Nhà văn tiểu thuyết viễn tưởng chính trị xuất sắc', 'Anh'),
('James Clear', 'Chuyên gia nghiên cứu về xây dựng thói quen', 'Mỹ'),
('Trần Trọng Kim', 'Nhà giáo dục, nhà sử học danh tiếng', 'Việt Nam'),
('Andrew Ng', 'Chuyên gia hàng đầu thế giới về Trí tuệ nhân tạo', 'Mỹ'),
('Nguyễn Hải Thanh', 'Giáo sư giảng dạy toán ứng dụng tại Việt Nam', 'Việt Nam'),
('Lê Đình Thúy', 'Tác giả giáo trình đại số tuyến tính đại học', 'Việt Nam'),
('Stephen Hawking', 'Nhà vật lý lý thuyết, vũ trụ học thiên tài', 'Anh'),
('Ngô Thị Thuận', 'Giáo sư đầu ngành hóa học hữu cơ Việt Nam', 'Việt Nam'),
('Đại học Luật Hà Nội', 'Tập thể tác giả biên soạn giáo trình chuẩn pháp lý', 'Việt Nam'),
('Paul Kalanithi', 'Bác sĩ phẫu thuật não kiêm nhà văn đầy nghị lực', 'Mỹ'),
('Jostein Gaarder', 'Nhà văn chuyên viết truyện triết học cho giới trẻ', 'Na Uy'),
('Gustave Le Bon', 'Nhà tâm lý học xã hội học nổi tiếng người Pháp', 'Pháp'),
('Cambridge University Press', 'Hội đồng khảo thí và xuất bản đại học Cambridge', 'Anh'),
('E.H. Gombrich', 'Nhà sử học nghệ thuật danh tiếng thế giới', 'Áo'),
('Ray D. Strand', 'Bác sĩ chuyên khoa nghiên cứu y học dinh dưỡng', 'Mỹ'),
('Masanobu Fukuoka', 'Triết gia kiêm nhà nông học tự nhiên', 'Nhật Bản'),
('Rachel Carson', 'Nhà sinh vật học tiên phong bảo vệ môi trường', 'Mỹ'),
('Niccolò Machiavelli', 'Nhà triết học chính trị kinh điển thời Phục Hưng', 'Ý'),
('Malcolm Gladwell', 'Nhà viết sách phân tích xu hướng xã hội học', 'Mỹ'),
('Huyền Chip', 'Tác giả sách ký sự hành trình du lịch nổi tiếng', 'Việt Nam'),
('Hữu Thọ', 'Nhà báo lão thành cách mạng, nguyên Tổng biên tập báo Nhân Dân', 'Việt Nam'),
('Đỗ Thanh Việt', 'Giáo sư giảng dạy kỹ thuật điện tử công nghệ', 'Việt Nam'),
('Nguyễn Đức Thiềm', 'KTS, tác giả sách cơ sở kiến trúc Việt Nam', 'Việt Nam'),
('Nguyễn Ngọc Tuấn', 'Nghệ nhân, chuyên gia nghiên cứu văn hóa trà', 'Việt Nam'),
('Đặng Thúy Hương', 'Nhà thiết kế, giảng viên mỹ thuật thời trang', 'Việt Nam');
INSERT INTO books (isbn, title, publisher_id, category_id, publication_year, total_copies, available_copies, price, shelf_location, cover_image, description) VALUES
-- Văn học (category_id = 3)
('978604118', 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 1, 3, 2021, 4, 2, 85000.00, 'Kệ A1-01', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', 'Tác phẩm xuất sắc của Nguyễn Nhật Ánh'),
('978604222', 'Dế Mèn Phiêu Lưu Ký', 3, 3, 2022, 10, 10, 55000.00, 'Kệ A2-03', '/uploads/books/1778858260472_demenphieuluuky.jpg', 'Truyện thiếu nhi kinh điển của Tô Hoài'),
('9786041234586', 'Nhà Giả Kim', 9, 3, 2022, 12, 10, 65000.00, 'Kệ VH-04', '/uploads/books/177885766786_nhagiakim.jpg', 'Tác phẩm nổi tiếng của Paulo Coelho'),
('9786041234589', 'Harry Potter và Phòng Chứa Bí Mật', 6, 3, 2023, 10, 8, 120000.00, 'Kệ VH-05', '/uploads/books/1778858976638_HarryPotter.jpg', 'Phần 2 của series Harry Potter'),
('9786041234592', 'Truyện Kiều', 9, 3, 2022, 6, 5, 45000.00, 'Kệ VH-06', '/uploads/books/177885766786_nhagiakim.jpg', 'Tác phẩm văn học trung đại kinh điển'),
('9786041234595', '1984', 9, 3, 2022, 5, 4, 95000.00, 'Kệ VH-07', '/uploads/books/177885766786_nhagiakim.jpg', 'Tiểu thuyết viễn tưởng của George Orwell'),
('9786041234599', 'Nóng Trong Lòng', 3, 3, 2023, 8, 7, 55000.00, 'Kệ VH-08', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', 'Tác phẩm khác của Nguyễn Nhật Ánh'),

-- Công nghệ thông tin1 (category_id = 1)
('978013235', 'Clean Code', 4, 1, 2020, 3, 0, 320000.00, 'Kệ CNTT-05', '/uploads/books/1778858079273_Clean Code.jpg', 'Mã sạch và nghệ thuật viết phần mềm'),
('9786041234593', 'Python Programming', 2, 1, 2024, 10, 8, 175000.00, 'Kệ CNTT-06', '/uploads/books/1778858079273_Clean Code.jpg', 'Học Python cơ bản'),
('9786041234598', 'Clean Architecture', 4, 1, 2017, 6, 4, 450000.00, 'Kệ CNTT-07', '/uploads/books/1778858079273_Clean Code.jpg', 'Kiến trúc phần mềm sạch'),
('9786041234600', 'Machine Learning Yearning', 2, 1, 2024, 7, 5, 220000.00, 'Kệ CNTT-09', '/uploads/books/1778858079273_Clean Code.jpg', 'Cấu trúc dự án Trí tuệ nhân tạo'),

-- Kinh tế (category_id = 2)
('9786041234587', 'Sapiens - Lược Sử Loài Người', 5, 2, 2023, 8, 6, 195000.00, 'Kệ XH-01', '/uploads/books/1778858891552_Sapiens.jpg', 'Lịch sử phát triển nhân loại'),
('9786041234588', 'Đắc Nhân Tâm', 8, 2, 2021, 15, 12, 85000.00, 'Kệ KNS-02', '/uploads/books/1778858450856_dacnhantam.jpg', 'Kinh điển nghệ thuật thu phục lòng người'),
('9786041234590', 'Deep Work', 2, 2, 2024, 7, 5, 135000.00, 'Kệ KNS-03', '/uploads/books/1778858497071_DeepWork.jpg', 'Kỹ năng làm việc tập trung sâu'),
('9786041234591', 'Grit - Sức Mạnh Của Đam Mê', 5, 2, 2023, 9, 7, 145000.00, 'Kệ KNS-04', '/uploads/books/1778859082984_Grit.jpg', 'Bí mật của sự thành công bền bỉ'),
('9786041234594', 'Kinh Tế Học Hành Vi', 1, 2, 2023, 8, 6, 155000.00, 'Kệ KT-06', '/uploads/books/1778858891552_Sapiens.jpg', 'Phân tích hành vi kinh tế'),
('9786041234596', 'Atomic Habits', 5, 2, 2024, 11, 9, 165000.00, 'Kệ KNS-05', '/uploads/books/1778858497071_DeepWork.jpg', 'Xây dựng thói quen nhỏ'),

-- Toán học (category_id = 4)
('9786041234601', 'Cơ Sở Toán Học Cho AI', 2, 4, 2024, 5, 5, 120000.00, 'Kệ TOAN-01', '/uploads/books/1778858079273_Clean Code.jpg', 'Toán học nền tảng trí tuệ nhân tạo'),
('9786041234602', 'Đại Số Tuyến Tính', 1, 4, 2023, 10, 9, 95000.00, 'Kệ TOAN-02', '/uploads/books/1778858079273_Clean Code.jpg', 'Giáo trình đại số tuyến tính căn bản'),

-- Vật lý (category_id = 5)
('9786041234603', 'Lược Sử Thời Gian', 5, 5, 2022, 6, 6, 115000.00, 'Kệ VLY-01', '/uploads/books/177885766786_nhagiakim.jpg', 'Tìm hiểu vũ trụ cùng Stephen Hawking'),

-- Hóa học (category_id = 6)
('9786041234604', 'Hóa Học Hữu Cơ Căn Bản', 3, 6, 2021, 8, 7, 85000.00, 'Kệ HOA-01', '/uploads/books/1778858079273_Clean Code.jpg', 'Giáo trình hóa hữu cơ căn bản'),

-- Luật (category_id = 7)
('9786041234605', 'Giáo Trình Luật Dân Sự Việt Nam', 7, 7, 2023, 12, 11, 110000.00, 'Kệ LUAT-01', '/uploads/books/1778858079273_Clean Code.jpg', 'Học phần luật dân sự'),

-- Y học (category_id = 8)
('9786041234606', 'Khi Hơi Thở Hóa Thinh Không', 9, 8, 2022, 7, 6, 120000.00, 'Kệ YHOC-01', '/uploads/books/177885766786_nhagiakim.jpg', 'Tự truyện y khoa đầy cảm xúc'),

-- Lịch sử (category_id = 9)
('9786041234597', 'Lịch Sử Việt Nam', 7, 9, 2023, 10, 8, 120000.00, 'Kệ LS-01', '/uploads/books/1778858260472_demenphieuluuky.jpg', 'Tổng quan tiến trình lịch sử'),
('9786041234607', 'Việt Nam Sử Lược', 9, 9, 2021, 5, 5, 145000.00, 'Kệ LS-02', '/uploads/books/1778858260472_demenphieuluuky.jpg', 'Tác phẩm sử học của Trần Trọng Kim'),

-- Triết học (category_id = 10)
('9786041234608', 'Thế Giới Của Sophie', 6, 10, 2023, 6, 4, 175000.00, 'Kệ TRIET-01', '/uploads/books/177885766786_nhagiakim.jpg', 'Lịch sử triết học phương Tây'),

-- Tâm lý học (category_id = 11)
('9786041234609', 'Tâm Lý Học Đám Đông', 5, 11, 2024, 14, 12, 95000.00, 'Kệ TLY-01', '/uploads/books/1778858450856_dacnhantam.jpg', 'Nghiên cứu hành vi đám đông'),

-- Ngoại ngữ (category_id = 12)
('9786041234610', 'Cambridge IELTS 18', 4, 12, 2023, 20, 18, 240000.00, 'Kệ NNGU-01', '/uploads/books/1778858497071_DeepWork.jpg', 'Đề thi thử IELTS chuẩn Cambridge'),

-- Âm nhạc - Nghệ thuật (category_id = 13)
('9786041234611', 'Câu Chuyện Nghệ Thuật', 8, 13, 2022, 4, 4, 450000.00, 'Kệ MNET-01', '/uploads/books/1778859976638_HarryPotter.jpg', 'Lịch sử nghệ thuật hội họa thế giới'),

-- Thể thao - Sức khỏe (category_id = 14)
('9786041234612', 'Y Học Dinh Dưỡng', 1, 14, 2023, 9, 9, 130000.00, 'Kệ TTSK-01', '/uploads/books/1778859082984_Grit.jpg', 'Ăn uống khoa học nâng cao sức khỏe'),

-- Nông nghiệp (category_id = 15)
('9786041234613', 'Cách Mạng Một Cọng Rơm', 5, 15, 2021, 8, 8, 88000.00, 'Kệ NN-01', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', 'Phương pháp làm nông tự nhiên'),

-- Môi trường (category_id = 16)
('9786041234614', 'Mùa Xuân Vắng Lặng', 2, 16, 2022, 5, 5, 125000.00, 'Kệ MTR-01', '/uploads/books/1778858891552_Sapiens.jpg', 'Tác động của hóa chất lên hệ sinh thái'),

-- Chính trị (category_id = 17)
('9786041234615', 'Quân Vương', 9, 17, 2023, 6, 6, 75000.00, 'Kệ CT-01', '/uploads/books/177885766786_nhagiakim.jpg', 'Nghệ thuật cai trị kinh điển'),

-- Xã hội học (category_id = 18)
('9786041234616', 'Điểm Bùng Phát', 5, 18, 2024, 7, 6, 140000.00, 'Kệ XHH-01', '/uploads/books/1778858891552_Sapiens.jpg', 'Cơ chế lan truyền các hiện tượng xã hội'),

-- Du lịch (category_id = 19)
('9786041234617', 'Xách Ba Lô Lên Và Đi', 3, 19, 2022, 10, 10, 95000.00, 'Kệ DL-01', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', 'Ký sự du ký của tuổi trẻ'),

-- Báo chí - Truyền thông (category_id = 20)
('9786041234618', 'Nghề Báo Gian Khổ Quyến Rũ', 7, 20, 2021, 4, 4, 110000.00, 'Kệ BCTT-01', '/uploads/books/1778858260472_demenphieuluuky.jpg', 'Tâm sự chuyện làm báo'),

-- Điện tử - Viễn thông (category_id = 21)
('9786041234619', 'Kỹ Thuật Mạch Điện Tử', 2, 21, 2023, 12, 11, 115000.00, 'Kệ DTVT-01', '/uploads/books/1778858079273_Clean Code.jpg', 'Cơ sở phần cứng điện tử'),

-- Xây dựng - Kiến trúc (category_id = 22)
('9786041234620', 'Ý Tưởng Kiến Trúc', 4, 22, 2022, 5, 5, 240000.00, 'Kệ XDKT-01', '/uploads/books/1778858079273_Clean Code.jpg', 'Tư duy thiết kế không gian'),

-- Thực phẩm - Đồ uống (category_id = 23)
('9786041234621', 'Nghệ Thuật Thưởng Trà', 3, 23, 2023, 6, 6, 180000.00, 'Kệ TPDU-01', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', 'Vàn hóa trà đạo Việt Nam'),

-- Thời trang - May mặc (category_id = 24)
('9786041234622', 'Lịch Sử Thiết Kế Thời Trang', 8, 24, 2024, 5, 4, 260000.00, 'Kệ TTR-01', '/uploads/books/1778858976638_HarryPotter.jpg', 'Sự phát triển của ngành thời trang thế giới');
INSERT INTO book_authors (book_id, author_id) VALUES 
(1, 1),   -- Cho Tôi Xin Một Vé Đi Tuổi Thơ -> Nguyễn Nhật Ánh
(2, 1),   -- Dế Mèn Phiêu Lưu Ký -> Nguyễn Nhật Ánh (Dùng tạm ID có sẵn)
(3, 4),   -- Nhà Giả Kim -> Paulo Coelho
(4, 7),   -- Harry Potter -> J.K. Rowling
(5, 3),   -- Truyện Kiều -> Nguyễn Du
(6, 12),  -- 1984 -> George Orwell
(7, 1),   -- Nóng Trong Lòng -> Nguyễn Nhật Ánh
(8, 2),   -- Clean Code -> Robert C. Martin
(9, 10),  -- Python Programming -> John Zelle
(10, 2),  -- Clean Architecture -> Robert C. Martin
(11, 15), -- Machine Learning Yearning -> Andrew Ng
(12, 5),  -- Sapiens -> Yuval Noah Harari
(13, 6),  -- Đắc Nhân Tâm -> Dale Carnegie
(14, 8),  -- Deep Work -> Cal Newport
(15, 9),  -- Grit -> Angela Duckworth
(16, 11), -- Kinh Tế Học Hành Vi -> Richard H. Thaler
(17, 13), -- Atomic Habits -> James Clear
(18, 16), -- Cơ Sở Toán Học Cho AI -> Nguyễn Hải Thanh
(19, 17), -- Đại Số Tuyến Tính -> Lê Đình Thúy
(20, 18), -- Lược Sử Thời Gian -> Stephen Hawking
(21, 19), -- Hóa Học Hữu Cơ Căn Bản -> Ngô Thị Thuận
(22, 20), -- Giáo Trình Luật Dân Sự Việt Nam -> Đại học Luật Hà Nội
(23, 21), -- Khi Hơi Thở Hóa Thinh Không -> Paul Kalanithi
(24, 14), -- Lịch Sử Việt Nam -> Trần Trọng Kim
(25, 14), -- Việt Nam Sử Lược -> Trần Trọng Kim
(26, 22), -- Thế Giới Của Sophie -> Jostein Gaarder
(27, 23), -- Tâm Lý Học Đám Đông -> Gustave Le Bon
(28, 24), -- Cambridge IELTS 18 -> Cambridge University Press
(29, 25), -- Câu Chuyện Nghệ Thuật -> E.H. Gombrich
(30, 26), -- Y Học Dinh Dưỡng -> Ray D. Strand
(31, 27), -- Cách Mạng Một Cọng Rơm -> Masanobu Fukuoka
(32, 28), -- Mùa Xuân Vắng Lặng -> Rachel Carson
(33, 29), -- Quân Vương -> Niccolò Machiavelli
(34, 30), -- Điểm Bùng Phát -> Malcolm Gladwell
(35, 31), -- Xách Ba Lô Lên Và Đi -> Huyền Chip
(36, 32), -- Nghề Báo Gian Khổ Quyến Rũ -> Hữu Thọ
(37, 33), -- Kỹ Thuật Mạch Điện Tử -> Đỗ Thanh Việt
(38, 34), -- Ý Tưởng Kiến Trúc -> Nguyễn Đức Thiềm
(39, 35), -- Nghệ Thuật Thưởng Trà -> Nguyễn Ngọc Tuấn
(40, 36); -- Lịch Sử Thiết Kế Thời Trang -> Đặng Thúy Hương