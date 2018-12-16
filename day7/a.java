import java.io.*;
import java.util.*;
import java.util.regex.*;

public class a {
	public static class Node<T> {
		public T value;
		public Map<T, Node<T>> parent = new TreeMap<>();
		public Map<T, Node<T>> children = new TreeMap<>();

		public Node(T value) {this.value = value;}
	}

	public static void main(String[] args) {
		Map<String, Node<String>> found = new TreeMap<>();

		// Parse the file
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));

			Pattern pattern = Pattern.compile("^Step (.) must be finished before step (.) can begin\\.$");
			String line;

			// Construct the tree
			while ((line = br.readLine()) != null) {
				Matcher m = pattern.matcher(line);

				if(m.matches()) {
					String a = m.group(1), b = m.group(2);

					// Add if missing
					if(!found.containsKey(a)) found.put(a, new Node<>(a));
					if(!found.containsKey(b)) found.put(b, new Node<>(b));

					// Add references
					found.get(b).parent.put(a, found.get(a));
					found.get(a).children.put(b, found.get(b));
				}
			}

			br.close();
		} catch(IOException e) {System.exit(1);}

		// for(Map.Entry<String, Node<String>> node : found.entrySet()) {
		// 	String key = node.getKey();
		// 	Node<String> val = node.getValue();
		// 	System.err.println(key + ": " + val.parent.size() + " parents, " + val.children.size() + " children.");
		// }

		// Satisfy
		int lastSize;
		do {
			lastSize = found.size();

			for(Map.Entry<String, Node<String>> node : found.entrySet()) {
				// If satisfiable
				if(node.getValue().parent.isEmpty()) {
					System.out.print(node.getKey()); // Must be first!
					for(Map.Entry<String, Node<String>> child : node.getValue().children.entrySet()) {
						child.getValue().parent.remove(node.getKey());
					}
					found.remove(node.getKey());
					break;
				}
			}
		} while(lastSize > found.size());

		System.out.println();

		if(!found.isEmpty()) System.err.println("Could not satisfy!");
	}
}
