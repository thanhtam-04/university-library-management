package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thuvien.entity.Fine;
import thuvien.entity.Loan;
import thuvien.entity.Member;
import thuvien.service.MemberService;
import thuvien.service.UserService;
import java.util.List;
@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserService   userService;
    private final thuvien.repository.MemberRepository memberRepository;
    private final thuvien.repository.LoanRepository loanRepository;
    private final thuvien.repository.FineRepository fineRepository;
    /* ── LIST ── */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("members",  memberService.findAll());
        model.addAttribute("activePage", "member");
        return "views/admin/member/list";
    }

    /* ── ADD FORM ── */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("member",   new Member());
        model.addAttribute("users",    userService.findUsersWithoutMember()); // chỉ user chưa có thẻ
        model.addAttribute("activePage", "member");
        return "views/admin/member/add";
    }

    /* ── SAVE (POST) ── */
    @PostMapping("/save")
    public String save(@ModelAttribute("member") Member member,
                       @RequestParam("userId") Long userId,
                       RedirectAttributes ra) {
        try {
            memberService.saveWithUser(member, userId);
            ra.addFlashAttribute("successMsg", "Thêm thành viên mới thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
            return "redirect:/admin/member/add";
        }
        return "redirect:/admin/member/list";
    }

    /* ── EDIT FORM ── */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member",   member);
        model.addAttribute("activePage", "member");
        return "views/admin/member/edit";
    }

    /* ── UPDATE (POST) ── */
    @PostMapping("/update")
    public String update(@ModelAttribute("member") Member member,
                         RedirectAttributes ra) {
        try {
            memberService.update(member);
            ra.addFlashAttribute("successMsg", "Cập nhật thành viên thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/member/list";
    }

    /* ── DELETE ── */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            memberService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa thành viên!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg",
                "Không thể xóa thành viên này (có thể đang có lịch sử mượn sách).");
        }
        return "redirect:/admin/member/list";
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));
        
        // Lấy danh sách phiếu mượn của thành viên này
        List<Loan> loanHistory = loanRepository.findByMemberId(id);
        // Lấy danh sách phí phạt (nếu bạn muốn hiển thị)
        List<Fine> fines = fineRepository.findByMemberId(id);
        
        model.addAttribute("m", member);
        model.addAttribute("loans", loanHistory);
        model.addAttribute("fines", fines);
        model.addAttribute("activePage", "member");
        return "views/admin/member/profile";
    }
}