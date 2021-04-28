package reskue.culturalasset;

import org.springframework.stereotype.Repository;

import reskue.ReskueRepository;

/**
 * 
 * The repository for CulturalAssetEntities
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@Repository
public interface CulturalAssetRepository extends ReskueRepository<CulturalAssetEntity>{

	CulturalAssetEntity findByLocationId(String locationId);
	
}
