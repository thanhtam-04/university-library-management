package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Publisher;
import thuvien.service.PublisherService;

@Controller
@RequestMapping("/admin/publisher")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("activePage", "publisher");
        return "views/admin/publisher/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        model.addAttribute("activePage", "publisher");
        return "views/admin/publisher/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("publisher") Publisher publisher, RedirectAttributes ra) {
        publisherService.save(publisher);
        ra.addFlashAttribute("successMsg", "Lưu nhà xuất bản thành công!");
        return "redirect:/admin/publisher/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", publisherService.findById(id));
        model.addAttribute("activePage", "publisher");
        return "views/admin/publisher/edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            publisherService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa nhà xuất bản!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể xóa (Nhà xuất bản đang có sách liên kết).");
        }
        return "redirect:/admin/publisher/list";
    }
}