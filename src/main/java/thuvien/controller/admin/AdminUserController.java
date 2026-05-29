package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
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
    @PostMapping("/approve-quick/{id}")
    public String approveQuick(@PathVariable Long id, 
                               @RequestParam boolean approve, 
                               RedirectAttributes ra) {
        try {
            // Cập nhật trạng thái duyệt của User thông qua service
            userService.approveUser(id, approve);
            User user = userService.findById(id);

            if (approve) {
                // ── TRƯỜNG HỢP: ĐỒNG Ý DUYỆT ──
                // Kiểm tra xem đã tồn tại Member liên kết với Email của User này chưa
                boolean memberExists = memberRepository.existsByUserEmail(user.getEmail());
                
                if (!memberExists) {
                    Member newMember = new Member();
                    newMember.setUser(user);
                    newMember.setFullName(user.getFullName());
                    newMember.setStudentCode(user.getUsername());
                    
                    // 1. Tạo mã thẻ
                    String studentCode = "SV-" + LocalDate.now().getYear() + "-" + user.getId();
                    newMember.setCardNumber(studentCode); 
                    
                    // 2. PHẢI CÓ DÒNG NÀY: Gán ngày cấp thẻ (không được để null)
                    newMember.setCardIssuedDate(LocalDate.now()); 
                    
                    // 3. Gán ngày hết hạn (Hàm này đã được sửa ở Bước 1)
                    newMember.setExpiryDate(LocalDate.now().plusYears(4)); 
                    
                    newMember.setStatus(Member.Status.ACTIVE);
                    memberRepository.save(newMember);
                }
                
                ra.addFlashAttribute("successMsg", "Đã duyệt tài khoản '" + user.getFullName() + "' và tự động cấp thẻ Bạn đọc thành công!");
            } else {
                // ── TRƯỜNG HỢP: KHÔNG DUYỆT / HỦY PHÊ DUYỆT ──
                
                // 1. Chuyển trạng thái tài khoản User sang "Không hoạt động" (Khóa đăng nhập)
                User userToLock = userService.findById(id); 
                userToLock.setIsActive(false); 
                userService.update(userToLock);

                // 2. Tìm và xóa hẳn bản ghi bên bảng Thành viên (Member)
                Optional<Member> memberOpt = memberRepository.findByUserEmail(userToLock.getEmail());
                if (memberOpt.isPresent()) {
                    memberRepository.delete(memberOpt.get());
                    ra.addFlashAttribute("successMsg", "Đã hủy duyệt: Tài khoản đã bị khóa và thẻ Thành viên đã được gỡ bỏ.");
                } else {
                    ra.addFlashAttribute("successMsg", "Đã hủy phê duyệt và khóa tài khoản thành công.");
                }
            }
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi xử lý duyệt: " + e.getMessage());
        }
        return "redirect:/admin/user/list";
    }
}