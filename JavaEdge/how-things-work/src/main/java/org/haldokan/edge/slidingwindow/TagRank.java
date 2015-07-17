package org.haldokan.edge.slidingwindow;

public class TagRank {
	private final String tag;
	private final int rank;

	public TagRank(String tag, int rank) {
		this.tag = tag;
		this.rank = rank;
	}

	public String getTag() {
		return tag;
	}

	public int getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "TagRank [tag=" + tag + ", rank=" + rank + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagRank other = (TagRank) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

}