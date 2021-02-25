package reskue.culturalasset;

import org.springframework.stereotype.Repository;

import reskue.ReskueRepository;

/**
 * 
 * The repository for CulturalAssetEntities
 *
 * @author Jan Straﬂburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Repository
public interface CulturalAssetRepository extends ReskueRepository<CulturalAssetEntity>{

	CulturalAssetEntity findByLocationId(String locationId);
	
}
