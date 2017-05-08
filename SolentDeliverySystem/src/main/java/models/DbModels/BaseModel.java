package models.DbModels;

/**
 * Base model is an interface that all Database models implement from.
 * Base model is empty but is needed for CommonDao in particular
 * because we can pass to its methods any model that actually
 * implements from BaseModel. In that way we dont have to create
 * same methods in each specific Dao's if method is the same for
 * all Database Models (objects).
 */
public interface BaseModel {
}
