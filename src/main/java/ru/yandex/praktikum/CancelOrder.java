package ru.yandex.praktikum;

public class CancelOrder {
    public CancelOrder() {

    }

    public CancelOrder(long track) {
        this.track = track;
    }

    public long getTrack() {
        return track;
    }

    public void setTrack(long track) {
        this.track = track;
    }

    long track;
}
