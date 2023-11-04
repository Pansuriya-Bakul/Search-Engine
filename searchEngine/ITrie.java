package searchEngine;
public interface ITrie {

	void add(String word);

	INode find(String word);

	int getWordCount();

	int getNodeCount();

	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object o);
}
