package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository  extends CrudRepository<com.example.demo.Item,Long> {

    Iterable<Item> findAllByItemTitleContains(String search);
    //AppUser findAllById(long id);
    //List<AppUser> findByUsername(String userName);
    Iterable<Item> findAllByItemPoster(Item itemPoster);
    Iterable<Item> findAllByItemStatus(String status);
    Iterable<Item> findAllByItemStatusAndItemPoster(String status, AppUser user);

}
