package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.User;
import org.junit.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserInfoTest {
    @Test
    public void should_create_user_info_from_user() {
        User user = mock(User.class);
        when(user.getName()).thenReturn("user1");
        when(user.getBalance()).thenReturn(4.3);
        HashSet<Item> items = new HashSet<>();
        items.add(new Item("category1", "an item", 5.2));
        items.add(new Item("category2", "another item", 120.7));
        when(user.getItems()).thenReturn(items);

        UserInfo userInfo = UserInfo.newUserInfo(user);

        assertThat(userInfo.getName()).isEqualTo("user1");
        assertThat(userInfo.getBalance()).isEqualTo(4.3);
        assertThat(userInfo.getItems())
                .extracting("name", "category", "value")
                .containsExactly(
                        tuple("an item", "category1", 5.2),
                        tuple("another item", "category2", 120.7));
    }
}
