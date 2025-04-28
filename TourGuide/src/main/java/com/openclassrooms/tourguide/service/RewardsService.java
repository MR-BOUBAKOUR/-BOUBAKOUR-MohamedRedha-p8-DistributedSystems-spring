package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

@Service
public class RewardsService {

	private final Logger logger = LoggerFactory.getLogger(RewardsService.class);

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	private final TaskExecutor customTaskExecutor;

	private List<Attraction> cachedAttractions = null;
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral, @Qualifier("customTaskExecutor") TaskExecutor customTaskExecutor) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
		this.customTaskExecutor = customTaskExecutor;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	private List<Attraction> getAttractions() {
		if (cachedAttractions == null) {
			cachedAttractions = gpsUtil.getAttractions();
		}
		return cachedAttractions;
	}

	public void calculateRewards(User user) {
		List<VisitedLocation> visitedLocations = new ArrayList<>(user.getVisitedLocations());
		List<Attraction> attractions = getAttractions();

		for(VisitedLocation visitedLocation : visitedLocations) {
			for(Attraction attraction : attractions) {
				if(nearAttraction(visitedLocation, attraction)) {
					user.addToUserRewards(
							new UserReward(
									visitedLocation,
									attraction,
									getRewardPoints(attraction, user))
					);
				}
			}
		}
	}

	public CompletableFuture<Void> calculateRewardsAsync(User user) {
		return CompletableFuture
				.runAsync(() -> {
                    logger.info("CalculateRewardsAsync for user: {} - Thread: {}", user.getUserName(), Thread.currentThread().getName());
					calculateRewards(user);
				}, customTaskExecutor)
				.exceptionally(ex -> {
					logger.error("Error in the calculateRewardsAsync : {}", ex.getMessage(), ex);
					return null;
				});
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return !(getDistance(attraction, location) > attractionProximityRange);
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);

        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}
}
