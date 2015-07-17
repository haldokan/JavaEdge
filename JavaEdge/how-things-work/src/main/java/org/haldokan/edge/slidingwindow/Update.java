package org.haldokan.edge.slidingwindow;

import java.time.LocalTime;

public class Update {
	private final String tag;
	private final int weight;
	private final String content;
	private final LocalTime time;

	public Update(String tag, int weight, String content) {
		this.tag = tag;
		this.weight = weight;
		this.content = content;
		this.time = LocalTime.now();
	}

	public String getTag() {
		return tag;
	}

	public int getWeight() {
		return weight;
	}

	public String getContent() {
		return content;
	}

	public LocalTime getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Update [tag=" + tag + ", weight=" + weight + ", content="
				+ content + ", time=" + time + "]";
	}
}