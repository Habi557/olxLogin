package com.olx.user.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.olx.user.entity.BlackListedTokensDocument;


public interface BlackListedTokens extends MongoRepository<BlackListedTokensDocument, Integer> {
	public BlackListedTokensDocument findByToken(String token);

}
