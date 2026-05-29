-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               12.2.2-MariaDB - MariaDB Server
-- Server OS:                    Win64
-- HeidiSQL Version:             12.14.0.7165
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for library_db
CREATE DATABASE IF NOT EXISTS `library_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `library_db`;

-- Dumping structure for table library_db.authors
CREATE TABLE IF NOT EXISTS `authors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(200) NOT NULL,
  `bio` text DEFAULT NULL,
  `nationality` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `pseudonym` varchar(100) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT 'default-author.png',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.authors: ~36 rows (approximately)
INSERT INTO `authors` (`id`, `full_name`, `bio`, `nationality`, `created_at`, `pseudonym`, `image_url`) VALUES
	(1, 'Nguyễn Nhật Ánh', 'Nhà văn, nhà báo, tiền bối xuất sắc kiêm biểu tượng lớn trong nền văn học hiện đại Việt Nam. Ông tốt nghiệp chuyên ngành Sư phạm, từng có thời gian làm giáo viên trước khi chuyển hẳn sang sự nghiệp báo chí và sáng tác văn học chuyên nghiệp. Với hơn 40 năm cầm bút, ông đặc biệt thành công vang dội ở các tác phẩm khai thác sâu sắc tâm lý tuổi học trò, thế giới tuổi thơ hồn nhiên và những rung động đầu đời của tuổi mới lớn. Văn phong của ông mộc mạc, tinh tế, giàu chất thơ và thấm đượm giá trị nhân văn sâu sắc. Ông từng vinh dự nhận Giải thưởng Văn học ASEAN năm 2010. Rất nhiều tác phẩm truyện dài của ông đã trở thành hiện tượng xuất bản toàn quốc, được dịch ra nhiều ngôn ngữ trên thế giới và chuyển thể thành các bộ phim điện ảnh bom tấn đạt doanh thu kỷ lục như Mắt Biếc, Tôi Thấy Hoa Vàng Trên Cỏ Xanh, Cho Tôi Xin Một Vé Đi Tuổi Thơ, Cô Gái Đến Từ Hôm Qua.', 'Việt Nam', '2026-05-19 17:12:32', '', '513c269f-b860-4776-a88b-4abf72912bca.jpg'),
	(2, 'Robert C. Martin', 'Thường được cộng đồng công nghệ toàn cầu gọi với tên tôn kính là "Uncle Bob". Ông là một trong những chuyên gia phần mềm, kiến trúc sư hệ thống và nhà tư vấn công nghệ có tầm ảnh hưởng lớn nhất thế giới trong suốt nửa thế kỷ qua. Ông chính là một trong số 17 tác giả đầu tiên đã cùng nhau ký tên và ban hành Tuyên ngôn Phát triển Phần mềm Linh hoạt (Agile Manifesto) vào năm 2001, đặt nền móng cho phương pháp quản trị dự án phần mềm hiện đại. Bên cạnh việc điều hành công ty tư vấn quốc tế Uncle Bob Consulting, ông còn dành cả cuộc đời để phổ biến văn hóa lập trình chuyên nghiệp, đạo đức nghề nghiệp của kỹ sư và tư duy tối ưu hóa hệ thống. Ông là cha đẻ của các cuốn giáo trình kinh điển, bắt buộc phải đọc đối với mọi lập trình viên từ cấp độ junior đến kiến trúc sư trưởng bao gồm: Clean Code (Mã sạch), Clean Architecture (Kiến trúc sạch), Clean Software Design và Clean Agile.', 'Mỹ', '2026-05-19 17:17:02', '', '22bd6e97-fd1a-4278-8420-0ea30845fddd.jpg'),
	(3, 'Nguyễn Du', 'Đại thi hào dân tộc Việt Nam, bậc thầy của nền văn học trung đại và là Danh nhân văn hóa thế giới được tổ chức UNESCO vinh danh chính thức. Sinh ra trong một gia đình đại quý tộc có truyền thống khoa bảng và văn hóa lừng lẫy tại Thăng Long, ông có vốn sống vô cùng phong phú do từng trải qua nhiều biến động thăng trầm của lịch sử thời Tây Sơn và thời Nguyễn. Ông là một thiên tài kiệt xuất về nghệ thuật sử dụng ngôn ngữ chữ Nôm, đưa thể thơ lục bát dân tộc đạt đến đỉnh cao chói lọi của văn chương bác học. Tác phẩm tiêu biểu nhất của ông là Đoạn Trường Tân Thanh (thường gọi là Truyện Kiều) - một kiệt tác trường tồn phản ánh sâu sắc giá trị hiện thực xã hội, tiếng nói nhân đạo, sự đồng cảm sâu sắc với nỗi đau của con người, đặc biệt là người phụ nữ. Bên cạnh đó, ông còn để lại ba tập thơ chữ Hán có giá trị tư tưởng tư duy triết lý vô cùng sâu sắc.', 'Việt Nam', '2026-05-20 02:44:57', '', '36ecbee1-e2bf-4ed8-aaf2-a511d3e9403e.jpg'),
	(4, 'Paulo Coelho', 'Tiểu thuyết gia, nhạc sĩ, nhà biên kịch kiêm đại sứ hòa bình của Liên Hợp Quốc, một trong những tác giả có tầm ảnh hưởng lớn nhất thời đại hiện đại thuộc nền văn học Mỹ Latinh. Trước khi trở thành nhà văn nổi tiếng thế giới, ông từng trải qua một tuổi trẻ nổi loạn, từng bị gia đình đưa vào bệnh viện tâm thần và từng bị bắt giam do các hoạt động nghệ thuật chống đối chính quyền độc tài lúc bấy giờ. Các tác phẩm của ông mang đậm tính triết lý sâu xa, thế giới tâm linh huyền bí, đan xen với hành trình khám phá chiều sâu nội tâm và vận mệnh của con người. Cuốn tiểu thuyết huyền thoại "Nhà Gi Giả Kim" (The Alchemist) của ông đã trở thành một trong những cuốn sách bán chạy nhất mọi thời đại, lập kỷ lục Guinness thế giới về tác phẩm được dịch ra nhiều ngôn ngữ nhất bởi một tác giả còn sống (hơn 80 ngôn ngữ) và bán được hơn 150 triệu bản trên toàn cầu.', 'Brazil', '2026-05-20 02:45:32', '', '0ac4d3e5-5c79-4635-a465-e6c5389c9396.jpg'),
	(5, 'Yuval Noah Harari', 'Nhà nghiên cứu lịch sử sâu sắc, triết gia xuất sắc kiêm Giáo sư giảng dạy tại Khoa Lịch sử thuộc Đại học Hebrew Jerusalem. Ông nhận bằng Tiến sĩ tại Đại học Oxford (Anh) vào năm 2002 và nhanh chóng trở thành một nhà tư tưởng có tầm ảnh hưởng toàn cầu. Ông nổi tiếng rộng rãi thông qua các công trình biên khảo lịch sử mang tầm vóc vĩ mô, kết hợp đột phá giữa nhân chủng học, triết học, sinh học và khoa học công nghệ để giải thích sự phát triển của loài người. Ông thường xuyên tham luận tại các diễn đàn kinh tế thế giới về tương lai của nhân loại trước làn sóng trí tuệ nhân tạo và kỹ nghệ sinh học. Bộ ba tác phẩm làm nên tên tuổi của ông bao gồm Sapiens: Lược sử loài người, Homo Deus: Lược sử tương lai và 21 Bài học cho thế kỷ 21 đã định hình lại hoàn toàn tư duy của hàng triệu độc giả, chính trị gia và giới tinh hoa quốc tế.', 'Israel', '2026-05-20 02:46:01', '', '16f74993-48d0-4b59-9de6-f59940644a4c.jpg'),
	(6, 'Dale Carnegie', 'Nhà văn, nhà thuyết trình vĩ đại người Mỹ đi tiên phong trong lĩnh vực phát triển bản thân, nghệ thuật bán hàng, tâm lý học hành vi giao tiếp ứng xử và huấn luyện kỹ năng lãnh đạo doanh nghiệp. Ông là người sáng lập ra Viện Phát triển Bản thân Dale Carnegie, một tổ chức hiện nay đã mở rộng chi nhánh đào tạo ra toàn cầu. Hệ thống bài giảng, phương pháp thực hành tư duy và các đầu sách của ông tập trung vào việc thấu hiểu tâm lý con người để xây dựng mối quan hệ bền vững. Cuốn sách Đắc Nhân Tâm (How to Win Friends and Influence People) xuất bản năm 1936 của ông đã trở thành một hiện tượng xuất bản của nhân loại, liên tục dẫn đầu danh sách bán chạy và là cuốn sách gối đầu giường của nhiều thế hệ doanh nhân, chính trị gia. Tác phẩm lớn khác của ông là Quăng Gánh Lo Đi Và Vui Sống cũng cứu rỗi hàng triệu người khỏi trầm cảm và bế tắc.', 'Mỹ', '2026-05-20 02:46:29', '', '2515ef40-87b4-4d1b-a71f-49e3a3513308.jpg'),
	(7, 'J.K. Rowling', 'Tên đầy đủ là Joanne Rowling, nữ biên kịch, nhà sản xuất điện ảnh, nhà từ thiện vĩ đại và nhà văn tiểu thuyết giả tưởng xuất sắc người Anh. Từ một người phụ nữ ly hôn, sống bằng tiền trợ cấp xã hội của chính phủ, bà đã vươn lên trở thành một trong những nhà văn giàu có và quyền lực nhất thế giới nhờ tài năng văn học thiên bẩm. Bà là cha đẻ của loạt truyện phù thủy huyền thoại Harry Potter gồm 7 tập - tác phẩm mang tính hiện tượng văn hóa toàn cầu, đạt kỷ lục bán chạy nhất trong lịch sử xuất bản với hơn 500 triệu bản được tiêu thụ. Tác phẩm này đã được chuyển thể thành loạt phim điện ảnh bom tấn ăn khách, mở ra một kỷ nguyên mới cho dòng văn học kỳ ảo dành cho mọi lứa tuổi. Bà cũng tích cực điều hành tổ chức từ thiện Lumos nhằm giúp đỡ trẻ em mồ côi và có hoàn cảnh khó khăn trên toàn thế giới.', 'Anh', '2026-05-20 02:46:56', '', '3e5d8ef6-b5fa-4c8f-9594-b73870829701.webp'),
	(8, 'Cal Newport', 'Giáo sư chuyên ngành Khoa học Máy tính tại Đại học Georgetown (Mỹ). Ông nhận bằng Tiến sĩ tại Viện Công nghệ Massachusetts (MIT) danh giá vào năm 2009. Bên cạnh các nghiên cứu học thuật chuyên sâu về các thuật toán phân tán trong mạng máy tính, ông còn là một nhà tiểu luận, diễn giả nổi tiếng chuyên nghiên cứu về mối giao thoa giữa công nghệ kỹ thuật số, năng suất làm việc và văn hóa đời sống hiện đại. Ông là người tiên phong chỉ ra tác hại xao nhãng của mạng xã hội đối với não bộ và đề xuất các phương pháp rèn luyện tư duy tập trung cao độ. Ông là tác giả của các cuốn sách tư duy sâu sắc, định hình lại phong cách làm việc của tri thức thời đại mới như: Deep Work (Làm việc sâu), Digital Minimalism (Chủ nghĩa tối giản số), So Good They Can\'t Ignore You và A World Without Email.', 'Mỹ', '2026-05-20 02:47:19', '', 'e6523080-507f-41bc-80d1-cc328699e77f.jpg'),
	(9, 'Angela Duckworth', 'Nhà tâm lý học hành vi, diễn giả kiêm Giáo sư nghiên cứu tại Đại học Pennsylvania (Mỹ). Bà từng tốt nghiệp chuyên ngành Thần kinh sinh học tại Đại học Harvard, nhận bằng Thạc sĩ tại Đại học Oxford và bằng Tiến sĩ Tâm lý học tại Đại học Pennsylvania. Bà là người sáng lập và điều hành tổ chức phi lợi nhuận Character Lab với sứ mệnh thúc đẩy khoa học phát triển tính cách cho trẻ em. Bà đã vinh dự được trao tặng giải thưởng thiên tài MacArthur Fellowship nhờ các công trình nghiên cứu khoa học chuyên sâu chứng minh rằng sự kết hợp giữa niềm đam mê dài hạn và tính kiên trì bền bỉ vượt nghịch cảnh (Grit) mới là yếu tố cốt lõi quyết định thành công lâu dài của một cá nhân, chứ không đơn thuần chỉ là chỉ số thông minh IQ bẩm sinh.', 'Mỹ', '2026-05-20 02:47:48', '', '58aeae00-aeb0-4a6e-ba2d-d812965673ef.jpg'),
	(10, 'John Zelle', 'Giáo sư, Tiến sĩ Khoa học máy tính lỗi lạc chuyên ngành giáo dục công nghệ thông tin tại Trường Đại học Wartburg (Mỹ). Ông nhận bằng Tiến sĩ tại Đại học Indiana và dành phần lớn sự nghiệp của mình để nghiên cứu các phương pháp sư phạm tối ưu nhằm truyền tải kiến thức lập trình phức tạp cho học sinh, sinh viên đại học. Ông được biết đến rộng rãi và kính trọng trong giới sư phạm công nghệ quốc tế nhờ biên soạn cuốn giáo trình "Python Programming: An Introduction to Computer Science". Đây là cuốn sách gối đầu giường chuẩn mực, có tư duy logic chặt chẽ, cấu trúc mạch lạc, được hàng trăm trường đại học hàng đầu trên toàn thế giới áp dụng làm tài liệu giảng dạy chính thức cho môn nhập môn lập trình máy tính.', 'Mỹ', '2026-05-20 03:09:36', '', 'd5667de1-1594-48a7-9311-c3189a3b8fb3.jpg'),
	(11, 'Richard H. Thaler', 'Giáo sư danh tiếng chuyên ngành Kinh tế học và Khoa học Hành vi tại Trường Kinh doanh Booth thuộc Đại học Chicago (Mỹ). Ông tốt nghiệp Tiến sĩ tại Đại học Rochester và đã vinh dự được trao tặng Giải thưởng Nobel Kinh tế danh giá vào năm 2017 nhờ những đóng góp mang tính cách mạng, tiên phong trong việc xây dựng cầu nối giữa phân tích kinh tế và tâm lý học hành vi con người. Các nghiên cứu của ông chứng minh rằng con người không luôn luôn hành động lý trí như các lý thuyết kinh tế cổ điển giả định. Công trình nghiên cứu nổi tiếng toàn cầu của ông về lý thuyết "Hích" (Nudge) đã được nhiều chính phủ trên thế giới (như Mỹ, Anh) áp dụng để thiết kế các chính sách công hiệu quả nhằm thay đổi hành vi xã hội.', 'Mỹ', '2026-05-20 02:57:04', '', '0d556af9-cbd7-4a72-a51c-f2064011a18d.jpg'),
	(12, 'George Orwell', 'Bút danh của Eric Arthur Blair, một trong những nhà văn, nhà báo, nhà tiểu luận và nhà phê bình văn học - chính trị có tầm ảnh hưởng sâu rộng nhất thế kỷ 20 người Anh. Ông từng có thời gian làm cảnh sát hoàng gia tại Miến Điện, trải nghiệm cuộc sống nghèo khổ tại Paris và Luân Đôn, những vốn sống thực tế đó đã nhào nặn nên một ngòi bút vô cùng sắc bén và chính trực. Các tác phẩm tiểu thuyết hư cấu viễn tưởng của ông, tiêu biểu là "1984" và "Chuyện ở nông trại" (Animal Farm), là những lời cảnh báo mang tính tiên tri sâu sắc về các mô hình độc tài toàn trị, sự kiểm soát tư tưởng, sự bóp méo ngôn ngữ và sự xói mòn nghiêm trọng quyền tự do cá nhân của con người, để lại những thuật ngữ chính trị kinh điển cho nhân loại.', 'Anh', '2026-05-20 02:57:30', '', 'bd2187f2-9fdb-42b1-b7ae-ddcc8436d1da.jpg'),
	(13, 'James Clear', 'Nhà văn, nhà nghiên cứu kiêm diễn giả chuyên sâu về chủ đề xây dựng thói quen tích cực, tối ưu hóa hiệu suất làm việc cá nhân và chiến lược phát triển tư duy dài hạn. Ông sở hữu bản tin điện tử "3-2-1" thu hút hàng triệu người đăng ký đọc mỗi tuần trên toàn thế giới. Cuốn sách đầu tay "Atomic Habits" (Thay đổi tí hon, hiệu quả bất ngờ) của ông đã trở thành một hiện tượng xuất bản toàn cầu, liên tục đứng đầu các bảng xếp hạng sách bán chạy nhất của New York Times suốt nhiều năm liền. Tác phẩm được đánh giá rất cao nhờ tính ứng dụng thực tế cực kỳ cao, đưa ra các chiến lược từng bước rõ ràng dựa trên nền tảng khoa học thần kinh, sinh học và tâm lý học hành vi để giúp con người loại bỏ thói quen xấu, thiết lập lối sống lành mạnh.', 'Mỹ', '2026-05-20 02:57:58', '', 'f5de784d-3172-4a65-b36e-f55312dc0cda.jpg'),
	(14, 'Trần Trọng Kim', 'Nhà giáo dục lớn, nhà nghiên cứu sử học, văn hóa học danh tiếng vang bóng một thời của Việt Nam nửa đầu thế giới 20. Ông tốt nghiệp trường Thuộc địa tại Paris, sau đó về nước cống hiến trọn đời cho ngành giáo dục, từng giữ chức Thanh tra Học chính và là học giả có đóng góp to lớn trong việc hệ thống hóa lịch sử, học thuật dân tộc bằng chữ Quốc ngữ. Ông có tư duy khách quan, phương pháp khoa học phương Tây kết hợp am hiểu sâu sắc Nho học phương Đông. Tác phẩm "Việt Nam sử lược" và "Nho giáo" của ông là những công trình học thuật nền tảng, có giá trị lưu trữ, nghiên cứu và tham khảo vô cùng sâu sắc cho nhiều thế hệ học giả. Ông cũng từng có thời gian ngắn giữ chức vụ Thủ tướng của chính phủ Đế quốc Việt Nam năm 1945.', 'Việt Nam', '2026-05-20 02:58:33', '', '817ecaab-e864-42cc-908e-55ff43d0d020.jpg'),
	(15, 'Andrew Ng', 'Tên tiếng Hoa là Ngô Ân Đạt, nhà khoa học máy tính, chuyên gia hàng đầu thế giới trong lĩnh vực Trí tuệ nhân tạo (AI), Học máy (Machine Learning) và Học sâu (Deep Learning). Ông nhận bằng Tiến sĩ tại Đại học California, Berkeley, hiện là Phó giáo sư tại Đại học Stanford. Ông là người đồng sáng lập dự án Google Brain, cựu nhà khoa học trưởng của Baidu. Đặc biệt, ông là người đồng sáng lập Coursera - nền tảng giáo dục trực tuyến lớn nhất thế giới và DeepLearning.AI, đóng góp vĩ đại vào việc phổ cập tri thức công nghệ cao cho hàng triệu người học trên toàn cầu. Các khóa học Học máy của ông trên mạng được coi là tài liệu chuẩn mực gối đầu giường của hầu hết các kỹ sư AI hiện đại.', 'Mỹ', '2026-05-20 02:58:59', '', 'f75cd03a-c5b3-451e-8d30-618b997cc76a.jpg'),
	(16, 'Nguyễn Hải Thanh', 'Giáo sư, Tiến sĩ khoa học, Nhà giáo Nhân dân lỗi lạc chuyên ngành Toán ứng dụng và Toán tin học tại Việt Nam. Ông có nhiều năm cống hiến xuất sắc trong công tác giảng dạy đại học, nghiên cứu chuyên sâu và biên soạn hệ thống giáo trình toán cao cấp, giải tích hệ thống, phương pháp tính, lý thuyết xác suất thống kê. Các công trình khoa học của ông tập trung vào việc ứng dụng toán học toán tin để giải quyết các bài toán tối ưu hóa trong kỹ thuật, kinh tế và quản lý. Hệ thống sách giáo khoa, giáo trình đại học do ông chủ biên đã đóng góp nền móng vững chắc cho việc đào tạo hàng vạn kỹ sư, cử nhân thuộc khối ngành khoa học tự nhiên và công nghệ kỹ thuật trên cả nước.', 'Việt Nam', '2026-05-20 02:59:37', '', 'c7c97ba4-50e2-4998-99a1-18fb1a654ae7.jpg'),
	(17, 'Lê Đình Thúy', 'Giáo sư, Nhà giáo Ưu tú, nhà sư phạm toán học chuyên nghiệp ngành Toán kinh tế. Ông là tác giả, dịch giả và chủ biên của loạt giáo trình "Đại số tuyến tính", "Toán cao cấp cho các nhà kinh tế", "Toán tối ưu" chuẩn mực của Bộ Giáo dục và Đào tạo Việt Nam. Các tác phẩm của ông nổi tiếng với phương pháp tiếp cận sư phạm mẫu mực, giải thích các khái niệm trừu tượng của toán học bằng ngôn ngữ ứng dụng thực tế trong phân tích mô hình kinh tế vi mô và vĩ mô. Các bộ giáo trình này đã được áp dụng làm tài liệu giảng dạy chính thức, bắt buộc suốt nhiều thập kỷ tại các trường đại học khối ngành kinh tế, tài chính và quản trị kinh doanh trên toàn quốc.', 'Việt Nam', '2026-05-20 03:00:04', '', '10e144f4-f9a4-4587-8e87-21d0c764eb0c.jpg'),
	(18, 'Stephen Hawking', 'Nhà vật lý lý thuyết, nhà vũ trụ học thiên tài, nhà văn phổ biến khoa học vĩ đại người Anh và là nguyên Giáo sư Toán học Lucas tại Đại học Cambridge (vị trí danh giá từng thuộc về Isaac Newton). Vượt qua nghịch cảnh nghiệt ngã của căn bệnh xơ cứng teo cơ bên (ALS) khiến ông bị liệt toàn thân và mất khả năng nói từ khi còn trẻ, ông đã để lại cho nhân loại những công trình khoa học vĩ đại về hố đen vũ trụ, thuyết tương đối tổng quát và cơ học lượng tử. Cuốn sách phổ biến khoa học vũ trụ kinh điển "Lược sử thời gian" (A Brief History of Time) của ông xuất bản năm 1988 đã trở thành tác phẩm bán chạy kỷ lục thế giới, giúp đại chúng hiểu được những bí ẩn sâu thẳm của vũ trụ bao la.', 'Anh', '2026-05-20 03:00:37', '', '73bba69b-b79c-4301-9e3b-0e5ca491e78f.jpg'),
	(19, 'Ngô Thị Thuận', 'Giáo sư, Tiến sĩ khoa học đầu ngành, người đặt nền móng vững chắc cho sự phát triển của chuyên ngành Hóa học hữu cơ tại Việt Nam. Bà tốt nghiệp xuất sắc tại các học viện khoa học danh tiếng nước ngoài, sau đó về nước dành trọn cả cuộc đời cho công tác giảng dạy, nghiên cứu khoa học tại Trường Đại học Khoa học Tự nhiên thuộc Đại học Quốc gia Hà Nội. Bà là tác giả của hàng loạt bộ giáo trình chuyên sâu cấp quốc gia về tổng hợp hóa học hữu cơ, cơ chế phản ứng hóa học, hóa học hợp chất thiên nhiên phục vụ công tác đào tạo thạc sĩ, tiến sĩ, kỹ sư công nghệ hóa chất, dược phẩm và công nghệ sinh học trên toàn quốc.', 'Việt Nam', '2026-05-20 03:01:07', '', '2053f402-aa7a-411b-a5e2-e67fef9a21c5.jpg'),
	(20, 'Đại học Luật Hà Nội', 'Tập thể các giáo sư, phó giáo sư, tiến sĩ khoa học, hội đồng thẩm phán, luật sư và chuyên gia pháp lý đầu ngành trực thuộc Trường Đại học Luật Hà Nội - cơ sở đào tạo luật pháp lớn và uy tín nhất tại Việt Nam. Tập thể tác giả chịu trách nhiệm nghiên cứu chuyên sâu, cập nhật các văn bản quy phạm pháp luật của nhà nước để biên soạn hệ thống giáo trình luật chuẩn quốc gia. Các bộ giáo trình tiêu biểu bao gồm Giáo trình Luật Dân sự, Giáo trình Luật Hình sự, Giáo trình Luật Thương mại, Giáo trình Luật Tố tụng và Luật Quốc tế, phục vụ công tác giảng dạy, nghiên cứu pháp lý chuẩn mực cho toàn bộ hệ thống đại học khối ngành luật trên cả nước.', 'Việt Nam', '2026-05-20 03:01:44', '', '41718aa8-0e14-4d22-9432-a782a8646153.jpg'),
	(21, 'Paul Kalanithi', 'Bác sĩ phẫu thuật thần kinh tài hoa, nhà nghiên cứu khoa học kiêm nhà văn đầy nghị lực người Mỹ gốc Ấn. Ông tốt nghiệp cử nhân và thạc sĩ văn học Anh tại Đại học Stanford, trước khi tốt nghiệp xuất sắc ngành Y khoa tại Đại học Yale danh tiếng. Khi đang ở đỉnh cao sự nghiệp phẫu thuật não và chuẩn bị nhận các giải thưởng danh giá, ông bị chẩn đoán mắc bệnh ung thư phổi giai đoạn cuối. Cuốn tự truyện tinh tế và triết lý "Khi hơi thở hóa thinh không" (When Breath Becomes Air) được ông hoàn thiện trong những ngày tháng cuối đời chiến đấu với bạo bệnh đã lay động sâu sắc trái tim hàng triệu độc giả toàn cầu về ranh giới giữa sự sống và cái chết, ý nghĩa đích thực của cuộc sống.', 'Mỹ', '2026-05-20 03:02:20', '', 'bfb2c3d4-40ab-4c85-b1d6-1809b97c0dfb.jpg'),
	(22, 'Jostein Gaarder', 'Nhà văn, nhà tư tưởng kiêm nhà hoạt động xã hội lỗi lạc người Na Uy. Ông từng học chuyên ngành thần học, ngôn ngữ và văn học tại Đại học Oslo, sau đó giảng dạy triết học trong nhiều năm trước khi dành trọn thời gian cho sự nghiệp sáng tác. Ông chuyên sáng tác các tác phẩm truyện dài, tiểu thuyết mang đậm màu sắc tư duy triết học, lịch sử văn minh nhân loại dưới góc nhìn lăng kính huyền ảo, giàu chất thơ nhưng vô cùng dễ hiểu, đặc biệt thích hợp cho giới trẻ. Tác phẩm văn học bất hủ "Thế giới của Sophie" (Sophie\'s World) của ông xuất bản năm 1991 đã trở thành hiện tượng văn học quốc tế, được dịch ra hơn 60 ngôn ngữ và bán được hàng chục triệu bản.', 'Na Uy', '2026-05-20 03:02:48', '', 'aabb6911-5bd8-4e8a-9846-e9b2260026b5.jpg'),
	(23, 'Gustave Le Bon', 'Nhà tâm lý học xã hội, nhà nhân chủng học, nhà vật lý học người Pháp tiên phong trong việc nghiên cứu lý thuyết về tâm lý học đám đông và hành vi tập thể. Sống trong thời kỳ nước Pháp có nhiều biến động chính trị và cách mạng xã hội, ông đã dành nhiều năm quan sát và đưa ra các luận điểm khoa học sắc bén về cách mà các cá nhân đánh mất lý trí cá nhân khi hòa mình vào một tập thể lớn. Công trình bất hủ "Tâm lý học đám đông" (La Psychologie des Foules) xuất bản năm 1895 của ông đến nay vẫn giữ nguyên giá trị cốt lõi, được áp dụng, trích dẫn rộng rãi trong các ngành khoa học chính trị, xã hội học, truyền thông đại chúng, nghệ thuật lãnh đạo và marketing hiện đại.', 'Pháp', '2026-05-20 03:03:12', '', '836e396d-ac86-4508-a129-d8235821ecaf.jpg'),
	(24, 'Cambridge University Press', 'Hội đồng khảo thí, nghiên cứu học thuật học đường và nhà xuất bản trực thuộc Đại học Cambridge danh tiếng tại Vương quốc Anh. Được thành lập bởi Hiến chương Vua Henry VIII vào năm 1534, đây là tổ chức xuất bản, in ấn lâu đời nhất thế giới và là nhà xuất bản đại học lớn thứ hai toàn cầu. Tổ chức chuyên nghiên cứu và cung cấp các bộ giáo trình giảng dạy tiếng Anh chuẩn quốc tế (như tài liệu luyện thi IELTS, Cambridge English) cùng hàng vạn công trình nghiên cứu khoa học chuyên sâu, tạp chí học thuật ở tất cả các lĩnh vực, bảo đảm tính chính xác, cập nhật và hàn lâm cao nhất phục vụ giáo dục toàn cầu.', 'Anh', '2026-05-20 03:03:35', '', '05dd490b-7551-483f-bafa-e3b8dd5ef622.jpg'),
	(25, 'E.H. Gombrich', 'Tên đầy đủ là Ngài Ernst Hans Josef Gombrich, nhà sử học nghệ thuật, giáo sư phê bình mỹ thuật lỗi lạc nhất thế kỷ 20 người Áo gốc Anh. Ông từng giữ chức vụ Giám đốc Viện Warburg và Giáo sư Lịch sử Triết học Nghệ thuật tại Đại học London. Ông đã được phong tước Hiệp sĩ vì những cống hiến vĩ đại cho nền văn hóa nhân loại. Cuốn sách "Câu chuyện nghệ thuật" (The Story of Art) xuất bản lần đầu năm 1950 của ông là một công trình học thuật vĩ đại nhưng vô cùng sống động, chân thực, được công nhận rộng rãi là cuốn giáo trình giới thiệu về lịch sử mỹ thuật, kiến trúc, điêu khắc và hội họa xuất sắc nhất mọi thời đại, dịch ra hơn 30 ngôn ngữ.', 'Áo', '2026-05-20 03:03:59', '', '9f1fcde5-3b6d-4af7-b40d-4da555698fc5.jpg'),
	(26, 'Ray D. Strand', 'Bác sĩ chuyên khoa y học gia đình tốt nghiệp Đại học Y khoa Colorado (Mỹ) với hơn 30 năm kinh nghiệm điều trị lâm sàng chuyên sâu. Bên cạnh công tác khám chữa bệnh, ông đã dành phần lớn sự nghiệp để nghiên cứu về hiện tượng căng thẳng oxy hóa tế bào và vai trò của y học dinh dưỡng trong việc phòng bệnh chủ động, hỗ trợ điều trị các bệnh mãn tính nguy hiểm như ung thư, tiểu đường, tim mạch. Cuốn sách nổi tiếng "Dinh dưỡng học bị thất truyền" (What Your Doctor Doesn\'t Know About Nutritional Medicine May Be Killing You) của ông đã mở ra một tư duy mang tính đột phá cho cả giới y khoa và người dân về việc bảo vệ sức khỏe bằng lối sống và vi chất dinh dưỡng.', 'Mỹ', '2026-05-20 03:04:22', '', 'da742af1-782c-4452-84a1-27e149801457.jpg'),
	(27, 'Masanobu Fukuoka', 'Triết gia, nhà nông học, nhà văn kiêm nhà hoạt động môi trường tiên phong khai sinh ra triết lý "Nông nghiệp tự nhiên" (Nông nghiệp thuận tự nhiên) người Nhật Bản. Từng là một nhà khoa học nghiên cứu bệnh học thực vật, ông đã từ bỏ phòng thí nghiệm hiện đại để trở về nông trại thực hành phương pháp canh tác "bốn không": Không cày lật đất, không dùng phân bón hóa học, không dùng thuốc trừ sâu, không làm cỏ. Cuốn sách bất hủ "Cuộc cách mạng một cọng rơm" của ông không chỉ dừng lại ở kỹ thuật nông nghiệp độc đáo mà còn truyền tải tư tưởng triết lý sâu sắc về lối sống tối giản, sự thức tỉnh tâm linh và sự hòa hợp tuyệt đối giữa con người với hệ sinh thái.', 'Nhật Bản', '2026-05-20 03:05:01', '', '24b7b12f-e5e9-425a-bdff-e48ac6ace173.jpg'),
	(28, 'Rachel Carson', 'Nhà sinh vật học biển, tác giả, nhà khoa học kiêm nhà hoạt động môi trường tiên phong người Mỹ. Bà từng làm việc cho Cục Cá và Động vật hoang dã Mỹ, sở hữu ngòi bút văn học tinh tế kết hợp tư duy khoa học nghiêm túc. Cuốn sách chấn động dư luận "Mùa xuân vắng lặng" (Silent Spring) xuất bản năm 1962 của bà đã dũng cảm vạch trần tác hại tàn phá môi trường kinh hoàng của thuốc trừ sâu hóa học (đặc biệt là chất DDT), bất chấp sự công kích của các tập đoàn hóa chất lớn. Tác phẩm đã tạo tiền đề bùng nổ cho phong trào bảo vệ môi trường hiện đại trên toàn cầu, dẫn đến việc cấm sử dụng DDT và thành lập Cơ quan Bảo vệ Môi trường Mỹ (EPA).', 'Mỹ', '2026-05-20 03:08:54', '', 'ff952dfe-a5e7-4ecd-a2b9-bb6c14ff585e.jpg'),
	(29, 'Niccolò Machiavelli', 'Nhà triết học chính trị, nhà ngoại giao, nhà viết kịch kiêm nhà thơ kinh điển thời kỳ Phục Hưng nước Ý. Ông từng giữ chức vụ Chánh văn phòng Đệ nhị Cộng hòa Florence, trực tiếp tham gia vào các hoạt động ngoại giao phức tạp giữa các quốc gia châu Âu. Ông được mệnh danh là một trong những cha đẻ của lý thuyết khoa học chính trị hiện đại với luận thuyết bất hủ "Quân Vương" (The Prince) - tác phẩm phân tích vô cùng thực dụng, sắc bén về bản chất của quyền lực, nghệ thuật lãnh đạo, thủ đoạn chính trị và quản trị quốc gia. Các tư tưởng của ông tạo ra những cuộc tranh luận học thuật kéo dài suốt nhiều thế kỷ trong lịch sử triết học thế giới.', 'Ý', '2026-05-20 03:08:24', '', 'fdfd7ce0-e772-4ee1-b559-25a5969c3c7c.jpg'),
	(30, 'Malcolm Gladwell', 'Nhà báo, tác giả và diễn giả nổi tiếng gốc Canada, cây viết kỳ cựu của tạp chí danh tiếng The New Yorker và từng là phóng viên kinh tế của tờ Washington Post. Ông có biệt tài thiên bẩm trong việc kết nối các nghiên cứu khoa học xã hội, tâm lý học hành vi, nhân chủng học phức tạp thành những câu chuyện vô cùng lôi cuốn, dễ hiểu và đầy tính phát hiện đối với đại chúng. Ông liên tục nằm trong danh sách 100 người có tầm ảnh hưởng lớn nhất thế giới của tạp chí Time. Toàn bộ các đầu sách phân tích xã hội học của ông bao gồm Điểm Bùng Phát (The Tipping Point), Trong Chớp Mắt (Blink), Những Kẻ Xuất Chúng (Outliers) và David & Goliath đều là sách bán chạy toàn cầu.', 'Mỹ', '2026-05-20 03:07:58', '', 'a2b02e28-b28f-4334-98e6-86490ad45ff6.jpg'),
	(31, 'Huyền Chip', 'Bút danh của Nguyễn Thị Khánh Huyền, nhà văn, nhà nghiên cứu công nghệ kiêm Thạc sĩ chuyên ngành Khoa học Máy tính tốt nghiệp tại Đại học Stanford (Mỹ) danh giá. Cô từng gây tiếng vang lớn trong giới trẻ Việt Nam với tư cách là người tiên phong cổ vũ tinh thần tự lập thông qua bộ sách ký sự hành trình "Xách ba lô lên và đi" kể về chuyến du hành qua 25 quốc gia. Sau khi hoàn thành việc học tại Thung lũng Silicon, cô trở thành một chuyên gia nghiên cứu sâu trong lĩnh vực Trí tuệ nhân tạo (AI), kỹ nghệ dữ liệu lớn (Big Data) và là tác giả của các đầu sách công nghệ chuyên sâu được đón nhận tại thị trường quốc tế.', 'Việt Nam', '2026-05-20 03:07:33', '', '4b079229-c490-451b-8e23-e83a33557214.jpg'),
	(32, 'Hữu Thọ', 'Nhà báo lão thành cách mạng vĩ đại, nguyên Ủy viên Ban Chấp hành Trung ương Đảng, nguyên Tổng biên tập Báo Nhân Dân, Trưởng ban Tư tưởng - Văn hóa Trung ương kiêm Trợ lý Tổng Bí thư. Ông là cây đại thụ, biểu tượng lớn của nền báo chí chính luận cách mạng Việt Nam thế kỷ 20 và đầu thế kỷ 21. Với ngòi bút chính trực, sắc sảo, luôn bám sát hơi thở của thực tiễn đời sống nông nghiệp, nông dân và công cuộc đổi mới đất nước, ông đã để lại hàng nghìn bài báo, tiểu luận, bút ký chính trị có giá trị lý luận và thực tiễn sâu sắc. Ông được nhà nước trao tặng Huân chương Độc lập hạng Nhất và nhiều phần thưởng cao quý khác.', 'Việt Nam', '2026-05-20 03:07:09', '', 'a19fff81-92ef-43d4-a1e9-609cfa557a70.jpg'),
	(33, 'Đỗ Thanh Việt', 'Giáo sư, Tiến sĩ khoa học kỹ thuật điện tử, viễn thông và tự động hóa. Ông có hơn 40 năm kinh nghiệm giảng dạy chuyên môn sâu, nghiên cứu khoa học tại các học viện công nghệ lớn, trường đại học kỹ thuật hàng đầu Việt Nam và từng tham gia hợp tác nghiên cứu tại các viện kỹ thuật quốc tế. Ông là tác giả kiêm chủ biên của nhiều bộ giáo trình nền tảng và chuyên sâu về Lý thuyết mạch điện tử, Xử lý tín hiệu số, Kiến trúc máy tính vi xử lý, Hệ thống nhúng tự động. Hệ thống sách giáo khoa học thuật của ông là cẩm nang bắt buộc, đồng hành cùng nhiều thế hệ sinh viên hệ kỹ sư công nghệ điện tử toàn quốc.', 'Việt Nam', '2026-05-20 03:06:42', '', '671bf7d7-6840-44fe-af29-e043e03842b3.jpg'),
	(34, 'Nguyễn Đức Thiềm', 'Kiến trúc sư, Nhà giáo Nhân dân, Phó giáo sư đầu ngành nghiên cứu về cơ sở lý luận kiến trúc, lịch sử kiến trúc và bảo tồn di sản văn hóa đô thị tại Việt Nam. Ông dành cả cuộc đời để giảng dạy tại Đại học Xây dựng Hà Nội và đóng góp to lớn vào việc định hình tư duy thẩm mỹ kiến trúc hiện đại nhưng giàu bản sắc dân tộc cho sinh viên Việt Nam. Cuốn sách kinh điển "Cơ sở văn hóa và kiến trúc Việt Nam" cùng hệ thống giáo trình chuyên ngành về Cấu tạo kiến trúc, Nguyên lý thiết kế kiến trúc dân dụng của ông là tài liệu học tập, tra cứu bắt buộc cho nhiều thế hệ kiến trúc sư và kỹ sư xây dựng.', 'Việt Nam', '2026-05-20 03:06:19', '', '722336fa-14c5-4c59-8bc8-fc3e848b3260.jpg'),
	(35, 'Nguyễn Ngọc Tuấn', 'Nghệ nhân trà quốc gia, chuyên gia khảo cứu và nhà nghiên cứu văn hóa dân gian chuyên sâu về lịch sử phát triển, trà cụ, phương pháp chế biến và nghệ thuật thưởng trà truyền thống của người Việt (Trà đạo Việt Nam). Ông đã dành nhiều thập kỷ đi thực địa khắp các bản làng vùng cao Tây Bắc để tìm kiếm, bảo tồn các dòng trà cổ thụ san tuyết quý hiếm. Các công trình biên khảo, sách nghiên cứu văn hóa và các bài viết chuyên luận của ông truyền tải triết lý sống an nhiên, lòng biết ơn thiên nhiên và gìn giữ nét đẹp văn hóa ứng xử thuần khiết của dân tộc qua từng chén trà Việt.', 'Việt Nam', '2026-05-20 03:05:55', '', '86f768fe-b4a1-4aaf-a5b9-4a37716e2093.jpg'),
	(36, 'Đặng Thúy Hương', 'Nhà thiết kế thời trang, chuyên gia mỹ thuật công nghiệp kiêm giảng viên cao cấp lâu năm tại các trường đại học nghệ thuật, thiết kế uy tín tại Việt Nam. Bà có nhiều công trình nghiên cứu sâu sắc về lịch sử phục trang cung đình, trang phục truyền thống của các dân tộc Việt Nam và xu hướng thời trang bền vững trên thế giới. Bà là tác giả của các bộ giáo trình chuẩn mực về Kỹ thuật thiết kế rập phẳng, Hình họa ứng dụng, Nguyên lý tạo dáng thời trang và Thiết kế chất liệu vải, đóng góp to lớn vào việc đào tạo ra hàng nghìn nhà thiết kế thời trang trẻ cho thị trường may mặc nước nhà.', 'Việt Nam', '2026-05-20 03:05:31', '', '54812b31-a59f-4e32-a1df-2e15d8df8ea3.jpg');

