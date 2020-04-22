package advance.db;

import java.util.List;

public class MapValueObjectOutBound extends MapValueObject {

	private static final long serialVersionUID = 1L;

	List<String> nodeList;

	public MapValueObjectOutBound() {

	}

	public MapValueObjectOutBound(double pageRank, List<String> nodeList) {
		super(pageRank);
		this.nodeList = nodeList;
	}

	public List<String> getNodeList() {
		return nodeList;
	}

	@Override
	public String toString() {
		return "MapValueObjectOutBound [nodeList=" + nodeList + "]";
	}

}
