package reskue;

import kueres.base.BaseRepository;

/**
 * 
 * The ReskueRepository provides repository functionality for a ReskueEntity that is needed for the rest of the reskue package.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

public interface ReskueRepository<E extends ReskueEntity<E>> extends BaseRepository<E> {

}