-- Dumping structure for table library_db.book_authors
CREATE TABLE IF NOT EXISTS `book_authors` (
  `book_id` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.book_authors: ~40 rows (approximately)
INSERT INTO `book_authors` (`book_id`, `author_id`) VALUES
	(1, 1),
	(2, 1),
	(3, 4),
	(4, 7),
	(5, 3),
	(6, 12),
	(7, 1),
	(8, 2),
	(9, 10),
	(10, 2),
	(11, 15),
	(12, 5),
	(13, 6),
	(14, 8),
	(15, 9),
	(16, 11),
	(17, 13),
	(18, 16),
	(19, 17),
	(20, 18),
	(21, 19),
	(22, 20),
	(23, 21),
	(24, 14),
	(25, 14),
	(26, 22),
	(27, 23),
	(28, 24),
	(29, 25),
	(30, 26),
	(31, 27),
	(32, 28),
	(33, 29),
	(34, 30),
	(35, 31),
	(36, 32),
	(37, 33),
	(38, 34),
	(39, 35),
	(40, 36);

-- Dumping structure for table library_db.book_copies
CREATE TABLE IF NOT EXISTS `book_copies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_id` bigint(20) NOT NULL,
  `barcode` varchar(50) NOT NULL,
  `status` enum('AVAILABLE','BORROWED','LOST','DAMAGED','MAINTENANCE') DEFAULT 'AVAILABLE',
  `condition_note` text DEFAULT NULL,
  `acquired_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `barcode` (`barcode`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.book_copies: ~13 rows (approximately)
INSERT INTO `book_copies` (`id`, `book_id`, `barcode`, `status`, `condition_note`, `acquired_date`) VALUES
	(1, 5, 'BC-5-B6A095', 'AVAILABLE', NULL, '2026-05-23'),
	(2, 39, 'BC-39-D8623E', 'AVAILABLE', NULL, '2026-05-23'),
	(3, 1, 'BC-1-773A2E', 'AVAILABLE', NULL, '2026-05-23'),
	(4, 1, 'BC-1-60438D', 'AVAILABLE', NULL, '2026-05-24'),
	(5, 11, 'BC-11-6DB7DD', 'AVAILABLE', NULL, '2026-05-24'),
	(6, 9, 'BC-9-DBD705', 'AVAILABLE', NULL, '2026-05-24'),
	(7, 9, 'BC-9-5CC137', 'AVAILABLE', NULL, '2026-05-24'),
	(8, 1, 'BC-1-C28C33', 'AVAILABLE', NULL, '2026-05-24'),
	(9, 2, 'BC-2-4F4D31', 'BORROWED', NULL, '2026-05-24'),
	(10, 1, 'BC-1-E393BF', 'AVAILABLE', NULL, '2026-05-26'),
	(11, 4, 'BC-4-589CBE', 'AVAILABLE', NULL, '2026-05-28'),
	(12, 11, 'BC-11-4139F2', 'AVAILABLE', NULL, '2026-05-28'),
	(13, 3, 'BC-3-741851', 'AVAILABLE', NULL, '2026-05-28');

-- Dumping structure for table library_db.books
CREATE TABLE IF NOT EXISTS `books` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(20) NOT NULL,
  `title` varchar(500) NOT NULL,
  `publisher_id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `edition` varchar(50) DEFAULT NULL,
  `language` varchar(50) DEFAULT 'Tiếng Việt',
  `total_copies` int(11) DEFAULT 0,
  `available_copies` int(11) DEFAULT 0,
  `shelf_location` varchar(50) DEFAULT NULL,
  `cover_image` varchar(500) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(12,2) DEFAULT NULL,
  `deposit_fee` decimal(12,2) DEFAULT 0.00,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `summary` text DEFAULT NULL,
  `deposit_amount` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `isbn` (`isbn`),
  KEY `publisher_id` (`publisher_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `1` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `chk_book_copies_logic` CHECK (`available_copies` <= `total_copies` and `available_copies` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.books: ~40 rows (approximately)
INSERT INTO `books` (`id`, `isbn`, `title`, `publisher_id`, `category_id`, `publication_year`, `edition`, `language`, `total_copies`, `available_copies`, `shelf_location`, `cover_image`, `description`, `price`, `deposit_fee`, `created_at`, `updated_at`, `summary`, `deposit_amount`) VALUES
	(1, '978604118', 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 1, 3, NULL, NULL, 'Tiếng Việt', 11, 5, 'Kệ A1-01', '/uploads/books/1778857096378_cho-toi-mot-ve-di-tuoi-tho-1_600x943.jpg', NULL, 84000.00, 0.00, '2026-05-23 18:52:22', '2026-05-28 03:14:03', NULL, 58800.00),
	(2, '978604222', 'Dế Mèn Phiêu Lưu Ký', 3, 3, NULL, NULL, 'Tiếng Việt', 10, 0, 'Kệ A2-03', '/uploads/books/1778858260472_demenphieuluuky.jpg', NULL, 55000.00, 0.00, '2026-05-24 16:41:10', '2026-05-28 03:14:03', NULL, 38500.00),
	(3, '9786041234586', 'Nhà Giả Kim', 9, 3, NULL, NULL, 'Tiếng Việt', 12, 9, 'Kệ VH-04', '/uploads/books/1779192399331_nhagiakim.jpg', NULL, 65000.00, 0.00, '2026-05-19 12:06:39', '2026-05-28 04:09:59', 'Cuốn tiểu thuyết huyền thoại của văn hào Paulo Coelho là một trong những cuốn sách bán chạy nhất lịch sử nhân loại, mang tính chất của một câu chuyện ngụ ngôn đầy tính triết lý và tâm linh. Truyện theo chân Santiago, một chàng chăn cừu nghèo người Tây Ban Nha, người đã dũng cảm từ bỏ cuộc sống an phận để lên đường thực hiện giấc mơ lặp đi lặp lại của mình: đi tìm kho báu tại các kim tự tháp Ai Cập. Hành trình xuyên qua sa mạc mênh mông đầy rẫy hiểm nguy không chỉ là một cuộc tìm kiếm vật chất, mà dần trở thành một cuộc viễn chinh khám phá thế giới nội tâm của chính mình. Trên đường đi, chàng đã gặp gỡ nhiều nhân vật kỳ lạ như một vị vua già thông thái, một nhà giả kim bí ẩn và một tình yêu đích thực nơi ốc đảo. Họ đã dạy cho chàng cách lắng nghe tiếng gọi của con tim, cách đọc và thấu hiểu "Ngôn ngữ của Vũ trụ", nhận biết các dấu hiệu định mệnh mà Thượng đế sắp đặt. Tác phẩm truyền cảm hứng mãnh liệt về việc theo đuổi Vận mệnh cá nhân, nhắc nhở chúng ta rằng: Khi bạn khao khát một điều gì đó đủ lớn, toàn vũ trụ sẽ hợp lực giúp bạn đạt được điều đó, và đôi khi kho báu thực sự không nằm ở đích đến mà nằm ngay trong quá trình ta hoàn thiện bản thân.', 45500.00),
	(4, '9786041234589', 'Harry Potter và Phòng Chứa Bí Mật', 6, 3, 2023, NULL, 'Tiếng Việt', 10, 7, 'Kệ VH-05', '/uploads/books/1778858976638_HarryPotter.jpg', 'Phần 2 của series Harry Potter', 120000.00, 0.00, '2026-05-17 03:28:50', '2026-05-28 03:14:03', 'Phần thứ hai trong loạt truyện phù thủy toàn cầu của nữ văn sĩ J.K. Rowling tiếp tục mở ra cánh cửa dẫn vào thế giới phép thuật kỳ ảo nhưng cũng không kém phần đen tối tại trường Hogwarts. Năm học này của cậu bé Harry Potter bắt đầu bằng những lời cảnh báo đầy kinh hãi từ gia tinh Dobby về một mối nguy hiểm chết người đang chờ đợi cậu tại trường. Bất chấp tất cả, Harry cùng hai người bạn thân Ron và Hermione vẫn quay lại trường và đối mặt với hàng loạt hiện tượng rùng rợn: những thông điệp bằng máu xuất hiện trên tường, những con mèo và học sinh gốc Muggle liên tục bị hóa đá một cách bí ẩn. Phòng chứa bí mật – nơi ẩn náu của một quái vật cổ xưa do vị tổ sáng lập Salazar Slytherin để lại – được cho là đã bị mở ra bởi một "Kẻ kế vị" giấu mặt. Để cứu lấy ngôi trường và minh oan cho người bạn Hagrid, Harry phải vận dụng lòng dũng cảm, sự thông minh và khả năng nói tiếng Xà ngữ của mình để đi sâu vào lòng đất, đối đầu trực diện với ký ức đen tối của Tom Riddle và con mãng xà Tử xà Basilisk khổng lồ, khẳng định một chân lý sâu sắc rằng: Lựa chọn của chúng ta mới thể hiện bản chất thực sự, chứ không phải là năng lực bẩm sinh.', 84000.00),
	(5, '9786041234592', 'Truyện Kiều', 9, 3, NULL, NULL, 'Tiếng Việt', 6, 4, 'Kệ VH-06', '/uploads/books/1779192413873_truyenkieu.jpg', NULL, 45000.00, 0.00, '2026-05-19 12:06:53', '2026-05-28 03:14:03', 'Kiệt tác văn học trung đại bất hủ của Đại thi hào Nguyễn Du, tác phẩm đại diện cho đỉnh cao chói lọi của thể thơ lục bát và ngôn ngữ chữ Nôm dân tộc. Cuốn truyện thơ dài 3254 câu kể về cuộc đời đầy bão táp, truân chuyên suốt 15 năm lưu lạc của Vương Thúy Kiều – một người con gái tài sắc vẹn toàn, hiếu nghĩa đủ đường. Vì tai họa gia đình ập xuống, nàng buộc phải lỗi hẹn tình thâm với Kim Trọng để bán mình chuộc cha và em trai. Từ đó, Kiều bị đẩy vào vòng xoáy bi kịch vô tận của xã hội phong kiến thối nát: hai lần sa vào lầu xanh, bị đày đọa bởi những kẻ độc ác như Mã Giám Sinh, Tú Bà, Sở Khanh, Hoạn Thư, rồi tìm được hạnh phúc ngắn ngủi bên người anh hùng Từ Hải trước khi bị lừa gạt dẫn đến cái chết của chồng và phải trầm mình xuống sông Tiền Đường. Tác phẩm là một bức tranh hiện thực tàn khốc lên án xã hội coi trọng đồng tiền, chà đạp lên quyền sống của con người, đồng thời là một tiếng khóc nhân đạo bao la, ngợi ca vẻ đẹp tâm hồn, sự thủy chung, lòng hiếu thảo và khát vọng tự do, công lý của con người trước định mệnh bất công.', 31500.00),
	(6, '9786041234595', '1984', 9, 3, NULL, NULL, 'Tiếng Việt', 5, 4, 'Kệ VH-07', '/uploads/books/1779192429598_1984.jpg', NULL, 95000.00, 0.00, '2026-05-19 12:07:09', '2026-05-28 03:14:03', 'Tiểu thuyết viễn tưởng phản địa đàng (dystopia) gai góc và kiệt xuất của George Orwell, được xem là một trong những cuốn sách có tầm ảnh hưởng chính trị sâu rộng nhất thế kỷ 20. Câu chuyện diễn ra tại một thế giới giả tưởng đen tối, nơi xã hội bị chia cắt thành các siêu quốc gia và quyền lực tối cao nằm trong tay một bộ máy độc tài toàn trị do "Anh Cả" (Big Brother) đứng đầu. Nhân vật chính Winston Smith, một nhân viên tại Bộ Sự Thật, có nhiệm vụ hàng ngày là bóp méo, sửa đổi lịch sử theo ý muốn của Đảng. Sống dưới sự giám sát nghẹt thở của các màn hình kiểm soát (telescreen), nơi ngay cả một suy nghĩ lệch lạc cũng bị coi là "Tội phạm tư tưởng", Winston bắt đầu nhen nhóm ngọn lửa nổi loạn trong lòng. Cậu bí mật ghi nhật ký, tìm kiếm tình yêu đích thực với cô gái Julia và cố gắng liên lạc với tổ chức phản kháng ngầm. Cuốn sách phác họa một cách rùng rợn về việc chính quyền kiểm soát tư duy con người bằng cách bóp méo sự thật, thao túng ngôn ngữ (Newspeak) và triệt tiêu mọi cảm xúc cá nhân, để lại lời cảnh báo sâu sắc về giá trị vô giá của tự do và sự thật độc lập.', 66500.00),
	(7, '9786041234599', 'Nóng Trong Lòng', 3, 3, NULL, NULL, 'Tiếng Việt', 8, 7, 'Kệ VH-08', '/uploads/books/1779207526539_nongtronglong.jpg', NULL, 55000.00, 0.00, '2026-05-19 16:18:46', '2026-05-28 03:14:03', 'Một tập truyện ngắn vô cùng tinh tế và giàu cảm xúc của nhà văn Nguyễn Nhật Ánh, tiếp tục khẳng định thương hiệu "người giữ ký ức cho tuổi học trò". Khác với những tác phẩm mang màu sắc hoài niệm tuổi thơ, cuốn sách này đi sâu khai thác những chuyển biến tâm lý tinh tế, những trăn trở, suy tư và cả những rung động đầu đời có phần ngơ ngác, vụng dại của lứa tuổi mới lớn khi đứng trước ngưỡng cửa trưởng thành. Qua những câu chuyện dung dị, quen thuộc về tình bạn, tình yêu, những mâu thuẫn gia đình và áp lực học đường, tác giả đã vẽ nên một bức tranh tâm lý vô cùng chân thực. Văn phong của Nguyễn Nhật Ánh vẫn mộc mạc, dí dỏm nhưng ở tác phẩm này lại lắng đọng thêm nhiều nốt trầm chiêm nghiệm. Cuốn sách như một làn gió mát làm dịu đi những "cơn nóng" bồng bột, những tổn thương vô hình trong lòng người trẻ, giúp họ tìm thấy sự đồng cảm, học cách bao dung, trân trọng gia đình và biết cách đối diện với những thay đổi của bản thân một cách trưởng thành và điềm tĩnh hơn.', 38500.00),
	(8, '978013235', 'Clean Code', 4, 1, NULL, NULL, 'Tiếng Việt', 3, 0, 'Kệ CNTT-05', '/uploads/books/1778858079273_Clean Code.jpg', NULL, 320000.00, 0.00, '2026-05-24 11:29:33', '2026-05-28 03:14:03', NULL, 224000.00),
	(9, '9786041234593', 'Python Programming', 2, 1, NULL, NULL, 'Tiếng Việt', 10, 6, 'Kệ CNTT-06', '/uploads/books/1779207587155_python.jpg', NULL, 175000.00, 0.00, '2026-05-19 16:19:47', '2026-05-28 03:14:03', 'Giáo trình nhập môn khoa học máy tính chuẩn mực được biên soạn bởi Giáo sư John Zelle, được áp dụng giảng dạy rộng rãi tại hàng trăm trường đại học danh tiếng trên toàn cầu. Cuốn sách sử dụng ngôn ngữ lập trình Python – một ngôn ngữ có cú pháp trong sáng, gần gũi với ngôn ngữ tự nhiên – làm công cụ chính để truyền tải các tư duy cốt lõi của khoa học máy tính. Thay vì chỉ tập trung vào việc giải thích cú pháp khô khan, tác giả khéo léo dẫn dắt người học qua các bài toán tư duy logic, cấu trúc dữ liệu, thuật toán cơ bản, lập trình hướng đối tượng (OOP) và thiết kế giao diện đồ họa (GUI). Mỗi chương đều đi kèm các ví dụ thực tế trực quan và hệ thống bài tập thực hành từ dễ đến khó. Đây là cuốn sách hoàn hảo cho người mới bắt đầu, giúp xây dựng một nền tảng tư duy lập trình vững chắc, làm bệ phóng để người học tiến xa hơn trong các lĩnh vực công nghệ cao như phát triển phần mềm, phân tích dữ liệu hay trí tuệ nhân tạo.', 122500.00),
	(10, '9786041234598', 'Clean Architecture', 4, 1, NULL, NULL, 'Tiếng Việt', 6, 4, 'Kệ CNTT-07', '/uploads/books/1779207605503_CleanArchitecture.jpg', NULL, 450000.00, 0.00, '2026-05-19 16:20:05', '2026-05-28 03:14:03', 'Tiếp nối thành công của Clean Code, cuốn sách này của Robert C. Martin nâng tầm tư duy của người lập trình lên mức vĩ mô – tầng kiến trúc hệ thống phần mềm. Cuốn sách giải quyết câu hỏi cốt lõi: Làm thế nào để xây dựng một hệ thống phần mềm có thể trường tồn với thời gian, dễ dàng thay đổi và mở rộng mà không bị sụp đổ dưới sức nặng của chính nó? Tác giả giới thiệu các nguyên lý thiết kế kinh điển như SOLID ở cấp độ component, sự phân chia ranh giới rõ ràng giữa các phân vùng chức năng (Business Rules, UI, Database, Frameworks). Kiến trúc sạch (Clean Architecture) hướng tới một hệ thống độc lập hoàn toàn với các thư viện hay công nghệ bên ngoài, giúp bạn có thể đổi từ cơ sở dữ liệu này sang cơ sở dữ liệu khác, hoặc thay đổi giao diện web thành mobile mà không cần sửa đổi logic cốt lõi của doanh nghiệp. Cuốn sách là tài liệu gối đầu giường không thể thiếu cho các Senior Developer và Software Architect muốn làm chủ nghệ thuật thiết kế hệ thống lớn.', 315000.00),
	(11, '9786041234600', 'Machine Learning Yearning', 2, 1, NULL, NULL, 'Tiếng Việt', 7, 3, 'Kệ CNTT-09', '/uploads/books/1779207634283_MachineLearningYearning.jpg', NULL, 220000.00, 0.00, '2026-05-19 16:20:34', '2026-05-28 03:14:03', 'Cuốn sách chuyên sâu và mang tính thực chiến cực kỳ cao của giáo sư Andrew Ng – một trong những bộ óc vĩ đại nhất của cuộc cách mạng Trí tuệ nhân tạo. Cuốn sách không đi sâu vào việc chứng minh các công thức toán học hay cú pháp code Machine Learning, mà tập trung hoàn toàn vào việc dạy người đọc "chiến lược" để xây dựng và tối ưu hóa một dự án Học máy trong thực tế doanh nghiệp. Tác giả chia sẻ những kinh nghiệm vô giá về cách phân chia tập dữ liệu (Train/Dev/Test), cách chẩn đoán lỗi của mô hình thông qua phân tích phương sai (Variance) và độ chệch (Bias), cách định hướng ưu tiên khi mô hình chạy sai, và cách tích hợp các kỹ thuật Học sâu (Deep Learning) vào hệ thống phức tạp. Đây là cẩm nang hành nghề bắt buộc cho các kỹ sư dữ liệu và nhà quản trị dự án AI, giúp tiết kiệm hàng tháng trời thử sai vô ích và định hình tư duy phát triển sản phẩm công nghệ một cách khoa học.', 154000.00),
	(12, '9786041234587', 'Sapiens - Lược Sử Loài Người', 5, 2, 2023, NULL, 'Tiếng Việt', 8, 6, 'Kệ XH-01', '/uploads/books/1778858891552_Sapiens.jpg', 'Lịch sử phát triển nhân loại', 195000.00, 0.00, '2026-05-17 03:28:50', '2026-05-28 03:14:03', 'Kiệt tác biên khảo mang tính đột phá toàn cầu của giáo sư triết học kiêm lịch sử Yuval Noah Harari, định hình lại cách chúng ta nhìn nhận về quá khứ và tương lai của chính giống loài mình. Cuốn sách đưa người đọc vào một hành trình vĩ mô kéo dài 70.000 năm lịch sử, giải thích cách một loài vượn người không có gì nổi bật ở Đông Phi lại có thể vươn lên trở thành kẻ thống trị tối cao của hành tinh xanh. Harari chỉ ra ba cuộc cách mạng lớn quyết định vận mệnh của Homo Sapiens: Cách mạng Nhận thức (khi con người biết tưởng tượng và tin vào các "huyền thoại chung" như tôn giáo, quốc gia, tiền tệ); Cách mạng Nông nghiệp (khi con người thuần hóa cây trồng nhưng cũng tự trói buộc mình); và Cách mạng Khoa học (kỷ nguyên giúp con người làm chủ công nghệ nhưng cũng đối mặt với nguy cơ tự diệt vong). Bằng lối viết sắc bén, kết hợp nhân chủng học, sinh học và triết học, tác phẩm mở ra những góc nhìn chấn động, kích thích tư duy phê phán sâu sắc về ý nghĩa của hạnh phúc và tương lai của nhân loại.', 136500.00),
	(13, '9786041234588', 'Đắc Nhân Tâm', 8, 2, 2021, NULL, 'Tiếng Việt', 15, 12, 'Kệ KNS-02', '/uploads/books/1778858450856_dacnhantam.jpg', 'Kinh điển nghệ thuật thu phục lòng người', 85000.00, 0.00, '2026-05-17 03:28:50', '2026-05-28 03:14:03', 'Được xem là cuốn sách tự lực (self-help) nổi tiếng và có doanh số bán chạy nhất mọi thời đại, tác phẩm của Dale Carnegie không chỉ là một cuốn sách hướng dẫn giao tiếp, mà là một triết lý sống nghệ thuật thu phục lòng người sâu sắc. Cuốn sách đưa ra các nguyên tắc cốt lõi, những bài học ứng xử tinh tế được đúc kết từ cuộc đời của các vĩ nhân lịch sử và trải nghiệm thực tế của chính tác giả. Bản chất của "Đắc nhân tâm" không phải là những thủ thuật thao túng tâm lý giả tạo, mà xuất phát từ lòng chân thành, sự thấu hiểu, biết lắng nghe và tôn trọng cái tôi của người khác. Cuốn sách hướng dẫn cách tạo thiện cảm trong lần đầu gặp gỡ, cách dẫn dắt người khác suy nghĩ theo mình mà không gây thù chuốc oán, và nghệ thuật khơi dậy tiềm năng, lòng tự trọng ở nhân viên hay người thân. Qua nhiều thập kỷ, những lời khuyên của Carnegie vẫn giữ nguyên giá trị thời đại, là cẩm nang sống gối đầu giường giúp hàng triệu người xây dựng mối quan hệ bền vững và gặt hái thành công.', 59500.00),
	(14, '9786041234590', 'Deep Work', 2, 2, 2024, NULL, 'Tiếng Việt', 7, 5, 'Kệ KNS-03', '/uploads/books/1778858497071_DeepWork.jpg', 'Kỹ năng làm việc tập trung sâu', 135000.00, 0.00, '2026-05-17 03:28:50', '2026-05-28 03:14:03', 'Trong kỷ nguyên số đầy rẫy những xao nhãng từ thông báo mạng xã hội, tin nhắn và email, tác phẩm của giáo sư khoa học máy tính Cal Newport xuất hiện như một cứu cánh, định hình lại phong cách làm việc hiệu suất cao. Tác giả đưa ra khái niệm "Deep Work" (Làm việc sâu) – khả năng tập trung cao độ không xao lãng vào một nhiệm vụ phức tạp về mặt nhận thức. Đây là kỹ năng siêu đẳng giúp bạn tiếp thu nhanh chóng các kiến thức khó và tạo ra những kết quả vượt trội trong thời gian ngắn. Đối lập với nó là "Shallow Work" (Làm việc nông) – những việc hành chính vặt vãnh tiêu tốn thời gian nhưng không tạo ra nhiều giá trị. Cuốn sách không chỉ chứng minh tầm quan trọng của Deep Work dựa trên khoa học thần kinh mà còn cung cấp 4 nguyên tắc thực kỷ luật nghiêm ngặt: làm việc chuyên sâu, biến sự buồn chán thành thói quen, từ bỏ mạng xã hội một cách thông minh và cắt giảm các công việc nông, giúp người đọc giành lại sự chủ động cho bộ não và sự nghiệp.', 94500.00),
	(15, '9786041234591', 'Grit - Sức Mạnh Của Đam Mê', 5, 2, 2023, NULL, 'Tiếng Việt', 9, 7, 'Kệ KNS-04', '/uploads/books/1778859082984_Grit.jpg', 'Bí mật của sự thành công bền bỉ', 145000.00, 0.00, '2026-05-17 03:28:50', '2026-05-28 03:14:03', 'Cuốn sách tâm lý học hành vi chấn động của Giáo sư Angela Duckworth đã phá tan huyền thoại về "tài năng bẩm sinh" để khẳng định một chân lý mới: Bí mật của sự thành công vượt trội nằm ở GRIT – sự kết hợp bền bỉ giữa niềm đam mê dài hạn và lòng kiên trì vượt qua nghịch cảnh. Dựa trên các công trình nghiên cứu khoa học thực địa quy mô lớn tại học viện quân sự West Point, các cuộc thi đánh vần quốc gia và các tập đoàn hàng đầu, tác giả chứng minh rằng chỉ số thông minh IQ hay tài năng thiên bẩm chỉ là điều kiện cần, còn nỗ lực và sự bền bỉ mới nhân đôi tài năng đó thành thành quả thực tế. Cuốn sách hướng dẫn chi tiết cách nuôi dưỡng lòng kiên trì từ bên trong (thông qua đam mê, luyện tập có chủ đích, tìm kiếm mục đích sống và tư duy tiến bộ) cũng như cách thúc đẩy lòng kiên trì từ bên ngoài (cách cha mẹ, thầy cô hay người lãnh đạo rèn luyện cho con trẻ), truyền cảm hứng mạnh mẽ cho bất kỳ ai muốn kiên định đi đến tận cùng ước mơ.', 101500.00),
	(16, '9786041234594', 'Kinh Tế Học Hành Vi', 1, 2, NULL, NULL, 'Tiếng Việt', 8, 6, 'Kệ KT-06', '/uploads/books/1779207658133_kinhte.jpg', NULL, 155000.00, 0.00, '2026-05-19 16:20:58', '2026-05-28 03:14:03', 'Tác phẩm xuất sắc của giáo sư đoạt giải Nobel Kinh tế Richard H. Thaler, đưa người đọc đi sâu khám phá một lĩnh vực khoa học đầy thú vị: sự giao thoa giữa kinh tế học và tâm lý học con người. Khác với nền kinh tế học truyền thống luôn giả định con người là những "sinh vật lý trí" (Econs) luôn tính toán chuẩn xác lợi ích, Thaler chứng minh rằng thế giới thực được vận hành bởi những con người bằng xương bằng thịt đầy rẫy những định kiến, sai lầm và hành vi phi lý trí mang tính hệ thống. Qua những ví dụ hài hước, sinh động về cách chúng ta chi tiêu tiền bạc, chọn mua nhà, hay sa bẫy trong các chương trình khuyến mãi, tác giả giải thích các khái niệm như "tâm lý học kế toán", "hiệu ứng sở hữu" và đặc biệt là lý thuyết "Hích" (Nudge) – cách thiết kế môi trường lựa chọn để định hướng hành vi con người theo hướng tốt đẹp hơn mà không cần ép buộc. Cuốn sách mở ra một tầm nhìn mới cho các nhà làm chính sách, các doanh nhân và người tiêu dùng thông thái.', 108500.00),
	(17, '9786041234596', 'Atomic Habits', 5, 2, NULL, NULL, 'Tiếng Việt', 11, 9, 'Kệ KNS-05', '/uploads/books/1779207717136_AtomicHabits.jpg', NULL, 165000.00, 0.00, '2026-05-19 16:21:57', '2026-05-28 03:14:03', 'Là một trong những cuốn sách về phát triển bản thân xuất sắc nhất kỷ nguyên hiện đại, tác phẩm của James Clear cung cấp một hệ thống phương pháp luận cực kỳ khoa học và thực tế để thay đổi cuộc đời thông qua việc tối ưu hóa những thói quen nhỏ. Tác giả chứng minh rằng: Nếu mỗi ngày bạn tiến bộ 1%, sau một năm bạn sẽ giỏi hơn gấp 37 lần. Bí mật của sự thay đổi không nằm ở những mục tiêu vĩ mô to tát, mà nằm ở hệ thống các thói quen tí hon (Atomic Habits). Cuốn sách chia sẻ "4 Quy luật của hành vi" giúp bạn dễ dàng thiết lập thói quen tốt và loại bỏ thói quen xấu: Khiến nó rõ ràng, Khiến nó hấp dẫn, Khiến nó dễ dàng và Khiến nó thỏa mãn. Bằng sự kết hợp nhuần nhuyễn giữa kiến thức sinh học, khoa học thần kinh và những câu chuyện thực tế truyền cảm hứng của các vận động viên Olympic, các CEO hàng đầu, James Clear mang đến một cẩm nang hành động từng bước một, giúp bạn làm chủ hành vi, xây dựng lối sống lành mạnh và đạt được hiệu suất đỉnh cao.', 115500.00),
	(18, '9786041234601', 'Toán học cao cấp', 2, 4, NULL, NULL, 'Tiếng Việt', 5, 5, 'Kệ TOAN-01', '/uploads/books/1779207844889_Toanhoccaocap.jpg', NULL, 120000.00, 0.00, '2026-05-19 16:24:04', '2026-05-28 03:14:03', 'Cuốn giáo trình chuyên sâu được biên soạn nhằm xây dựng chiếc cầu nối vững chắc giữa thế giới toán học lý thuyết trừu tượng và các ứng dụng thực tế trong ngành Trí tuệ nhân tạo (AI) và Học máy (Machine Learning). Cuốn sách tập trung hệ thống hóa 4 mảng kiến thức toán học nền tảng và cốt lõi nhất mà mọi kỹ sư dữ liệu bắt buộc phải làm chủ: Đại số tuyến tính (ma trận, vector, định thức, không gian vector); Giải tích đa biến (đạo hàm riêng, gradient descent – trái tim của mạng neural); Lý thuyết xác suất và thống kê (các phân phối xác suất, định lý Bayes, ước lượng tham số); và Toán học tối ưu (các bài toán quy hoạch tuyến tính, cực trị có điều kiện). Với cách tiếp cận hiện đại, mỗi khái niệm toán học đều được tác giả giải thích rõ ràng kèm theo các ví dụ minh họa bằng code thuật toán AI thực tế, giúp người học không bị ngợp trước các công thức mà hiểu sâu sắc bản chất vận hành phía sau của các mô hình học sâu (Deep Learning).', 84000.00),
	(19, '9786041234602', 'Đại Số Tuyến Tính', 1, 4, NULL, NULL, 'Tiếng Việt', 10, 9, 'Kệ TOAN-02', '/uploads/books/1779207892871_daisotuyentinh.jpg', NULL, 95000.00, 0.00, '2026-05-19 16:24:52', '2026-05-28 03:14:03', 'Giáo trình chuẩn mực của Nhà giáo Ưu tú Lê Đình Thúy, là cuốn sách gối đầu giường không thể thiếu cho sinh viên các khối ngành khoa học tự nhiên, kỹ thuật, công nghệ thông tin và đặc biệt là khối ngành kinh tế - tài chính trên toàn quốc. Tác phẩm nổi tiếng với phương pháp tiếp cận sư phạm mẫu mực, mạch lạc, đi từ cơ bản đến nâng cao. Nội dung sách bao phủ toàn bộ các kiến thức trọng tâm về lý thuyết ma trận, định thức, hệ phương trình tuyến tính, không gian vector, ánh xạ tuyến tính, dạng toàn phương và không gian Euclide. Điểm đặc biệt làm nên giá trị của cuốn sách là tác giả không chỉ đưa ra các định lý, chứng minh khô khan mà luôn lồng ghép các bài toán ứng dụng thực tế vô cùng đặc sắc như: mô hình cân đối liên ngành Leontief trong kinh tế, bài toán tối ưu hóa nguồn lực sản xuất, giúp người học hiểu rõ giá trị thực tiễn và tư duy logic của toán học.', 66500.00),
	(20, '9786041234603', 'Lược Sử Thời Gian', 5, 5, NULL, NULL, 'Tiếng Việt', 6, 6, 'Kệ VLY-01', '/uploads/books/1779207933002_Luocsuthoigian.jpg', NULL, 115000.00, 0.00, '2026-05-19 16:25:32', '2026-05-28 03:14:03', 'Một trong những cuốn sách phổ biến khoa học vĩ đại và bán chạy nhất mọi thời đại của nhà vật lý lý thuyết thiên tài Stephen Hawking. Cuốn sách là nỗ lực phi thường của Hawking nhằm giải thích những bí ẩn sâu thẳm và phức tạp nhất của vũ trụ bao la bằng một ngôn ngữ giản dị, trong sáng dành cho đại chúng, hoàn toàn không chứa các công thức toán học phức tạp (ngoại trừ phương trình E=mc² của Einstein). Tác giả dẫn dắt người đọc qua các lý thuyết nền tảng của vật lý hiện đại: từ Thuyết tương đối tổng quát của Einstein giải thích thế giới vĩ mô, đến Cơ học lượng tử giải thích thế giới vi mô của các hạt dưới nguyên tử. Từ đó, Hawking mở ra những cuộc thảo luận chấn động về nguồn gốc của vũ trụ (Thuyết Vụ Nổ Lớn - Big Bang), bản chất bí ẩn của Hố đen, sự giãn nở của không-thời gian và hướng tới một "Thuyết vạn vật" thống nhất, kích thích trí tưởng tượng và khát khao khám phá tri thức của nhân loại.', 80500.00),
	(21, '9786041234604', 'Hóa Học Hữu Cơ Căn Bản', 3, 6, NULL, NULL, 'Tiếng Việt', 8, 7, 'Kệ HOA-01', '/uploads/books/1779207961953_Hoahochuuco.jpg', NULL, 85000.00, 0.00, '2026-05-19 16:26:01', '2026-05-28 03:14:03', 'Bộ giáo trình hàn lâm uy tín cấp quốc gia được chủ biên bởi Giáo sư Ngô Thị Thuận, là tài liệu giảng dạy cốt lõi dành cho sinh viên chuyên ngành Hóa học, Sinh học, Dược học và Công nghệ thực phẩm tại các trường đại học lớn. Cuốn sách cung cấp một hệ thống kiến thức toàn diện và sâu sắc về cấu trúc, danh pháp, tính chất vật lý và hóa học của các hợp chất hữu cơ từ hydrocacbon no, không no, thơm đến các dẫn xuất chứa oxy, nitơ và các hợp chất thiên nhiên phức tạp như cacbohydrat, amino axit. Tác giả đặc biệt chú trọng việc giải thích bản chất của các cơ chế phản ứng hữu cơ (thế, cộng, tách) thông qua lý thuyết orbital phân tử và hiệu ứng cấu trúc, giúp người học không chỉ học vẹt các phương trình phản ứng mà hình thành tư duy phân tích, dự đoán sản xuất và ứng dụng vào các ngành công nghiệp tổng hợp dược phẩm, hóa dầu thực tế.', 59500.00),
	(22, '9786041234605', 'Giáo Trình Luật Dân Sự Việt Nam', 7, 7, NULL, NULL, 'Tiếng Việt', 12, 11, 'Kệ LUAT-01', '/uploads/books/1779207998100_luatdansu.jpg', NULL, 110000.00, 0.00, '2026-05-19 16:26:38', '2026-05-28 03:14:03', 'Tác phẩm được biên soạn bởi hội đồng học thuật đầu ngành trực thuộc Trường Đại học Luật Hà Nội, là tài liệu giảng dạy và nghiên cứu pháp lý có tính chuẩn mực, chính thống bậc nhất tại Việt Nam. Cuốn sách bám sát các quy định, tinh thần của Bộ luật Dân sự hiện hành, cung cấp hệ thống kiến thức nền tảng về phần chung của luật dân sự (chủ thể, hộ tịch, tài sản, giao dịch dân sự, đại diện, thời hiệu) và các chế định chuyên sâu cốt lõi như Quyền sở hữu và các quyền khác đối với tài sản, Lý thuyết về Nghĩa vụ và Hợp đồng dân sự, cũng như Pháp luật về Thừa kế. Bằng phương pháp phân tích khoa học kết hợp giữa lý luận và thực tiễn xét xử, cuốn sách giải thích rõ ràng các nguyên lý pháp lý, quyền và nghĩa vụ hợp pháp của các bên, là cẩm nang bắt buộc cho sinh viên luật, luật sư, thẩm phán và các nhà quản lý.', 77000.00),
	(23, '9786041234606', 'Khi Hơi Thở Hóa Thinh Không', 9, 8, NULL, NULL, 'Tiếng Việt', 7, 6, 'Kệ YHOC-01', '/uploads/books/1779208026459_Khihoitho.jpg', NULL, 120000.00, 0.00, '2026-05-19 16:27:06', '2026-05-28 03:14:03', 'Cuốn tự truyện vô cùng xúc động và thấm đẫm triết lý nhân sinh của Paul Kalanithi – một bác sĩ phẫu thuật thần kinh tài hoa đầy triển vọng bỗng chốc trở thành một bệnh nhân ung thư phổi giai đoạn cuối ở tuổi 36. Cuốn sách được viết trong những tháng ngày cuối cùng của cuộc đời, khi Paul đang đứng ở ranh giới mong manh giữa sự sống và cái chết. Với ngòi bút tinh tế của một người từng học thạc sĩ văn học Anh tại Stanford kết hợp với trải nghiệm sâu sắc của một bác sĩ y khoa tại Yale, tác giả đã tái hiện lại hành trình đầy kiêu hãnh của mình: từ một cậu sinh viên tò mò về ý nghĩa cuộc sống, một bác sĩ thức trắng đêm trong phòng mổ để giành giật mạng sống cho bệnh nhân, cho đến khi chính mình phải đối diện với án tử hình của căn bệnh quái ác. Tác phẩm không phải là một tiếng khóc than bi lụy, mà là một lời tự tình, một bài ca ca ngợi nghị lực sống, tình yêu gia đình và câu trả lời sâu sắc cho câu hỏi: Điều gì khiến cuộc sống đáng sống khi tương lai không còn nữa?', 84000.00),
	(24, '9786041234597', 'Lịch Sử Việt Nam', 7, 9, NULL, NULL, 'Tiếng Việt', 10, 8, 'Kệ LS-01', '/uploads/books/1779208071200_lichsuvietnam.jpg', NULL, 120000.00, 0.00, '2026-05-19 16:27:51', '2026-05-28 03:14:03', 'Một công trình biên khảo lịch sử đồ sộ và nghiêm túc, phác họa lại toàn bộ tiến trình lịch sử thăng trầm nhưng đầy oanh liệt của dân tộc Việt Nam từ thuở bình minh dựng nước đến kỷ nguyên hiện đại. Cuốn sách được chia làm nhiều phần mạch lạc, dẫn dắt người đọc đi qua thời kỳ tiền sử, giai đoạn huyền sử Hùng Vương, một ngàn năm bắc thuộc kiên cường đấu tranh giành độc lập, cho đến thời kỳ tự chủ hoàng kim của các triều đại Lý, Trần, Lê, Nguyễn. Tác phẩm không chỉ đơn thuần liệt kê các mốc thời gian hay các trận đánh khô khan, mà đi sâu phân tích bối cảnh chính trị, sự phát triển văn hóa, bang giao quốc tế, tư duy quân sự và các thiết chế kinh tế - xã hội qua từng thời kỳ. Đây là tư liệu lịch sử vô giá giúp người đọc hiểu rõ cội nguồn, bồi đắp lòng tự hào dân tộc và rút ra những bài học quý báu từ quá khứ để hướng tới tương lai.', 84000.00),
	(25, '9786041234607', 'Việt Nam Sử Lược', 9, 9, NULL, NULL, 'Tiếng Việt', 5, 5, 'Kệ LS-02', '/uploads/books/1779208116251_vnsuluoc.jpg', NULL, 145000.00, 0.00, '2026-05-19 16:28:36', '2026-05-28 03:14:03', NULL, 101500.00),
	(26, '9786041234608', 'Thế Giới Của Sophie', 6, 10, NULL, NULL, 'Tiếng Việt', 6, 4, 'Kệ TRIET-01', '/uploads/books/1779208159326_thegioisophe.jpg', NULL, 175000.00, 0.00, '2026-05-19 16:29:19', '2026-05-28 03:14:03', NULL, 122500.00),
	(27, '9786041234609', 'Tâm Lý Học Đám Đông', 5, 11, NULL, NULL, 'Tiếng Việt', 14, 12, 'Kệ TLY-01', '/uploads/books/1779208191452_tamlyhocdamdong.jpg', NULL, 95000.00, 0.00, '2026-05-19 16:29:51', '2026-05-28 03:14:03', NULL, 66500.00),
	(28, '9786041234610', 'Cambridge IELTS 18', 4, 12, NULL, NULL, 'Tiếng Việt', 20, 18, 'Kệ NNGU-01', '/uploads/books/1779208215263_18.jpg', NULL, 240000.00, 0.00, '2026-05-19 16:30:15', '2026-05-28 03:14:03', NULL, 168000.00),
	(29, '9786041234611', 'Câu Chuyện Nghệ Thuật', 8, 13, NULL, NULL, 'Tiếng Việt', 4, 4, 'Kệ MNET-01', '/uploads/books/1779208248114_cauchuyennghethuat.jpg', NULL, 450000.00, 0.00, '2026-05-19 16:30:48', '2026-05-28 03:14:03', NULL, 315000.00),
	(30, '9786041234612', 'Y Học Dinh Dưỡng', 1, 14, NULL, NULL, 'Tiếng Việt', 9, 9, 'Kệ TTSK-01', '/uploads/books/1779208284667_yhocdinhduong.jpg', NULL, 130000.00, 0.00, '2026-05-19 16:31:24', '2026-05-28 03:14:03', NULL, 91000.00),
	(31, '9786041234613', 'Cách Mạng Một Cọng Rơm', 5, 15, NULL, NULL, 'Tiếng Việt', 8, 8, 'Kệ NN-01', '/uploads/books/1779208660563_cachmangmotcongrom.jpg', NULL, 88000.00, 0.00, '2026-05-19 16:37:40', '2026-05-28 03:14:03', NULL, 61600.00),
	(32, '9786041234614', 'Mùa Xuân Vắng Lặng', 2, 16, NULL, NULL, 'Tiếng Việt', 5, 5, 'Kệ MTR-01', '/uploads/books/1779208748257_Muaxuanvanglang.jpg', NULL, 125000.00, 0.00, '2026-05-19 16:39:08', '2026-05-28 03:14:03', NULL, 87500.00),
	(33, '9786041234615', 'Quân Vương', 9, 17, NULL, NULL, 'Tiếng Việt', 6, 6, 'Kệ CT-01', '/uploads/books/1779208766181_quanvuong.jpg', NULL, 75000.00, 0.00, '2026-05-19 16:39:26', '2026-05-28 03:14:03', NULL, 52500.00),
	(34, '9786041234616', 'Điểm Bùng Phát', 5, 18, NULL, NULL, 'Tiếng Việt', 7, 6, 'Kệ XHH-01', '/uploads/books/1779208781610_diembungphat.jpg', NULL, 140000.00, 0.00, '2026-05-19 16:39:41', '2026-05-28 03:14:03', NULL, 98000.00),
	(35, '9786041234617', 'Xách Ba Lô Lên Và Đi', 3, 19, NULL, NULL, 'Tiếng Việt', 10, 10, 'Kệ DL-01', '/uploads/books/1779208796144_xachbalo.jpg', NULL, 95000.00, 0.00, '2026-05-19 16:39:56', '2026-05-28 03:14:03', NULL, 66500.00),
	(36, '9786041234618', 'Nghề Báo Gian Khổ Quyến Rũ', 7, 20, NULL, NULL, 'Tiếng Việt', 4, 4, 'Kệ BCTT-01', '/uploads/books/1779208811543_nghenhabao.jpg', NULL, 110000.00, 0.00, '2026-05-19 16:40:11', '2026-05-28 03:14:03', NULL, 77000.00),
	(37, '9786041234619', 'Kỹ Thuật Mạch Điện Tử', 2, 21, NULL, NULL, 'Tiếng Việt', 12, 11, 'Kệ DTVT-01', '/uploads/books/1779208646645_kythuatmachdientu.jpg', NULL, 115000.00, 0.00, '2026-05-19 16:37:26', '2026-05-28 03:14:03', NULL, 80500.00),
	(38, '9786041234620', 'Kiến Trúc Việt Nam', 4, 22, NULL, NULL, 'Tiếng Việt', 5, 5, 'Kệ XDKT-01', '/uploads/books/1779208508741_kientruc.jpg', NULL, 240000.00, 0.00, '2026-05-19 16:35:08', '2026-05-28 03:14:03', NULL, 168000.00),
	(39, '9786041234621', 'Trà Kinh', 3, 23, NULL, NULL, 'Tiếng Việt', 6, 5, 'Kệ TPDU-01', '/uploads/books/1779208553730_traking.jpg', NULL, 180000.00, 0.00, '2026-05-19 16:35:53', '2026-05-28 03:14:03', NULL, 126000.00),
	(40, '9786041234622', 'Những quý cô thời trang', 8, 24, NULL, NULL, 'Tiếng Việt', 5, 4, 'Kệ TTR-01', '/uploads/books/1779208627159_nhungquyco.jpg', NULL, 260000.00, 0.00, '2026-05-19 16:37:07', '2026-05-28 03:14:03', NULL, 182000.00);

-- Dumping structure for table library_db.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `1` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.categories: ~24 rows (approximately)
INSERT INTO `categories` (`id`, `name`, `description`, `parent_id`) VALUES
	(1, 'Công nghệ thông tin1', 'Sách khoa học máy tính, lập trình, an toàn thông tin', NULL),
	(2, 'Kinh tế', 'Sách kinh tế học, tài chính doanh nghiệp, quản trị', NULL),
	(3, 'Văn học', 'Sách tiểu thuyết, truyện ngắn, thơ ca trong và ngoài nước', NULL),
	(4, 'Toán học', 'Sách giáo trình toán cao cấp, toán ứng dụng', NULL),
	(5, 'Vật lý', 'Sách nghiên cứu vật lý lý thuyết và ứng dụng', NULL),
	(6, 'Hóa học', 'Sách hóa học cơ bản, hóa hữu cơ và vô cơ', NULL),
	(7, 'Luật', 'Sách văn bản luật, giáo trình luật dân sự, hình sự', NULL),
	(8, 'Y học', 'Sách nghiên cứu y khoa, chăm sóc sức khỏe, tự truyện y học', NULL),
	(9, 'Lịch sử', 'Sách lịch sử Việt Nam và lịch sử thế giới', NULL),
	(10, 'Triết học', 'Sách triết học phương Đông, phương Tây và tư tưởng', NULL),
	(11, 'Tâm lý học', 'Sách nghiên cứu tâm lý hành vi, tâm lý học xã hội', NULL),
	(12, 'Ngoại ngữ', 'Sách giáo trình, tài liệu luyện thi chứng chỉ tiếng Anh, Trung, Nhật', NULL),
	(13, 'Âm nhạc - Nghệ thuật', 'Sách lịch sử hội họa, âm nhạc, điêu khắc', NULL),
	(14, 'Thể thao - Sức khỏe', 'Sách hướng dẫn rèn luyện thể chất, dinh dưỡng', NULL),
	(15, 'Nông nghiệp', 'Sách kỹ thuật canh tác, nông nghiệp tự nhiên', NULL),
	(16, 'Môi trường', 'Sách nghiên cứu sinh thái, bảo vệ môi trường toàn cầu', NULL),
	(17, 'Chính trị', 'Sách lý luận chính trị, khoa học chính trị kinh điển', NULL),
	(18, 'Xã hội học', 'Sách nghiên cứu cấu trúc, xu hướng xã hội', NULL),
	(19, 'Du lịch', 'Sách cẩm nang, du ký, khám phá văn hóa các vùng miền', NULL),
	(20, 'Báo chí - Truyền thông', 'Sách kỹ thuật viết báo, truyền thông đại chúng', NULL),
	(21, 'Điện tử - Viễn thông', 'Sách kỹ thuật mạch, phần cứng, mạng viễn thông', NULL),
	(22, 'Xây dựng - Kiến trúc', 'Sách thiết kế không gian, kỹ thuật thi công công trình', NULL),
	(23, 'Thực phẩm - Đồ uống', 'Văn hóa ẩm thực, nghệ thuật pha chế, thưởng trà', NULL),
	(24, 'Thời trang - May mặc', 'Lịch sử thiết kế, xu hướng và kỹ thuật may mặc', NULL);

-- Dumping structure for table library_db.contact_messages
CREATE TABLE IF NOT EXISTS `contact_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` enum('UNREAD','READ','REPLIED') DEFAULT NULL,
  `student_code` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `replied_at` datetime(6) DEFAULT NULL,
  `reply_content` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.contact_messages: ~3 rows (approximately)
INSERT INTO `contact_messages` (`id`, `created_at`, `email`, `full_name`, `message`, `phone`, `status`, `student_code`, `subject`, `replied_at`, `reply_content`) VALUES
	(4, '2026-05-27 11:24:29.203731', 'Nhi123@gmail.com', 'lenhi', 'dffa', '0348155189', 'UNREAD', 'SV-2026-5', 'borrow', NULL, NULL),
	(5, '2026-05-27 11:58:54.456533', 'Nhi123@gmail.com', 'lenhi', 'styj', '0348155189', 'REPLIED', 'SV-2026-5', 'card', '2026-05-27 11:59:13.343863', 'helo'),
	(6, '2026-05-27 12:25:02.068329', 'Nhi123@gmail.com', 'Lê Thị Yến Nhi', 'gigi', '0434874433', 'UNREAD', NULL, 'borrow', NULL, NULL);

-- Dumping structure for table library_db.digital_documents
CREATE TABLE IF NOT EXISTS `digital_documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `downloads` int(11) DEFAULT 0,
  `file_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `views` int(11) DEFAULT 0,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.digital_documents: ~35 rows (approximately)
INSERT INTO `digital_documents` (`id`, `author`, `category`, `created_at`, `downloads`, `file_url`, `title`, `type`, `views`, `year`) VALUES
	(1, 'Nguyễn Nhật Ánh', 'Văn học', '2026-05-27 23:37:59.198354', 0, '/uploads/documents/56c757bd-4ac4-4d2c-bdd9-868905c17c10_23052026BAOCAOBIGDATA (1).pdf', 'Giáo trình Ngữ văn hiện đại', 'giaotrinh', 0, 2021),
	(2, 'Robert C. Martin', 'Công nghệ thông tin', '2026-05-27 23:40:03.236113', 0, '/uploads/documents/af7269d4-e27e-48d6-b42d-d9274ad679bf_Hệ_thống_giám_sát_chất_lượng_không_khí.pdf', 'Hướng dẫn kiến trúc phần mềm sạch', 'giaotrinh', 0, 2020),
	(3, 'Nguyễn Du', 'Văn học', '2026-05-27 18:10:00.000000', 95, '/files/doc3.pdf', 'Luận văn phân tích thi pháp văn học', 'luanvan', 200, 2022),
	(4, 'Paulo Coelho', 'Văn học', '2026-05-27 18:15:00.000000', 45, '/files/doc4.pdf', 'Báo cáo phương pháp sáng tác văn chương', 'luanvan', 120, 2022),
	(5, 'Yuval Noah Harari', 'Kinh tế', '2026-05-27 18:20:00.000000', 60, '/files/doc5.pdf', 'Nghiên cứu lịch sử phát triển kinh tế', 'nckh', 150, 2023),
	(6, 'Dale Carnegie', 'Kinh tế', '2026-05-27 18:25:00.000000', 80, '/files/doc6.pdf', 'Tài liệu quản trị hành vi và giao tiếp', 'nckh', 220, 2021),
	(7, 'J.K. Rowling', 'Văn học', '2026-05-27 18:30:00.000000', 120, '/files/doc7.pdf', 'Đề tài nghiên cứu văn học giả tưởng', 'nckh', 300, 2023),
	(8, 'Cal Newport', 'Kinh tế', '2026-05-27 18:35:00.000000', 70, '/files/doc8.pdf', 'Báo cáo chuyên đề năng suất lao động', 'baobao', 180, 2024),
	(9, 'Angela Duckworth', 'Kinh tế', '2026-05-27 18:40:00.000000', 50, '/files/doc9.pdf', 'Tài liệu kỹ năng rèn luyện kiên trì', 'baobao', 130, 2023),
	(10, 'John Zelle', 'Công nghệ thông tin', '2026-05-27 18:45:00.000000', 300, '/files/doc10.pdf', 'Bộ đề thực hành lập trình Python', 'dethi', 600, 2024),
	(11, 'Richard H. Thaler', 'Kinh tế', '2026-05-27 18:50:00.000000', 250, '/files/doc11.pdf', 'Tài liệu tham khảo kinh tế học hành vi', 'dethi', 500, 2023),
	(12, 'George Orwell', 'Văn học', '2026-05-27 18:55:00.000000', 180, '/files/doc12.pdf', 'Tài liệu lý luận văn học hiện đại', 'dethi', 350, 2022),
	(13, 'James Clear', 'Kinh tế', '2026-05-27 19:00:00.000000', 40, '/files/doc13.pdf', 'Báo cáo quy trình xây dựng thói quen', 'baobao', 90, 2024),
	(14, 'Trần Trọng Kim', 'Lịch sử', '2026-05-27 19:05:00.000000', 500, '/files/doc14.pdf', 'Tài liệu khảo cứu lịch sử Việt Nam', 'baobao', 1200, 2021),
	(15, 'Andrew Ng', 'Công nghệ thông tin', '2026-05-27 19:10:00.000000', 30, '/files/doc15.pdf', 'Luận văn trí tuệ nhân tạo nâng cao', 'luanvan', 80, 2024),
	(16, 'Nguyễn Hải Thanh', 'Toán học', '2026-05-27 19:15:00.000000', 110, '/files/doc16.pdf', 'Giáo trình đại số tuyến tính chuyên sâu', 'giaotrinh', 280, 2023),
	(17, 'Stephen Hawking', 'Vật lý', '2026-05-27 19:20:00.000000', 65, '/files/doc17.pdf', 'Đề tài nghiên cứu vũ trụ học', 'nckh', 140, 2022),
	(18, 'Ngô Thị Thuận', 'Hóa học', '2026-05-27 19:25:00.000000', 400, '/files/doc18.pdf', 'Bộ đề thi hóa học hữu cơ tổng hợp', 'dethi', 800, 2021),
	(19, 'Đại học Luật Hà Nội', 'Luật', '2026-05-27 19:30:00.000000', 55, '/files/doc19.pdf', 'Giáo trình luật dân sự Việt Nam', 'baobao', 110, 2023),
	(20, 'Paul Kalanithi', 'Y học', '2026-05-27 19:35:00.000000', 40, '/files/doc20.pdf', 'Luận văn nghiên cứu y học lâm sàng', 'luanvan', 95, 2022),
	(21, 'Jostein Gaarder', 'Văn học', '2026-05-27 20:00:00.000000', 120, '/files/doc21.pdf', 'Báo cáo phương pháp giáo dục triết học', 'baobao', 250, 2025),
	(22, 'Gustave Le Bon', 'Kinh tế', '2026-05-27 20:05:00.000000', 85, '/files/doc22.pdf', 'Tài liệu hướng dẫn tâm lý học đám đông', 'giaotrinh', 190, 2024),
	(23, 'Cambridge University Press', 'Toán học', '2026-05-27 20:10:00.000000', 45, '/files/doc23.pdf', 'Luận văn thống kê toán học ứng dụng', 'luanvan', 110, 2023),
	(24, 'E.H. Gombrich', 'Vật lý', '2026-05-27 20:15:00.000000', 60, '/files/doc24.pdf', 'Đề tài nghiên cứu ứng dụng vật liệu', 'nckh', 130, 2024),
	(25, 'Ray D. Strand', 'Luật', '2026-05-27 20:20:00.000000', 90, '/files/doc25.pdf', 'Bộ đề thi pháp luật hành chính đại cương', 'dethi', 210, 2025),
	(26, 'Masanobu Fukuoka', 'Y học', '2026-05-27 20:25:00.000000', 35, '/files/doc26.pdf', 'Tài liệu hướng dẫn chẩn đoán y khoa', 'giaotrinh', 85, 2024),
	(27, 'Rachel Carson', 'Hóa học', '2026-05-27 20:30:00.000000', 110, '/files/doc27.pdf', 'Báo cáo phân tích định lượng hóa học', 'baobao', 220, 2023),
	(28, 'Niccolò Machiavelli', 'Lịch sử', '2026-05-27 20:35:00.000000', 75, '/files/doc28.pdf', 'Nghiên cứu văn hóa lịch sử Phục Hưng', 'nckh', 160, 2024),
	(29, 'Malcolm Gladwell', 'Công nghệ thông tin', '2026-05-27 20:40:00.000000', 200, '/files/doc29.pdf', 'Tài liệu an toàn bảo mật thông tin', 'giaotrinh', 400, 2025),
	(30, 'Huyền Chip', 'Kinh tế', '2026-05-27 20:45:00.000000', 50, '/files/doc30.pdf', 'Luận văn quản lý logistics toàn cầu', 'luanvan', 125, 2024),
	(31, 'Hữu Thọ', 'Toán học', '2026-05-27 20:50:00.000000', 130, '/files/doc31.pdf', 'Đề thi chọn học sinh giỏi toán ứng dụng', 'dethi', 280, 2025),
	(32, 'Đỗ Thanh Việt', 'Vật lý', '2026-05-27 20:55:00.000000', 40, '/files/doc32.pdf', 'Báo cáo chuyên đề nhiệt động lực học', 'baobao', 95, 2023),
	(33, 'Nguyễn Đức Thiềm', 'Hóa học', '2026-05-27 21:00:00.000000', 65, '/files/doc33.pdf', 'Giáo trình hóa học môi trường', 'giaotrinh', 140, 2024),
	(34, 'Nguyễn Ngọc Tuấn', 'Luật', '2026-05-27 21:05:00.000000', 55, '/files/doc34.pdf', 'Đề tài nghiên cứu luật kinh tế quốc tế', 'nckh', 120, 2024),
	(35, 'Đặng Thúy Hương', 'Y học', '2026-05-27 21:10:00.000000', 30, '/files/doc35.pdf', 'Tài liệu hướng dẫn điều trị bệnh', 'baobao', 70, 2025);

-- Dumping structure for table library_db.fines
CREATE TABLE IF NOT EXISTS `fines` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loan_id` bigint(20) NOT NULL,
  `member_id` bigint(20) NOT NULL,
  `fine_amount` decimal(12,2) NOT NULL,
  `days_overdue` int(11) NOT NULL,
  `fine_per_day` decimal(10,2) DEFAULT 2000.00,
  `reason` varchar(500) DEFAULT NULL,
  `status` enum('UNPAID','PAID','WAIVED') DEFAULT 'UNPAID',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `paid_at` datetime DEFAULT NULL,
  `paid_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loan_id` (`loan_id`),
  KEY `member_id` (`member_id`),
  KEY `paid_by` (`paid_by`),
  CONSTRAINT `1` FOREIGN KEY (`loan_id`) REFERENCES `loans` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `3` FOREIGN KEY (`paid_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.fines: ~0 rows (approximately)
INSERT INTO `fines` (`id`, `loan_id`, `member_id`, `fine_amount`, `days_overdue`, `fine_per_day`, `reason`, `status`, `created_at`, `paid_at`, `paid_by`) VALUES
	(1, 1, 1, 20000.00, 10, 2000.00, 'Trả sách quá hạn 10 ngày', 'UNPAID', '2026-05-28 03:38:28', NULL, NULL);

-- Dumping structure for table library_db.loan_items
CREATE TABLE IF NOT EXISTS `loan_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loan_id` bigint(20) NOT NULL,
  `book_copy_id` bigint(20) NOT NULL,
  `returned` tinyint(1) DEFAULT 0,
  `return_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loan_id` (`loan_id`),
  KEY `book_copy_id` (`book_copy_id`),
  CONSTRAINT `1` FOREIGN KEY (`loan_id`) REFERENCES `loans` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`book_copy_id`) REFERENCES `book_copies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.loan_items: ~13 rows (approximately)
INSERT INTO `loan_items` (`id`, `loan_id`, `book_copy_id`, `returned`, `return_date`) VALUES
	(1, 1, 1, 1, '2026-05-23 16:16:58'),
	(2, 2, 2, 0, NULL),
	(3, 4, 3, 1, '2026-05-24 01:47:17'),
	(4, 5, 4, 1, '2026-05-24 00:52:03'),
	(5, 6, 5, 0, NULL),
	(6, 7, 6, 0, NULL),
	(7, 8, 7, 0, NULL),
	(8, 9, 8, 1, '2026-05-24 01:53:04'),
	(9, 10, 9, 0, NULL),
	(10, 11, 10, 0, NULL),
	(11, 13, 11, 0, NULL),
	(12, 15, 12, 0, NULL),
	(13, 16, 13, 1, '2026-05-28 11:09:59');

-- Dumping structure for table library_db.loans
CREATE TABLE IF NOT EXISTS `loans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loan_code` varchar(30) NOT NULL,
  `member_id` bigint(20) NOT NULL,
  `librarian_id` bigint(20) DEFAULT NULL,
  `loan_date` datetime DEFAULT current_timestamp(),
  `due_date` date NOT NULL,
  `return_date` datetime DEFAULT NULL,
  `status` enum('PENDING','ACTIVE','RETURNED','OVERDUE','LOST') DEFAULT 'PENDING',
  `deposit_paid` decimal(12,2) DEFAULT 0.00,
  `deposit_refunded` decimal(12,2) DEFAULT 0.00,
  `note` text DEFAULT NULL,
  `deposit_status` enum('NONE','UNPAID','PAID','REFUNDED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loan_code` (`loan_code`),
  KEY `member_id` (`member_id`),
  KEY `librarian_id` (`librarian_id`),
  CONSTRAINT `1` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`librarian_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.loans: ~13 rows (approximately)
INSERT INTO `loans` (`id`, `loan_code`, `member_id`, `librarian_id`, `loan_date`, `due_date`, `return_date`, `status`, `deposit_paid`, `deposit_refunded`, `note`, `deposit_status`) VALUES
	(1, 'PM-197A', 7, 1, '2026-05-23 13:53:31', '2026-06-06', '2026-05-23 16:16:58', 'RETURNED', 0.00, 0.00, '', 'NONE'),
	(2, 'PM-6254', 7, 1, '2026-05-23 14:03:45', '2026-06-06', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(4, 'PM-7259', 7, 1, '2026-05-23 15:55:03', '2026-06-06', '2026-05-24 01:47:17', 'RETURNED', 0.00, 0.00, '', 'NONE'),
	(5, 'PM-F3F8', 7, 1, '2026-05-24 00:51:19', '2026-06-07', '2026-05-24 00:52:03', 'RETURNED', 0.00, 0.00, '', 'NONE'),
	(6, 'PM-275D', 7, 1, '2026-05-24 00:56:12', '2026-06-07', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(7, 'PM-5EE9', 7, 1, '2026-05-24 01:35:45', '2026-06-07', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(8, 'PM-861A', 7, 1, '2026-05-24 01:36:45', '2026-06-07', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(9, 'PM-0EC0', 7, 1, '2026-05-24 01:52:33', '2026-06-07', '2026-05-24 01:53:04', 'RETURNED', 0.00, 0.00, '', 'NONE'),
	(10, 'PM-517B', 7, 1, '2026-05-24 23:42:02', '2026-05-23', '2026-05-21 23:51:00', 'ACTIVE', 0.00, 0.00, '', 'NONE'),
	(11, 'PM-D779', 8, 1, '2026-05-26 09:53:22', '2026-06-09', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(13, 'PM-29E1', 8, 1, '2026-05-28 00:09:53', '2026-06-11', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(15, 'PM-49F0', 8, 1, '2026-05-28 00:16:49', '2026-05-29', NULL, 'PENDING', 0.00, 0.00, '', 'NONE'),
	(16, 'PM-DDDE', 7, 1, '2026-05-28 10:56:44', '2026-06-11', '2026-05-28 11:09:59', 'RETURNED', 45500.00, 0.00, '', 'REFUNDED');

-- Dumping structure for table library_db.members
CREATE TABLE IF NOT EXISTS `members` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `card_number` varchar(20) NOT NULL,
  `student_code` varchar(20) DEFAULT NULL,
  `department` varchar(200) DEFAULT NULL,
  `course` varchar(50) DEFAULT NULL,
  `card_issued_date` date NOT NULL,
  `card_expiry_date` date NOT NULL,
  `max_borrow_limit` int(11) DEFAULT 5,
  `status` enum('ACTIVE','SUSPENDED','EXPIRED') DEFAULT 'ACTIVE',
  `total_borrowed` int(11) DEFAULT 0,
  `current_debt` decimal(12,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `card_number` (`card_number`),
  UNIQUE KEY `student_code` (`student_code`),
  CONSTRAINT `1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.members: ~3 rows (approximately)
INSERT INTO `members` (`id`, `user_id`, `card_number`, `student_code`, `department`, `course`, `card_issued_date`, `card_expiry_date`, `max_borrow_limit`, `status`, `total_borrowed`, `current_debt`) VALUES
	(1, 1, 'LIB-2026-001', 'SV2101001', 'Công nghệ thông tin', 'K64', '2024-10-01', '2028-10-01', 5, 'ACTIVE', 3, 0.00),
	(2, 3, 'CARD-1778110194051', NULL, NULL, NULL, '2026-05-07', '2030-05-07', 5, 'ACTIVE', 0, 0.00),
	(3, 4, 'CARD-1778110619381', NULL, NULL, NULL, '2026-05-07', '2030-05-07', 5, 'ACTIVE', 0, 0.00),
	(7, 5, 'SV-2026-5', 'lenhi', NULL, NULL, '2026-05-07', '2030-05-07', 5, 'ACTIVE', 0, 0.00),
	(8, 7, 'SV-2026-7', 'thanhtam12', NULL, NULL, '2026-05-24', '2030-05-24', 5, 'ACTIVE', 0, 0.00);

-- Dumping structure for table library_db.payments
CREATE TABLE IF NOT EXISTS `payments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NOT NULL,
  `fine_id` bigint(20) DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `payment_type` enum('FINE','DEPOSIT','DEPOSIT_REFUND') NOT NULL,
  `payment_method` enum('CASH','TRANSFER') DEFAULT 'CASH',
  `note` text DEFAULT NULL,
  `processed_by` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  KEY `fine_id` (`fine_id`),
  KEY `processed_by` (`processed_by`),
  CONSTRAINT `1` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`fine_id`) REFERENCES `fines` (`id`),
  CONSTRAINT `3` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.payments: ~0 rows (approximately)

-- Dumping structure for table library_db.publishers
CREATE TABLE IF NOT EXISTS `publishers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `address` text DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `website` varchar(200) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.publishers: ~9 rows (approximately)
INSERT INTO `publishers` (`id`, `name`, `address`, `phone`, `email`, `website`, `created_at`) VALUES
	(1, 'NXB Trẻ', '161B Lý Chính Thắng, Q3, TP.HCM', '02839316289', 'hopthu@nxbtre.com.vn', 'www.nxbtre.com.vn', '2026-05-17 03:27:30'),
	(2, 'NXB Giáo Dục Việt Nam', '81 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', '02438220801', 'lienhe@nxbgianduc.vn', 'www.nxbgd.vn', '2026-05-17 03:27:30'),
	(3, 'NXB Kim Đồng', '55 Quang Trung, Hai Bà Trưng, Hà Nội', '02439434730', 'info@nxbkimdong.com.vn', 'www.nxbkimdong.com.vn', '2026-05-17 03:27:30'),
	(4, 'NXB Thông Tin Và Truyền Thông', '115 Trần Duy Hưng, Cầu Giấy, Hà Nội', '02435563875', 'nxb.tttt@mic.gov.vn', 'www.nxbthongtintruyenthong.vn', '2026-05-17 03:27:30'),
	(5, 'NXB Thế Giới', '46 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', '02438253841', 'thegioi@hn.vnn.vn', 'www.nxbthegioi.vn', '2026-05-17 03:27:30'),
	(6, 'NXB Hội Nhà Văn', '65 Nguyễn Du, Hai Bà Trưng, Hà Nội', '02438222135', 'nxbhoinhavan@gmail.com', 'www.nxbhoinhavan.vn', '2026-05-17 03:27:30'),
	(7, 'NXB Chính Trị Quốc Gia Sự Thật', '6/86 Duy Tân, Cầu Giấy, Hà Nội', '02438221581', 'suthat@nxbctqg.vn', 'www.nxbctqg.org.vn', '2026-05-17 03:27:30'),
	(8, 'NXB Tổng Hợp TP.HCM', '62 Nguyễn Thị Minh Khai, Q1, TP.HCM', '02838225340', 'tonghop@nxbhcm.com.vn', 'www.nxbhcm.com.vn', '2026-05-17 03:27:30'),
	(9, 'NXB Lao Động', '175 Tây Sơn, Đống Đa, Hà Nội', '02438515380', 'nxblaodong@yahoo.com', 'www.nxblaodong.com.vn', '2026-05-17 03:27:30');

-- Dumping structure for table library_db.reservation
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reservation_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `note` text DEFAULT NULL,
  `notified_date` datetime DEFAULT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_id` (`member_id`,`book_id`,`status`),
  KEY `FKs25sh1gv4uidcd1c1qjux3af2` (`book_id`),
  CONSTRAINT `FK26jrwg2m27qtlfkvfi82yie4o` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `FKs25sh1gv4uidcd1c1qjux3af2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.reservation: ~1 rows (approximately)
INSERT INTO `reservation` (`id`, `reservation_date`, `status`, `note`, `notified_date`, `book_id`, `member_id`) VALUES
	(1, '2026-05-24 02:18:12.150319', 'PENDING', NULL, NULL, 8, 7),
	(5, '2026-05-26 09:53:36.425749', 'PENDING', NULL, NULL, 8, 8);

-- Dumping structure for table library_db.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` enum('ROLE_ADMIN','ROLE_LIBRARIAN','ROLE_STUDENT') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.roles: ~3 rows (approximately)
INSERT INTO `roles` (`id`, `name`) VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_LIBRARIAN'),
	(3, 'ROLE_STUDENT');

-- Dumping structure for table library_db.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.user_roles: ~6 rows (approximately)
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 3),
	(5, 3),
	(6, 3),
	(7, 3);

-- Dumping structure for table library_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(200) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `avatar` varchar(500) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `is_approved` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table library_db.users: ~7 rows (approximately)
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `avatar`, `is_active`, `created_at`, `is_approved`) VALUES
	(1, 'admin', '$2a$10$Uh/ekBNbRLLESnLzSB1jueGDD/imG7r5qyqJmiQ6htZJIAM4IAof2', 'Quản trị viên Nhi', 'nhi@university.edu.vn', '0912345678', NULL, 1, '2026-05-04 02:17:19', b'1'),
	(2, 'thuthu01', '123456', 'Nguyễn Văn A', 'vana@university.edu.vn', '0987654321', NULL, 1, '2026-05-04 02:17:19', b'1'),
	(3, 'thanhtam', '$2a$10$K7sxrw4GxquraxsAceHtCuaED.wsAY.LE/nXt6jmHI4A.bQWhPHO2', 'Lưu Thị Thanh Tâm', 'tammaibong16042004@gmail.com', '0348155189', NULL, 1, '2026-05-06 23:29:53', b'1'),
	(4, 'thanhtam1', '$2a$10$cV5FJKhr5PdXhsOm1ra2m.AxH79zfLqs.pSogtHv8T3bo6tGOyb3u', 'Lưu Thị Thanh Tâm', 'tammaibong160420045@gmail.com', '0348155188', NULL, 1, '2026-05-06 23:36:59', b'1'),
	(5, 'lenhi', '$2a$10$sGzQfG18hBG7rxhEb4yWw.lEliWD5B9gWjkGgubAZuqnIdSSIa8o.', 'Lê Thị Yến Nhi', 'Nhi123@gmail.com', '0434874433', NULL, 1, '2026-05-07 02:35:16', b'1'),
	(6, 'lenhi1', '$2a$10$7bvy..2hdUmpvXBaNiViGu7wG.XZL7CTbYXa4SWIvAI5sC6fwOcXS', 'Lê Thị Yến Nhi', 'Nhi1234@gmail.com', '0434874436', NULL, 1, '2026-05-07 04:44:53', b'0'),
	(7, 'thanhtam12', '$2a$10$3L/n4DtYy0BOnoqnk21DluWM/y9w/.Nm41Pff8UU72qk/DQHx6C.O', 'Lưu Thị Thanh Tâm', 'tammaibong10420045@gmail.com', '0348155188', NULL, 1, '2026-05-24 16:43:38', b'1');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
