package reskue;

import kueres.base.BaseController;

public abstract class ReskueController<E extends ReskueEntity<E>, R extends ReskueRepository<E>, S extends ReskueService<E, R>> extends BaseController<E, R, S>{

}
