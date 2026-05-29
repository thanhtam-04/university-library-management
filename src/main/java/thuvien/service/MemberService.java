package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thuvien.entity.Member;
import thuvien.entity.User;
import thuvien.repository.MemberRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService; // Thêm UserService để tìm User

    public List<Member> findAll() { return memberRepository.findAll(); }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên ID: " + id));
    }

    public void saveWithUser(Member member, Long userId) {
        // Tìm User dựa trên ID được chọn từ dropdown
        thuvien.entity.User user = userService.findById(userId);
        member.setUser(user);
        memberRepository.save(member);
    }

    public void update(Member member) {
        // Giữ nguyên User cũ khi cập nhật để tránh lỗi Null User
        Member existing = findById(member.getId());
        member.setUser(existing.getUser()); 
        memberRepository.save(member);
    }

    public void deleteById(Long id) { memberRepository.deleteById(id); }

	public Member findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public java.lang.reflect.Member findByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}