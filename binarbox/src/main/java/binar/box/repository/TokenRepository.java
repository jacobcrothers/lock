package binar.box.repository;

import binar.box.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Timis Nicu Alexandru on 23-Mar-18.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findOneByToken(String token);

}