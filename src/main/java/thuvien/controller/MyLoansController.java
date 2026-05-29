package thuvien.controller;

import lombok.RequiredArgsConstructor;
import thuvien.entity.Reservation;
import thuvien.repository.ReservationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import thuvien.entity.Loan;
import thuvien.entity.Member;
import thuvien.entity.User;
import thuvien.repository.MemberRepository;
import thuvien.service.LoanService;
import thuvien.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MyLoansController {

    private final UserService           userService;
    private final LoanService           loanService;
    private final MemberRepository      memberRepository;
    private final ReservationRepository reservationRepository;

    /* ─── GET /my-loans: Giao diện quản lý cá nhân độc giả ─── */
    @GetMapping("/my-loans")
    public String myLoans(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        model.addAttribute("currentUser", user);

        Member member = memberRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("member", member);

        if (member == null) {
            model.addAttribute("loans",        Collections.emptyList());
            model.addAttribute("activeLoans",  Collections.emptyList());
            model.addAttribute("reservations", Collections.emptyList());
            model.addAttribute("overdueCount", 0L);
            return "my-loans";
        }

        // 1. Lấy và lọc danh sách đặt sách (Reservations) - Lọc trùng theo ID sách
        List<Reservation> allReservations = reservationRepository.findByMemberId(member.getId());
        List<Reservation> uniqueReservations = (allReservations != null) 
            ? allReservations.stream()
                .filter(distinctByKey(r -> r.getBook().getId()))
                .collect(Collectors.toList())
            : Collections.emptyList();
        model.addAttribute("reservations", uniqueReservations);

        // 2. Lấy danh sách đơn mượn (Loans)
     // 2. Lấy danh sách đơn mượn (Loans)
        List<Loan> allLoans = loanService.findByMember(member.getId());
        model.addAttribute("loans", (allLoans != null) ? allLoans : Collections.emptyList());

        // --- THÊM ĐOẠN NÀY ĐỂ TÍNH TOÁN TRẠNG THÁI ---
        Map<Long, String> loanStatusMap = new HashMap<>();
        if (allLoans != null) {
            java.time.LocalDate today = java.time.LocalDate.now();
            for (Loan l : allLoans) {
                String status;
                if (l.getStatus() == Loan.Status.RETURNED) {
                    status = "RETURNED";
                } else if (l.getDueDate() != null && l.getDueDate().isBefore(today)) {
                    status = "OVERDUE";
                } else if (l.getStatus() == Loan.Status.OVERDUE) {
                    status = "OVERDUE";
                } else {
                    status = "ACTIVE";
                }
                loanStatusMap.put(l.getId(), status);
            }
        }
        model.addAttribute("loanStatusMap", loanStatusMap);
        // ----------------------------------------------
        // 3. Tính toán Active Loans
        List<Loan> activeLoans = (allLoans != null) ? allLoans.stream()
                .filter(l -> l.getStatus() == Loan.Status.ACTIVE
                          || l.getStatus() == Loan.Status.OVERDUE
                          || l.getStatus() == Loan.Status.PENDING)
                .toList() : Collections.emptyList();
        model.addAttribute("activeLoans", activeLoans);

        // 4. Tính toán số lượng quá hạn
     // 4. Tính toán số lượng quá hạn (dùng loanStatusMap đã tính đúng)
        long overdueCount = loanStatusMap.values().stream()
                .filter(s -> s.equals("OVERDUE"))
                .count();
        model.addAttribute("overdueCount", overdueCount);
        return "my-loans";
    }

    /* ─── Helper: Lọc phần tử duy nhất trong Stream ─── */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /* ─── Helper: Lấy thông tin tài khoản hiện tại ─── */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return null;
        try { return userService.findByUsername(auth.getName()); }
        catch (Exception e) { return null; }
    }
}