package com.miesitu.web_project.controller;


import com.miesitu.web_project.services.UserService;

import java.util.List;

import com.miesitu.web_project.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AdminController {
    @Autowired
    private UserService userSaveService;

    @GetMapping("/admin")
    public String  showUserList(Model model) {
        // Iterable<com.miesitu.web_project.entity.User> userLists  = userSaveService.listAll();
        // model.addAttribute("userLists", userLists);
        // return "user";
        return findPagenated(1, model);

    }

    @GetMapping("/admin/newUser")
    public String  showUserForm(Model model) {
        model.addAttribute("user1", new User());
        model.addAttribute("pageTitle", "Add New User");
        return "userForm";

    }

    @PostMapping("/admin/saveUser")
    public String  saveUser(User user1, RedirectAttributes ra) {
       userSaveService.saveUser(user1);
       ra.addFlashAttribute("message", "User has been saved succesfully.");
        return "redirect:/admin";

    }

    @GetMapping("/admin/editUser/{userId}")
    public String  showEditForm(@PathVariable("userId") long userId, Model model, RedirectAttributes ra) {
        try {
        User user = userSaveService.get(userId);
        model.addAttribute("user1", user);
        model.addAttribute("pageTitle","Edit User(Id:" + user.getUserId() + ")");
        return "userForm";

    } catch (UsernameNotFoundException e) {
        ra.addFlashAttribute("message", e.getMessage());
        return "redirect:/admin";
    }

    }


    @GetMapping("/admin/deleteUser/{userId}")
    public String  deleteUser(@PathVariable("userId") long userId,RedirectAttributes ra) {
        try {
        userSaveService.delete(userId);
        ra.addFlashAttribute("message","The user ID:"+userId+"has been deleted.");

    } catch (UsernameNotFoundException e) {
        ra.addFlashAttribute("message", e.getMessage());
    }
    return "redirect:/admin";

    }  

    @GetMapping("/page/{pageNo}")
    public String  findPagenated(@PathVariable(value="pageNo") int pageNo, Model model ) {
        int pageSize = 2;
        Page<User> page = userSaveService.findPaginated(pageNo,pageSize);
        List<User> userLists = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("userLists", userLists);
        return "user";

    }
    
}

