package searchEngine;
public class TrieNode implements INode {
	
	TrieNode[] nodes = new TrieNode[26];
	int count;
    boolean isEnd;

	@Override
	public int getValue() {		
		return count;
	}
	
	@Override
	public void incrementValue() {
		count++;	
	}
	
	@Override
	public INode[] getChildren() {
		return nodes;
	}	
}


