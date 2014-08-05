package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.Amount;
import fr.xebia.xebay.domain.BidException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static fr.xebia.xebay.domain.internal.Item.BANK;

public class Items {
    private final Item[] items;
    private int currentItemIndex;

    public Items(Item... items) {
        if (items.length == 0) {
            throw new BidException();
        }
        this.items = items;
        this.currentItemIndex = items.length - 1;
    }

    public Item find(String name) {
        try {
            return get(name);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Item next() {
        return next(currentItemIndex + 1);
    }

    private Item next(int currentItemIndex) {
        if (currentItemIndex >= items.length) {
            currentItemIndex = 0;
        }
        if (items[currentItemIndex].getOwner() == BANK) {
            this.currentItemIndex = currentItemIndex;
            return items[currentItemIndex];
        }
        if (currentItemIndex == this.currentItemIndex) {
            return null;
        }
        return next(currentItemIndex + 1);
    }

    public Item get(String name) {
        return stream()
                .filter((item) -> item.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException());
    }

    public static Optional<Items> load(String resource) {
        Set<Item> items = new LinkedHashSet<>();
        try (BufferedReader csv = new BufferedReader(new InputStreamReader(Items.class.getResourceAsStream(resource)))) {
            Pattern columnPattern = Pattern.compile("(?:\"(?<withQuotes>[^\"]+)\"|(?<withoutQuote>[^\"^,]+)),?");
            String currentLine;
            while ((currentLine = csv.readLine()) != null) {
                Matcher matcher = columnPattern.matcher(currentLine);

                if (matcher.find()) {
                    String category = matcher.group("withoutQuote") != null ? matcher.group("withoutQuote") : matcher.group("withQuotes");
                    if (matcher.find()) {
                        String name = matcher.group("withoutQuote") != null ? matcher.group("withoutQuote") : matcher.group("withQuotes");

                        if (matcher.find()) {
                            items.add(new Item(category.trim(), name.trim(), new Amount(Double.valueOf(matcher.group("withoutQuote").trim()))));
                        }
                    }
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(new Items(items.toArray(new Item[items.size()])));
    }

    public void userIsUnregistered(User user) {
        stream().filter((item) -> user.equals(item.getOwner()))
                .forEach((item) -> item.backToBank());
    }

    public Stream<Item> stream() {
        return Arrays.stream(items);
    }
}
