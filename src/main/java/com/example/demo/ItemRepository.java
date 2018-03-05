package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository  extends CrudRepository<AppItem,Long> {

    Iterable<AppItem> findAllByItemTitleContainsAndItemStatusOrItemCategoryAndItemStatus(String title, String status2, String catagory, String status);
    //AppUser findAllById(long id);
    //List<AppUser> findByUsername(String userName);
    Iterable<AppItem> findAllByItemPoster(AppItem appItemPoster);
    Iterable<AppItem> findAllByItemStatus(String status);
    Iterable<AppItem> findAllByItemStatusAndItemPoster(String status, AppUser user);

}
