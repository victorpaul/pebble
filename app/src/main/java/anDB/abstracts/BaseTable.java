package anDB.abstracts;

import anDB.DBHandler;
import anDB.annotations.Column;
import anDB.annotations.Index;


/**
 * Created by victorPAul on 6/25/14.
 */
public abstract class BaseTable{

	@Column(
        name= DBHandler.TABLE_ID,
        type="INTEGER",
        AUTOINCREMENT = true,
        PRIMARY_KEY = true,
        index = @Index(name="index_id",sortBy = "DESC", unique = true)
    )
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	abstract public void beforeDelete(BaseTable baseTable);
}