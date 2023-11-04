package searchEngine;
public interface INode {
    int getValue();
    void incrementValue();
    INode[] getChildren();
}