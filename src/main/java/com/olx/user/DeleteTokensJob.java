package com.olx.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.olx.user.entity.BlackListedTokensDocument;
import com.olx.user.repo.BlackListedTokens;

@Configuration
@EnableScheduling
public class DeleteTokensJob {
	@Autowired
	BlackListedTokens blackListedTokens;
 
	@Scheduled(fixedDelay = 300000) //5 mins
	public void deleteNaturallyExpiredTokens() {
		System.out.println("Delete tokens job started");
		//Write the code to remove naturally expired tokens from blacklisted_tokens
		//collection using Repository APIs
		//blackListedTokensRepository.deletexxx();
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now.getDayOfMonth()+'/'+now.getYear());
		System.out.println(now.getHour());

		List<BlackListedTokensDocument> allDocuments = blackListedTokens.findAll();
		for(BlackListedTokensDocument document: allDocuments) {
			LocalDateTime naturalExpiredDate = document.getNaturaExpiredDate();
			System.out.println(naturalExpiredDate.toLocalDate());
			if((LocalDateTime.now()).isAfter(naturalExpiredDate)){
				blackListedTokens.deleteById(document.getId());
			}
			
			
			
		}
	}
}
