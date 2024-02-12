package com.ex.flipkartclone.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ex.flipkartclone.entity.AccessToken;
import com.ex.flipkartclone.entity.RefreshToken;
import com.ex.flipkartclone.repo.AccessTokenRepo;
import com.ex.flipkartclone.repo.RefreshTokenRepo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ScheduledJobs {

	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;

	@Scheduled(cron = "")
	@Scheduled(cron = "0 0 */6 * * *")
	public void removeExpiredAccessTokens() {
		List<AccessToken> accessToken = accessTokenRepo.findByExpirationBefore(LocalDateTime.now());
		accessToken.removeAll(accessToken);
	}

	@Scheduled(cron = "")
	@Scheduled(cron = "0 0 */6 * * *")
	public void removeExpiredRefreshTokens() {
		List<RefreshToken> refreshToken = refreshTokenRepo.findByExpirationBefore(LocalDateTime.now());
		refreshToken.removeAll(refreshToken);
	}

}
