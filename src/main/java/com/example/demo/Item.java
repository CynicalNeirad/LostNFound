package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String itemTitle;

    private String itemDescription;

    private String itemCategory;

    private String itemImage;

    private String itemStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AppUser> itemPoster;

    public void addItemPoster(AppUser s){this.itemPoster.add(s);}

    public Item() {
        this.itemPoster=new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Set<AppUser> getItemPoster() {
        return itemPoster;
    }

    public void setItemPoster(Set<AppUser> itemPoster) {
        this.itemPoster = itemPoster;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemCatagory='" + itemCategory + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemPoster=" + itemPoster +
                '}';
    }
}

