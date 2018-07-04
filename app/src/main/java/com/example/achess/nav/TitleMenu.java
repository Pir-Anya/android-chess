package com.example.achess.nav;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by anna
 */

public class TitleMenu extends ExpandableGroup<SubTitle> {

    private String imageUrl;

    public TitleMenu(String title, List<SubTitle> items, String imageUrl) {
        super(title, items);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

