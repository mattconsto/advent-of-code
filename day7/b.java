import java.io.*;
import java.util.*;
import java.util.regex.*;

public class b {
	public static class Node<T> {
		public T value;
		public Worker<T> worker; // Reference to a Worker instance
		public Map<T, Node<T>> parent = new TreeMap<>();
		public Map<T, Node<T>> children = new TreeMap<>();

		public Node(T value) {this.value = value;}
	}

	public static class Worker<T> {
		public int busy = 0;
		public Node<T> task;
	}

	public static void main(String[] args) {
		Map<String, Node<String>> found = new TreeMap<>();
		List<Worker<String>> workers = Arrays.asList(new Worker<>(), new Worker<>(), new Worker<>(), new Worker<>(), new Worker<>()); // input.txt: 5
		int timePerStep = 60; // input.txt: 60
		int timeTaken = 0;

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

		// Satisfy
		do {
			// System.out.println("log " + timeTaken + "\tw1 " +
			// (workers.get(0).task != null ? workers.get(0).task.value + " " + workers.get(0).busy: '.') + "\tw2 " +
			// (workers.get(1).task != null ? workers.get(1).task.value + " " + workers.get(1).busy : '.'));

			// for(Map.Entry<String, Node<String>> node : found.entrySet()) {
			// 	String key = node.getKey();
			// 	Node<String> val = node.getValue();
			// 	System.err.println(key + ": " + val.parent.size() + " parents, " + val.children.size() + " children.");
			// }

			// Find free workers
			List<Integer> freeWorkers = new ArrayList<>();
			for(int i = 0; i < workers.size(); i++) if(workers.get(i).busy <= 0) freeWorkers.add(i);

			// Find free jobs
			List<String> satisfiable = new ArrayList<>();
			for(Map.Entry<String, Node<String>> node : found.entrySet()) {
				if(satisfiable.size() >= freeWorkers.size()) break;
				if(node.getValue().parent.isEmpty() && node.getValue().worker == null) {
					satisfiable.add(node.getKey());
				}
			}

			// Assign jobs to workers
			for(int i = 0; i < freeWorkers.size() && i < satisfiable.size(); i++) {
				String target = satisfiable.get(i);
				Worker<String> worker = workers.get(freeWorkers.get(i));
				worker.busy = timePerStep + (int) target.toUpperCase().charAt(0) - (int) 'A' + 1;
				worker.task = found.get(target);
				found.get(target).worker = worker;
			}

			// Do work
			if(freeWorkers.size() == 0 || satisfiable.size() == 0) {
				// Find smallest amount of time
				int delta = workers.stream().map(w -> w.busy).reduce((a,b) -> a>0 ? (b>0 ? Math.min(a,b) : a) : b).orElse(0);
				timeTaken += delta;
				if(delta == 0) break; // Time has frozen!

				for(int i = 0; i < workers.size(); i++) {
					// If the worker is busy, spend time
					if(workers.get(i).busy >= 0) {
						workers.get(i).busy -= delta;
						if(workers.get(i).busy <= 0 && workers.get(i).task != null) {
							System.err.print(workers.get(i).task.value); // Print it
							for(Map.Entry<String, Node<String>> child : workers.get(i).task.children.entrySet()) {
								child.getValue().parent.remove(workers.get(i).task.value);
							}
							found.remove(workers.get(i).task.value);
							workers.get(i).task = null;
						}
					}
				}
			}
		} while(found.size() > 0);

		System.err.println();
		System.out.println(timeTaken);

		if(!found.isEmpty()) System.err.println("Could not satisfy!");
	}
}
