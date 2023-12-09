package com.example.inkzone.service;

import com.example.inkzone.model.entity.Role;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.model.enums.RoleEnum;
import com.example.inkzone.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private final String EXISTING_EMAIL = "admin@example.com";
    private final String NON_EXISTING_EMAIL = "nonexisting@example.com";
    private ApplicationUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        toTest = new ApplicationUserDetailsService(mockUserRepository);
    }

    @Test
    void testUserFound(){
        Role testUserRole = new Role();
        testUserRole.setName(RoleEnum.USER);
        Role testModeratorRole = new Role();
        testModeratorRole.setName(RoleEnum.MODERATOR);
        Role testAdminRole = new Role();
        testAdminRole.setName(RoleEnum.ADMIN);
        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setEmail(EXISTING_EMAIL);
        testUserEntity.setPassword("password");
        testUserEntity.setRoles(Set.of(testUserRole, testModeratorRole, testAdminRole));

        when(mockUserRepository.findByEmail(EXISTING_EMAIL))
                .thenReturn(Optional.of(testUserEntity));

        UserDetails userDetails = toTest.loadUserByUsername(EXISTING_EMAIL);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(EXISTING_EMAIL, userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());
        Assertions.assertEquals(3, userDetails.getAuthorities().size());
        assertRole(userDetails.getAuthorities(), "ROLE_USER");
        assertRole(userDetails.getAuthorities(), "ROLE_MODERATOR");
        assertRole(userDetails.getAuthorities(), "ROLE_ADMIN");



    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities, String role){
        authorities
                .stream()
                .filter(a -> role.equals(a.getAuthority()))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("Role " + role + " not found!"));
    }

    @Test
    void testUserNotFound(){
        assertThrows(UsernameNotFoundException.class,
                () -> {
            toTest.loadUserByUsername(NON_EXISTING_EMAIL);

                });

    }
}
