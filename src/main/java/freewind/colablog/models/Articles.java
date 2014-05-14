package freewind.colablog.models;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class Articles {

    private ObservableList<Article> articles = FXCollections.observableArrayList();

    public void add(Article article) {
        this.articles.add(article);
    }

    public void addListener(ListChangeListener<Article> listener) {
        articles.addListener(listener);
    }

}
