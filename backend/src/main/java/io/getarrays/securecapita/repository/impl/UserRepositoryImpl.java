package io.getarrays.securecapita.repository.impl;

import io.getarrays.securecapita.entity.Role;
import io.getarrays.securecapita.entity.User;
import io.getarrays.securecapita.enums.RoleType;
import io.getarrays.securecapita.enums.VerificationType;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {

    private static final String COUNT_USER_EMAIL_QUERY = "";
    private static final String INSERT_USER_QUERY = "";

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository<Role> roleRoleRepository;
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public User create(User user) {

        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("Email already in use.Please use a different email address and try again");
        //save new user

        try {

            KeyHolder holder = new GeneratedKeyHolder();

            SqlParameterSource source = getSqlParameterSource(user);

            jdbc.update(INSERT_USER_QUERY, source, holder);

            user.setId(requireNotNull(holder.getKey()).longvalue());


            //Add role to the user
            roleRoleRepository.addRoleToUser(user.getId(), RoleType.ROLE_USER.name());

            //Send Verification Url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), VerificationType.ACCOUNT.getType());


        } catch (EmptyResultDataAccessException e) {
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Collection list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(long id) {
        return null;
    }

    private Integer getEmailCount(String email) {

        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {

        return new MapSqlParameterSource()
                .addValue("fistName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("password", bCryptPasswordEncoder.encode(user.getPassword()))
                .addValue("email", user.getEmail());
    }

    private String getVerificationUrl(String key, String type) {

        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }
}