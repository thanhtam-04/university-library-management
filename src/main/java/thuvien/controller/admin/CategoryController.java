package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Category;
import thuvien.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ─── DANH SÁCH ───────────────────────────────────────────
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", categoryService.findAll());
        model.addAttribute("activePage", "category");
        return "views/admin/category/list";
    }

    // ─── FORM THÊM MỚI ───────────────────────────────────────
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("parentList", categoryService.findAll()); // danh mục cha
        model.addAttribute("activePage", "category");
        model.addAttribute("formTitle", "Thêm danh mục mới");
        model.addAttribute("isEdit", false);
        return "views/admin/category/add";
    }

    // ─── LƯU (THÊM MỚI) ──────────────────────────────────────
    @PostMapping("/save")
    public String save(@ModelAttribute("category") Category category,
                       @RequestParam(value = "parentId", required = false) Long parentId,
                       RedirectAttributes ra) {
        // Kiểm tra tên trùng khi thêm mới
        if (category.getId() == null && categoryService.existsByName(category.getName().trim())) {
            ra.addFlashAttribute("errorMsg",
                "Tên danh mục \"" + category.getName() + "\" đã tồn tại!");
            return "redirect:/admin/category/add";
        }

        // Gán danh mục cha nếu có chọn
        if (parentId != null) {
            Category parent = categoryService.findById(parentId);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        categoryService.save(category);
        ra.addFlashAttribute("successMsg", "Thêm danh mục thành công!");
        return "redirect:/admin/category/list";
    }

    // ─── FORM CHỈNH SỬA ──────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);

        // Loại chính nó ra khỏi danh sách cha (tránh vòng lặp)
        List<Category> parentList = categoryService.findAll()
                .stream()
                .filter(c -> !c.getId().equals(id))
                .toList();

        model.addAttribute("category", category);
        model.addAttribute("parentList", parentList);
        model.addAttribute("activePage", "category");
        model.addAttribute("formTitle", "Chỉnh sửa danh mục");
        model.addAttribute("isEdit", true);
        return "views/admin/category/edit";
    }

    // ─── CẬP NHẬT ────────────────────────────────────────────
    @PostMapping("/update")
    public String update(@ModelAttribute("category") Category category,
                         @RequestParam(value = "parentId", required = false) Long parentId,
                         RedirectAttributes ra) {
        // Gán danh mục cha nếu có chọn
        if (parentId != null) {
            Category parent = categoryService.findById(parentId);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        categoryService.save(category);
        ra.addFlashAttribute("successMsg", "Cập nhật danh mục thành công!");
        return "redirect:/admin/category/list";
    }

    // ─── XÓA ─────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            categoryService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg",
                "Không thể xóa! Danh mục đang được sử dụng bởi sách hoặc có danh mục con.");
        }
        return "redirect:/admin/category/list";
    }
}