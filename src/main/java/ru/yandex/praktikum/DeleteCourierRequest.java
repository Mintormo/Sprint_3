package ru.yandex.praktikum;

public class DeleteCourierRequest {
    public String getId() {
        return id;
    }

    public DeleteCourierRequest() {

    }

    public DeleteCourierRequest(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
}
