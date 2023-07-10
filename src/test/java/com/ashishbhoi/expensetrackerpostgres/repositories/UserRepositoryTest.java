package com.ashishbhoi.expensetrackerpostgres.repositories;

import com.ashishbhoi.expensetrackerpostgres.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveStudent() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.test")
                .username("test")
                .password("test")
                .build();

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    public void findByEmail() {
        saveStudent();
        List<User> users = userRepository.findByEmailOrUsername("test@test.test", null);
        Assertions.assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void findByUsername() {
        saveStudent();
        List<User> users = userRepository.findByEmailOrUsername(null, "test");
        Assertions.assertThat(users.size()).isEqualTo(1);
    }
}