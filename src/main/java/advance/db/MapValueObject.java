package advance.db;

import java.io.Serializable;

public class MapValueObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected double pageRank;
	
	public MapValueObject() {

	}

	public MapValueObject(double pageRank) {
		this.pageRank = pageRank;
	}

	public double getPageRank() {
		return pageRank;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+pageRank;
	}
	
	
	

}
