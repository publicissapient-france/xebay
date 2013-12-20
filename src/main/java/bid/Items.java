package bid;

class Items {
    private final Item[] items;
    private int currentItemIndex;

    Items(Item... items) {
        if (items.length == 0) {
            throw new BidException();
        }
        this.items = items;
        this.currentItemIndex = -1;
    }

    Item next() {
        currentItemIndex++;
        if (currentItemIndex >= items.length) {
            currentItemIndex = 0;
        }
        return items[currentItemIndex];
    }
}
