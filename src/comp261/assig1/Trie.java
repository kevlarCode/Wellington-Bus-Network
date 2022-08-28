package comp261.assig1;

import java.util.*;

/**
 * This is an implementation of a trie, used for the search box.
 */

public class Trie {
	TrieNode root = new TrieNode(); // the root node of the trie

	// Contructor - takes no arguements
	public Trie() {
	}

	/**
	 * Adds a given stop to the Trie.
	 */
	public void addStops(Stop stop) {
		TrieNode node = root;
		char[] stopName = new char[stop.getName().length()]; // Creating a List of characters to set nodes
		for (int i = 0; i < stop.getName().length(); i++) {
			stopName[i] = stop.getName().toLowerCase().charAt(i); // appending characters
		}
		// iterate through the char:
		for (char character : stopName) {
			if (!node.children.keySet().contains(character)){
				TrieNode childNode = new TrieNode(); // create new node if it doesnt exist
				node.children.put(character, childNode);
			}
			node = node.children.get(character); // update node 
		}
		node.data.add(stop); // add the stop 
	}

	/**
	 * Returns all the stops whose names start with a given prefix.
	 */
	public ArrayList<Stop> getAll(String prefix) {
		prefix = prefix.toLowerCase(); // To lowercase to match case search

		// convert String into List of Char 
		TrieNode node = root;
		ArrayList <Stop> stops = new ArrayList<Stop>();
		char[] word = new char[prefix.length()];
		for (int i = 0; i < prefix.length(); i++) {
			word[i] = prefix.charAt(i);
		}
		// Check all characters in the search
		for (char character : word ) {
			if (!node.children.keySet().contains(character)) {
				return null;
			}
			node = node.children.get(character); // update node position
		}
		getFromNode(node, stops);
		return stops; // return all related stops to the search
	}

	/**
	 *  Recursive method to traverse through the tris.
	 */
	public void getFromNode (TrieNode node, List<Stop> stops ) {
		//if (!node.data.isEmpty()) stops.addAll(node.data);
		for (int i = 0; i < node.data.size(); i++) { // adds all the stops from the data list of the respective TrieNode
			stops.add(node.data.get(i));
		}
		for (TrieNode child : node.children.values()) {
			getFromNode(child, stops); // Recursive Call
		}
	}

	/**
	 * Represents a single node in the trie. It contains a collection of the
	 * stops whose names are exactly the traversal down to this node.
	 */
	private class TrieNode {
		List<Stop> data = new ArrayList<>();
		Map<Character, TrieNode> children = new HashMap<>();
	}
}
