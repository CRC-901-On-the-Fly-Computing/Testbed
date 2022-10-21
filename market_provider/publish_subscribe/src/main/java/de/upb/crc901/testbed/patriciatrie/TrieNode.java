package de.upb.crc901.testbed.patriciatrie;

import de.upb.crc901.testbed.otfproviderregistry.common.Hashing;
import de.upb.crc901.testbed.otfproviderregistry.common.StringOperations;

public class TrieNode {
	private String label;
	private TrieNode left_child, right_child;
	private Publication publication;
	private String hash;

	public TrieNode() {
		label = "";
		left_child = null;
		right_child = null;
		publication = null;
		this.updateHash();
	}

	public TrieNode(Publication p) {
		left_child = null;
		right_child = null;
		publication = p;
		label = p.getPublicationLabel();
		this.updateHash();
	}

	public boolean isLeafNode() {
		return left_child == null && right_child == null;
	}

	public boolean checkCorrectness() {
		if (publication != null && !this.isLeafNode()) {
			System.err.println("Node check failed!");
			return false;
		}

		if (!this.isLeafNode()) {
			if (!this.hash.equals(Hashing.hashStringtoBitstring(left_child.getNodeHash() + right_child.getNodeHash(), Publication.BITSTRING_SIZE))) {
				System.err.println("Hash check failed!");
				return false;
			}

			String[] s = { left_child.getLabel(), right_child.getLabel() };
			if (!this.label.equals(StringOperations.longestCommonPrefix(s))) {
				System.err.println("Label check failed!");
				return false;
			}
		} else {
			if (!this.publication.getPublicationLabel().equals(this.label)) {
				System.err.println("Publication label check failed!");
				return false;
			}
		}

		if (this.isLeafNode())
			return true;
		else
			return this.left_child.checkCorrectness() && this.right_child.checkCorrectness();
	}

	private String getNodeHash() {
		if (this.isLeafNode())
			return Hashing.hashStringtoBitstring(label, Publication.BITSTRING_SIZE);
		else
			return Hashing.hashStringtoBitstring(left_child.getNodeHash() + right_child.getNodeHash(), Publication.BITSTRING_SIZE);
	}

	public String getHash() {
		return this.hash;
	}

	public TrieNode getLeftChild() {
		return this.left_child;
	}

	public TrieNode getRightChild() {
		return this.right_child;
	}

	public void setLeftChild(TrieNode node) {
		this.left_child = node;
	}

	public void setRightChild(TrieNode node) {
		this.right_child = node;
	}

	public String getLeftEdgeLabel() {
		if (this.left_child != null) {
			return this.left_child.getLabel().substring(label.length());
		} else {
			System.err.println("Error: Trie node has no left edge!");
			return "";
		}
	}

	public String getRightEdgeLabel() {
		if (this.right_child != null) {
			return this.right_child.getLabel().substring(label.length());
		} else {
			System.err.println("Error: Trie node has no right edge!");
			return "";
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if(label == null)
			System.out.println("oo");
		this.label = label;
	}

	public void updateHash() {
		this.hash = this.getNodeHash();
	}

	public Publication getPublication() {
		return this.publication;
	}

	public String toString() {
		return "Is Leaf: " + this.isLeafNode() + ", (" + this.label + ", " + this.hash + ")";
	}
}
