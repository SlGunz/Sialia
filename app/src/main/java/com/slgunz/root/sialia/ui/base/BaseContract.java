package com.slgunz.root.sialia.ui.base;


import com.slgunz.root.sialia.data.model.Tweet;

public interface BaseContract {

    interface Presenter {
        void subscribe(BaseFragment view);

        void unsubscribe();

        void selectItem(Tweet tweet);

        Tweet currentItem();
    }
}
