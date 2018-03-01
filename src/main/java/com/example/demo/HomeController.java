package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    AppRoleRepository appRoleRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @RequestMapping("/")
    public String showIndex(Model model){
        model.addAttribute("items", itemRepository.findAllByItemStatus("lost"));
        return "mainPage";

    }

    @RequestMapping("/userpage")
    public String productList(Model model, Authentication authentication){
        AppUser user = appUserRepository.findAppUserByUsername(authentication.getName());
        model.addAttribute("items", itemRepository.findAllByItemStatusAndItemPoster("found",user));
        return "mainPage";
    }

    @GetMapping("/adminpage")
    public String admintools(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "adminPage";
    }

    @RequestMapping("/appuserform")
    public String userRegistration(Model model){
        model.addAttribute("appuser",new AppUser());
        return "appuserform";
    }

    @RequestMapping(value="/appuserform",method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("appuser") AppUser appuser, BindingResult result, Model model){
        model.addAttribute("appuser",appuser);
        if(result.hasErrors()){
            return "appuserform";
        }else{
            model.addAttribute("message","User Account Successfully Created");
            appUserRepository.save(appuser);
        }
        return "redirect:/";
    }
    @GetMapping("/additem")
    public String addItem(Model model){
        com.example.demo.Item item = new Item();
        itemRepository.save(item);
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("item", item);
        return "addItemPage";
    }
    @PostMapping("/processitem")
    public String processItem(@Valid @ModelAttribute("product") com.example.demo.Item item, Model model, BindingResult result, Authentication authentication){
        if(result.hasErrors()){
            return "addItemPage";
        }
        else{
                item.setItemStatus("Lost");
                item.addItemPoster(appUserRepository.findAppUserByUsername(authentication.getName()));
                itemRepository.save(item);

            return "redirect:/";
        }
    }
    @PostMapping("/search")
    public String search(HttpServletRequest request, Model model){

        String searchString = request.getParameter("search");
        Iterable<Item> item = itemRepository.findAllByItemTitleContains(searchString);
        model.addAttribute("items", item);
        return "mainPage";
    }

    @RequestMapping("/swap/{id}")
    public String processCheckout(@PathVariable("id") long id, Model model) {
        Item item = itemRepository.findOne(id);
        if(item.getItemStatus().equals("Found")){item.setItemStatus("Lost");}
        else{item.setItemStatus("Found");}
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "adminPage";
    }

}

