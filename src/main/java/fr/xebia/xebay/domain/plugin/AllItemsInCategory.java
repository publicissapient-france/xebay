package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.xebia.xebay.domain.internal.Item.BANK;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public class AllItemsInCategory extends ActivablePlugin {
    public static final BigDecimal CREDIT_AMOUNT = new BigDecimal(500);

    protected AllItemsInCategory() {
        super("AllItemsInCategory", "Credit 500 to user having all items in category");
    }

    @Override
    protected void onActivation(Items items) {
        items.stream()
                .collect(Collectors.<Item, String, Set<User>>toMap(
                        item -> item.getCategory(),
                        item -> new HashSet<>(asList(item.getOwner())),
                        (existingUsers, newSetWithOneUser) -> {
                            Set<User> allUsers = new HashSet<>();
                            allUsers.addAll(existingUsers);
                            allUsers.addAll(newSetWithOneUser);
                            return allUsers;
                        }
                ))
                .forEach((String category, Set<User> users) -> {
                    if (users.size() != 1) {
                        return;
                    }
                    User userToCredit = users.iterator().next();
                    if (userToCredit == BANK) {
                        return;
                    }
                    userToCredit.credit(CREDIT_AMOUNT);
                });
    }

    @Override
    protected void onBidOfferResolvedIfActivated(BidOffer bidOffer, Items items) {
        User owner = bidOffer.item.getOwner();
        if (owner == BANK) {
            return;
        }
        Set<User> ownersOfSameCategoryAsItem = items.stream()
                .filter(item -> item.getCategory().equals(bidOffer.item.getCategory()))
                .map(item -> item.getOwner())
                .collect(toSet());
        if (hasAllItems(owner, ownersOfSameCategoryAsItem)) {
            owner.credit(CREDIT_AMOUNT);
        }
    }

    private boolean hasAllItems(User owner, Set<User> ownersOfSameCategoryAsBidOfferItem) {
        return ownersOfSameCategoryAsBidOfferItem.size() == 1
                && ownersOfSameCategoryAsBidOfferItem.iterator().next().equals(owner);
    }
}
