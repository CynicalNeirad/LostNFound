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
        model.addAttribute("items", itemRepository.findAllByItemStatus("Lost")); //The main page, which contains all lost items
        return "mainPage";
    }

    @RequestMapping("/userpage") //The userpage contains all found items by the current user
    public String foundItems(Model model, Authentication authentication){
        AppUser user = appUserRepository.findAppUserByUsername(authentication.getName());
        model.addAttribute("items", itemRepository.findAllByItemStatusAndItemPoster("Found",user));
        return "mainPage";
    }

    @RequestMapping("/foundadminpage") //The admin is able to see all found items and thus has a separate query for that
    public String allfoundItems(Model model){
        model.addAttribute("items", itemRepository.findAllByItemStatus("Found"));
        return "mainPage";
    }

    @GetMapping("/adminpage") //The admin page contains all items and several commands that only the admin has
    public String admintools(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "adminPage";
    }

    @RequestMapping("/appuserform")  //For registration and creation of a new user
    public String userRegistration(Model model){
        model.addAttribute("appuser",new AppUser());
        return "appuserform";
    }

    @RequestMapping(value="/appuserform",method= RequestMethod.POST) //Retrieves the user information from the html page and processes it into the repository
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

    @GetMapping("/additem") //Initalizes adding a new item to the database, also returns the users to allow for posting as another user
    public String addItem(Model model){
        AppItem appItem = new AppItem();
        itemRepository.save(appItem);
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("item", appItem);
        return "addItemPage";
    }

    @PostMapping("/processitem") //Ensures that the item has no errors, and handles extra item assignment, like username is non-chosen or status if the object was found
    public String processItem(@Valid @ModelAttribute("item") AppItem appItem, BindingResult result, Model model, Authentication authentication){
        if(result.hasErrors()){
            return "addItemPage";
        }
        else{
            appItem.setItemStatus("Lost");
            System.out.println(appItem.getItemPoster());
            for(AppUser appUser : appItem.getItemPoster()){ //Loops through the users to check for "user" Found Item, and upon finding it sets the item as a found item.
                AppUser userName = appUserRepository.findOne(appUser.getId());
                if (userName.getUsername().equals("Found Item")) {
                    appItem.setItemStatus("Found");
                }}
            if (appItem.getItemPoster().isEmpty()){  //As non-admin lack access to the user list on the add form, this assigns the item a user based on the current user
                appItem.addItemPoster(appUserRepository.findAppUserByUsername(authentication.getName())); }
            itemRepository.save(appItem);
            return "redirect:/";
        }
    }

    @PostMapping("/search") //My nav bar search, covers most cases but has some issues, need to figure out how to link the logic better (it's much too long)
    public String search(HttpServletRequest request, Model model, Authentication authentication){

        String searchString = request.getParameter("search");
        String catagroySearch = searchString;
        String titleSearch = searchString;
        Iterable<AppItem> item = itemRepository.findAllByItemTitleContainsAndItemStatusOrItemCategoryAndItemStatusOrItemPosterAndItemTitleContains(searchString,"Lost", catagroySearch, "Lost", appUserRepository.findAppUserByUsername(authentication.getName()), titleSearch);
        model.addAttribute("items", item);
        return "mainPage";
    }

    @RequestMapping("/swap/{id}") //This swaps the status of an item from lost to found via the admin page and then returns there (Find a way to save position on the page?)
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

