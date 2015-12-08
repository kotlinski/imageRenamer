package se.kotlinski.imagesort.commandline;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FileSystemPrettyPrinter {


  public void prettyPrintFolderStructure(final Map<String, List<File>> mediaFileDestinations) {
    StringTree stringTree = new StringTree(File.separator);
    for (String filePath : mediaFileDestinations.keySet()) {
      List<String> subFolders = getFolders(filePath);
      addChild(stringTree.root, subFolders, 1);
    }
    printTree(stringTree.root, 0);
  }

  private void printTree(final StringTree.Node root, int level) {
    for (StringTree.Node child : root.children) {
      String prefix = " |";
      for (int i = 0; i < child.level; i++) {
        System.out.print(prefix);
      }
      System.out.println("-" + child.data);
      printTree(child, level++);

    }
  }

  private void addChild(StringTree.Node node, List<String> list, int index) {
    if (list.size() <= index) {
      return;
    }
    String itemToAdd = list.get(index);
    StringTree.Node newNode = new StringTree.Node(itemToAdd, index);

    if (node.children.contains(newNode)) {
      for (StringTree.Node child : node.children) {
        if (child.data.equals(itemToAdd)) {
          addChild(child, list, ++index);
        }
      }
    }
    else {
      node.children.add(newNode);
      addChild(newNode, list, ++index);
    }
  }

  private List<String> getFolders(final String filePath) {
    List<String> separated = new LinkedList<>(Arrays.asList(filePath.split(File.separator)));
    separated.remove(0);
    separated.add(0, File.separator);
    return separated;
  }
}


class StringTree {
  public Node root;

  public StringTree(String rootData) {
    root = new Node(rootData, 0);
  }

  public static class Node implements Comparable<Node> {
    public String data;
    public Set<Node> children = new TreeSet<>();
    public int level;

    public Node(final String data, int level) {
      this.data = data;
      this.level = level;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Node node = (Node) o;

      return data != null ? data.equals(node.data) : node.data == null;

    }

    @Override
    public int hashCode() {
      return data != null ? data.hashCode() : 0;
    }

    @Override
    public int compareTo(final Node o) {
      return o.data.compareTo(data);
    }
  }
}
