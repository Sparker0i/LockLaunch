package me.sparker0i.question.model;

public class Category {
    private String name;
    private boolean isSelected;

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name , boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
