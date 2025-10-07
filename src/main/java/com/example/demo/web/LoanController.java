package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ItemViewModel;
import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.ItemStatus;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ItemService;
import com.example.demo.service.LoanServiceImpl;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/loan")
public class LoanController {
	
	private final LoanServiceImpl loanService;
	private final ItemService itemService;
	private final UserService userService;
	private final CategoryService categoryService;
	
	public LoanController(LoanServiceImpl loanService, ItemService itemService, 
            UserService userService, CategoryService categoryService) { // <-- MODIFIED
			this.loanService = loanService;
			this.itemService = itemService;
			this.userService = userService;
			this.categoryService = categoryService; // <-- NEW
	}
	
	@GetMapping
	public String showItemToLoan(Model model, @AuthenticationPrincipal User user,
			@RequestParam(name = "categoryId", required = false) Long categoryId) {
		
		 List<Category> categories = categoryService.getAll();
	        model.addAttribute("categories", categories);
	        model.addAttribute("selectedCategoryId", categoryId); // ส่ง ID ที่เลือกกลับไป

	        List<Item> items = itemService.findItemsForLoanPage(categoryId);
			
		//List<Item> items = itemService.getAll();
		List<Loan> userLoans = userService.getUserLoans(user.getId());
		
		Map<Long, Loan> myLoanMap = userLoans.stream()
                .collect(Collectors.toMap(loan -> loan.getItem().getId(), loan -> loan));
		
		List<ItemViewModel> viewModels = new ArrayList<>();
		
		for (Item item : items) {
            ItemViewModel vm = new ItemViewModel();
            
            vm.setId(item.getId());
            vm.setAssetCode(item.getAssetCode());
            vm.setEquipName(item.getEquip().getEquipName());
            vm.setStatusDisplayName(item.getStatus().getDisplayName());
            
            Loan myLoanForItem = myLoanMap.get(item.getId());
            
            if (item.getStatus() == ItemStatus.Available) { // <-- **แก้ไขเป็นแบบนี้**
                vm.setCanBorrow(true);
            } else if (myLoanForItem != null) { 
                vm.setLoanId(myLoanForItem.getId());
                if (myLoanForItem.getPendingReturnAt() == null) {
                    vm.setCanRequestReturn(true); 
                } else {
                    vm.setPendingReturn(true); 
                }
            } else {
                vm.setLoanedByOther(true);
            }

            viewModels.add(vm); // เพิ่ม ViewModel ที่ประมวลผลเสร็จแล้วลง List
        }
		
		model.addAttribute("items", viewModels);
		return "equipLoan";
	}
	
    @PostMapping("/store")
    public String createLoan(@RequestParam("item_id") Long itemId,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            loanService.createLoan(itemId, user);
            redirectAttributes.addFlashAttribute("successMessage", "ยืมอุปกรณ์เรียบร้อยแล้ว");
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/loan"; // กลับไปหน้าเดิม
    }
    
    @PostMapping("/requestReturn/{id}")
    public String requestReturn(@PathVariable("id")Long loanId,
    		@RequestParam("return_photo")MultipartFile returnPhoto,
    		RedirectAttributes redirectAttributes) {
    	
    	if(returnPhoto.isEmpty()) {
    		redirectAttributes.addFlashAttribute("errorMessage","กรุณาแนบไฟล์รูปภาพ");
    		return "redirect:/loan";
    	}
    	
    	try {
    		loanService.requestReturn(loanId, returnPhoto);
    		redirectAttributes.addFlashAttribute("successMessage",  "แจ้งคืนอุปกรณ์เรียบร้อยแล้ว รอกรรมการตรวจสอบ");
    	}catch(Exception e) {
    		redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาดในการแจ้งคืน: " + e.getMessage());
    	}
    	return "redirect:/loan";
    }
}
