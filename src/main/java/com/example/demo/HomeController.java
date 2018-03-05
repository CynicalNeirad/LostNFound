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
        model.addAttribute("items", itemRepository.findAllByItemStatus("Lost"));
        return "mainPage";
    }

    @RequestMapping("/userpage")
    public String foundItems(Model model, Authentication authentication){
        AppUser user = appUserRepository.findAppUserByUsername(authentication.getName());
        model.addAttribute("items", itemRepository.findAllByItemStatusAndItemPoster("Found",user));
        return "mainPage";
    }

    @RequestMapping("/foundadminpage")
    public String allfoundItems(Model model){
        model.addAttribute("items", itemRepository.findAllByItemStatus("Found"));
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
        AppItem appItem = new AppItem();
        itemRepository.save(appItem);
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("item", appItem);
        return "addItemPage";
    }

    @PostMapping("/processitem")
    public String processItem(@Valid @ModelAttribute("item") AppItem appItem, BindingResult result, Model model, Authentication authentication){
        if(result.hasErrors()){
            return "addItemPage";
        }
        else{
            appItem.setItemStatus("Lost");
            System.out.println(appItem.getItemPoster());
            for(AppUser appUser : appItem.getItemPoster()){
                AppUser userName = appUserRepository.findOne(appUser.getId());
                if (userName.getUsername().equals("Found Item")) {
                    appItem.setItemStatus("Found");
                }}
            if (appItem.getItemPoster().isEmpty()){
                appItem.addItemPoster(appUserRepository.findAppUserByUsername(authentication.getName())); }
            itemRepository.save(appItem);
            return "redirect:/";
        }
    }

    @PostMapping("/search")
    public String search(HttpServletRequest request, Model model){

        String searchString = request.getParameter("search");
        String catagroySearch = searchString;
        Iterable<AppItem> item = itemRepository.findAllByItemTitleContainsAndItemStatusOrItemCategoryAndItemStatus(searchString,"Lost", catagroySearch, "Lost");
        model.addAttribute("items", item);
        return "mainPage";
    }

    @RequestMapping("/swap/{id}")
    public String processCheckout(@PathVariable("id") long id, Model model) {
        AppItem appItem = itemRepository.findOne(id);
        if(appItem.getItemStatus().equals("Found")){
            appItem.setItemStatus("Lost");}
        else{
            appItem.setItemStatus("Found");}
        itemRepository.save(appItem);
        model.addAttribute("items", itemRepository.findAll());
        return "adminPage";
    }

}

