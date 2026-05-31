package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.User;
import thuvien.entity.Member;
import thuvien.repository.RoleRepository;
import thuvien.repository.MemberRepository;
import thuvien.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository; // Sử dụng để đồng bộ thông tin bạn đọc

    /**
     * 1. Trang danh sách tài khoản
     * URL: http://localhost:8080/admin/user/list
     */
    @GetMapping("/list")
    public String listUsers(Model model) {
        // Đảm bảo findAll() lấy toàn bộ record trong bảng User
        List<User> users = userService.findAll(); 
        model.addAttribute("users", users);
        return "views/admin/user/list";
    }

    /**
     * 2. Trang thêm mới tài khoản
     * URL: http://localhost:8080/admin/user/add
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "views/admin/user/add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, 
                          @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                          RedirectAttributes ra) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                ra.addFlashAttribute("errorMsg", "Tên đăng nhập đã tồn tại!");
                return "redirect:/admin/user/add";
            }
            if (userService.existsByEmail(user.getEmail())) {
                ra.addFlashAttribute("errorMsg", "Email đã được sử dụng!");
                return "redirect:/admin/user/add";
            }

            if (roleIds != null) {
                user.setRoles(new HashSet<>(roleRepository.findAllById(roleIds)));
            }

            userService.save(user);
            ra.addFlashAttribute("successMsg", "Thêm tài khoản mới thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/user/list";
    }

    /**
     * 3. Trang chỉnh sửa tài khoản
     * URL: http://localhost:8080/admin/user/edit/{id}
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "views/admin/user/edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                             RedirectAttributes ra) {
        try {
            if (roleIds != null) {
                user.setRoles(new HashSet<>(roleRepository.findAllById(roleIds)));
            } else {
                user.setRoles(new HashSet<>());
            }

            userService.update(user);
            ra.addFlashAttribute("successMsg", "Cập nhật tài khoản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/user/list";
    }

    /**
     * 4. Xóa tài khoản
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa tài khoản thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa tài khoản này.");
        }
        return "redirect:/admin/user/list";
    }

    /**
     * 5. Duyệt nhanh trực tiếp từ dropdown danh sách thành viên
     * URL: POST http://localhost:8080/admin/user/approve-quick/{id}
     */
    /**
     * Duyệt nhanh hoặc Khóa tài khoản trực tiếp từ danh sách
     * URL: POST http://localhost:8080/admin/user/approve-quick/{id}
     */
    @PostMapping("/approve-quick/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveQuick(@PathVariable Long id, 
                               @RequestParam boolean approve, 
                               RedirectAttributes ra) {
        try {
            // 1. Cập nhật trạng thái duyệt của User trong bảng users
            userService.approveUser(id, approve);
            User user = userService.findById(id);

            if (approve) {
                // ── TRƯỜNG HỢP: ĐỒNG Ý DUYỆT ──
                boolean memberExists = memberRepository.existsByUserEmail(user.getEmail());
                
                if (!memberExists) {
                    Member newMember = new Member();
                    newMember.setUser(user);
                    newMember.setFullName(user.getFullName());
                    newMember.setStudentCode(user.getUsername());
                    
                    String studentCode = "SV-" + LocalDate.now().getYear() + "-" + user.getId();
                    newMember.setCardNumber(studentCode); 
                    newMember.setCardIssuedDate(LocalDate.now()); 
                    newMember.setExpiryDate(LocalDate.now().plusYears(4)); 
                    newMember.setStatus(Member.Status.ACTIVE); // Trạng thái hoạt động
                    
                    memberRepository.save(newMember);
                } else {
                    // Nếu đã tồn tại member nhưng status đang INACTIVE thì cho quay lại ACTIVE
                    Optional<Member> memberOpt = memberRepository.findByUserEmail(user.getEmail());
                    memberOpt.ifPresent(m -> {
                        m.setStatus(Member.Status.ACTIVE);
                        memberRepository.save(m);
                    });
                }
                ra.addFlashAttribute("successMsg", "Đã duyệt tài khoản '" + user.getFullName() + "' thành công!");
            } else {
                // ── TRƯỜNG HỢP: HỦY DUYỆT / KHÓA TÀI KHOẢN ──
                
                // 1. Khóa đăng nhập của User
                user.setIsActive(false); 
                userService.update(user);

                // 2. VÔ HIỆU HÓA THẺ THÀNH VIÊN (Thay vì xóa, tránh lỗi khóa ngoại)
                Optional<Member> memberOpt = memberRepository.findByUserEmail(user.getEmail());
                if (memberOpt.isPresent()) {
                    Member member = memberOpt.get();
                    member.setIsActive(false);
                    memberRepository.save(member);
                    ra.addFlashAttribute("successMsg", "Đã khóa tài khoản và vô hiệu hóa thẻ thành viên.");
                } else {
                    ra.addFlashAttribute("successMsg", "Đã khóa tài khoản thành công.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
            ra.addFlashAttribute("errorMsg", "Lỗi xử lý: " + e.getMessage());
        }
        return "redirect:/admin/user/list";
    }
}