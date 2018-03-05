package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    AppRoleRepository appRoleRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ItemRepository itemRepository;


    @Override
    public void run(String... strings) throws Exception {

        AppRole role = new AppRole();
        role.setRoleName("USER");
        appRoleRepository.save(role);

        role = new AppRole();
        role.setRoleName("ADMIN");
        appRoleRepository.save(role);

        // A few users
        // User 1
        AppUser user = new AppUser();
        user.setUsername("John");
        user.setPassword("password1");
        user.setFullName("John Doe");
        user.setUserEmail("g1@gmail.com");
        appUserRepository.save(user);
        user.addRole(appRoleRepository.findAppRoleByRoleName("USER"));
        appUserRepository.save(user);
        // User 2
        user = new AppUser();
        user.setUsername("Jacob");
        user.setPassword("password2");
        user.setFullName("Jacob Smith");
        user.setUserEmail("g2@gmail.com");
        appUserRepository.save(user);
        user.addRole(appRoleRepository.findAppRoleByRoleName("ADMIN"));
        appUserRepository.save(user);
        // User 3
        user = new AppUser();
        user.setUsername("Joe");
        user.setPassword("password3");
        user.setFullName("Joe Blow");
        user.setUserEmail("g3@gmail.com");
        appUserRepository.save(user);
        user.addRole(appRoleRepository.findAppRoleByRoleName("USER"));
        appUserRepository.save(user);
        // User 4
        user = new AppUser();
        user.setUsername("Jane");
        user.setPassword("password4");
        user.setFullName("Jane Pane");
        user.setUserEmail("g4@gmail.com");
        appUserRepository.save(user);
        user.addRole(appRoleRepository.findAppRoleByRoleName("USER"));
        appUserRepository.save(user);

        user = new AppUser();
        user.setUsername("Found Item");
        user.setPassword("test");
        appUserRepository.save(user);



        AppItem newAppItem = new AppItem();
        newAppItem.setItemTitle("Found Thing");
        newAppItem.setItemCategory("Other");
        newAppItem.setItemStatus("Found");
        newAppItem.setItemDescription("I found this thing");
        itemRepository.save(newAppItem);

        newAppItem = new AppItem();
        newAppItem.setItemTitle("Lost Jeans");
        newAppItem.setItemCategory("Clothes");
        newAppItem.setItemStatus("Lost");
        newAppItem.setItemDescription("I lost my Blue Jeans");
        itemRepository.save(newAppItem);

        newAppItem = new AppItem();
        newAppItem.setItemTitle("Lost Dog");
        newAppItem.setItemCategory("Pet");
        newAppItem.setItemStatus("Lost");
        newAppItem.setItemDescription("Lost Husky");
        itemRepository.save(newAppItem);




        AppItem appItem = itemRepository.findOne(new Long(1));
        appItem.addItemPoster(appUserRepository.findOne(new Long(1)));
        itemRepository.save(appItem);
        appItem = itemRepository.findOne(new Long(2));
        appItem.addItemPoster(appUserRepository.findOne(new Long(2)));
        itemRepository.save(appItem);
        appItem = itemRepository.findOne(new Long(3));
        appItem.addItemPoster(appUserRepository.findOne(new Long(1)));
        itemRepository.save(appItem);









    }
}
