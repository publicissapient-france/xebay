package fr.xebia.xebay.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

public class Items {
    private final Item[] items;
    private int currentItemIndex;

    public Items(Item... items) {
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

    Item get(String name) {
        return stream(items)
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

                if (matcher.find() && matcher.find()) {
                    String name = matcher.group("withoutQuote") != null ? matcher.group("withoutQuote") : matcher.group("withQuotes");

                    if (matcher.find()) {
                        items.add(new Item(name.trim(), Double.valueOf(matcher.group("withoutQuote"))));
                    }
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(new Items(items.toArray(new Item[items.size()])));
    }
}
