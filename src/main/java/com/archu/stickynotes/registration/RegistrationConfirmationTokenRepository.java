package com.archu.stickynotes.registration;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationConfirmationTokenRepository extends MongoRepository<RegistrationConfirmationToken, String> {

    Optional<RegistrationConfirmationToken> findByRegistrationConfirmationToken(String registrationConfirmationToken);
}
