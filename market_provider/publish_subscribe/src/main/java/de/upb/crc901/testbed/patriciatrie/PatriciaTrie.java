package de.upb.crc901.testbed.patriciatrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.upb.crc901.testbed.otfproviderregistry.common.StringOperations;

/**
 * A partricia trie (or radix trie) in which one can insert publications.
 * 
 * @author Michael
 *
 */
public class PatriciaTrie {

	private TrieNode root;

	/**
	 * Creates an empty Particia trie.
	 */
	public PatriciaTrie() {
		root = null;
	}

	/**
	 * Returns true, if this trie does not store any publications.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the label of the root node.
	 * 
	 * @return
	 */
	public String getRootLabel() {
		if (root == null)
			return null;
		return root.getLabel();
	}

	/**
	 * Returns the hash of the root node.
	 * 
	 * @return
	 */
	public String getRootHash() {
		if (root == null)
			return null;
		return root.getHash();
	}

	/**
	 * Returns the node with the given label or null, if no such node exists in this partricia trie.
	 * 
	 * @param label
	 * @return
	 */
	public TrieNode search(String label) {
		if (root == null)
			return null;
		return this.search(label, root);
	}

	private TrieNode search(String label, TrieNode node) {
		if (node.getLabel().equals(label))
			return node;
		if (!node.isLeafNode()) {
			if (label.startsWith(node.getLabel() + node.getLeftEdgeLabel()))
				return this.search(label, node.getLeftChild());
			if (label.startsWith(node.getLabel() + node.getRightEdgeLabel()))
				return this.search(label, node.getRightChild());
		}
		return null;
	}

	/**
	 * Returns the node with label of minimum length that has the given label as a prefix or null, if no such node exists in the partricia trie.
	 * 
	 * @param prefix
	 * @return
	 */
	public TrieNode searchMinimalLabelPrefix(String prefix) {
		if (root == null)
			return null;
		return this.searchMinimalLabelPrefix(prefix, root);
	}

	private TrieNode searchMinimalLabelPrefix(String prefix, TrieNode node) {
		if (node.getLabel().startsWith(prefix))
			return node;
		if (!node.isLeafNode()) {
			if (prefix.startsWith(node.getLabel() + node.getLeftEdgeLabel()))
				return this.searchMinimalLabelPrefix(prefix, node.getLeftChild());
			if (prefix.startsWith(node.getLabel() + node.getRightEdgeLabel()))
				return this.searchMinimalLabelPrefix(prefix, node.getRightChild());
			return null;
		}
		return null;
	}

	/**
	 * Inserts the given publication into the partricia trie.
	 * 
	 * @param pub
	 */
	public void insert(Publication pub) {
		if (root == null) {
			root = new TrieNode(pub);
		} else if (root.isLeafNode()) {
			TrieNode r = new TrieNode();
			TrieNode p = new TrieNode(pub);
			String lcp = this.lcp(root, p);
			r.setLabel(lcp);
			if (root.getLabel().substring(lcp.length()).charAt(r.getLabel().length()) < p.getLabel().substring(lcp.length())
					.charAt(r.getLabel().length())) {
				r.setLeftChild(root);
				r.setRightChild(p);
			} else {
				r.setLeftChild(p);
				r.setRightChild(root);
			}
			r.updateHash();
			this.root = r;
		} else
			this.insert(pub, root, new Stack<>(), false);
	}

	private void insert(Publication pub, TrieNode node, Stack<TrieNode> parentStack, boolean left) {
		if (pub.getPublicationLabel().startsWith(node.getLabel())) {
			parentStack.push(node);
			if (pub.getPublicationLabel().startsWith(node.getLabel() + node.getLeftEdgeLabel().charAt(0)))
				this.insert(pub, node.getLeftChild(), parentStack, true);
			else
				this.insert(pub, node.getRightChild(), parentStack, false);
		} else {
			if (parentStack.isEmpty()) {
				TrieNode r = new TrieNode();
				TrieNode p = new TrieNode(pub);
				String lcp = this.lcp(node, p);
				r.setLabel(lcp);
				if (node.getLabel().substring(lcp.length()).charAt(0) < p.getLabel().substring(lcp.length()).charAt(0)) {
					r.setLeftChild(node);
					r.setRightChild(p);
				} else {
					r.setLeftChild(p);
					r.setRightChild(node);
				}
				r.updateHash();
				this.root = r;
			} else {
				TrieNode r = new TrieNode();
				TrieNode p = new TrieNode(pub);
				String lcp_l = this.lcp(node, p);
				r.setLabel(lcp_l);
				if (node.getLabel().substring(lcp_l.length()).charAt(0) < p.getLabel().substring(lcp_l.length()).charAt(0)) {
					r.setLeftChild(node);
					r.setRightChild(p);
				} else {
					r.setLeftChild(p);
					r.setRightChild(node);
				}
				r.updateHash();
				if (left) {
					parentStack.peek().setLeftChild(r);
				} else {
					parentStack.peek().setRightChild(r);
				}
				while (!parentStack.isEmpty())
					parentStack.pop().updateHash();
			}
		}
	}

	private String lcp(TrieNode n1, TrieNode n2) {
		String[] s = { n1.getLabel(), n2.getLabel() };
		return StringOperations.longestCommonPrefix(s);
	}

	/**
	 * Removes the given publication from the PatriciaTrie. Does nothing, if the given publication does not exist beforehand in the trie.
	 * 
	 * @param publication
	 */
	public void remove(Publication publication) {
		TrieNode pNode = this.search(publication.getPublicationLabel());
		if (pNode != null) {
			if (pNode == this.root) {
				this.root = null;
			} else {
				TrieNode parent = this.getParent(pNode);
				TrieNode neighbor = null;
				if (parent.getLeftChild() == pNode) {
					neighbor = parent.getRightChild();
				} else if (parent.getRightChild() == pNode) {
					neighbor = parent.getLeftChild();
				} else {
					System.err.println("There might be an error in getParent() in PatriciaTrie!");
				}

				if (parent == root) {
					this.root = neighbor;
				} else {
					TrieNode parent_2 = this.getParent(parent);
					if (parent_2.getLeftChild() == parent) {
						parent_2.setLeftChild(neighbor);
					} else if (parent_2.getRightChild() == parent) {
						parent_2.setRightChild(neighbor);
					} else {
						System.err.println("There might be an error in getParent() in PatriciaTrie!");
					}

					while (parent_2 != null) {
						parent_2.updateHash();
						parent_2 = this.getParent(parent_2);
					}
				}
			}
		} // else publication not in the trie
	}

	private TrieNode getParent(TrieNode node) {
		if (root == null || node == root)
			return null;
		return this.searchParent(node.getLabel(), root);
	}

	private TrieNode searchParent(String label, TrieNode node) {
		if (node.isLeafNode())
			return null;
		if (node.getLeftChild().getLabel().equals(label) || node.getRightChild().getLabel().equals(label))
			return node;
		if (label.startsWith(node.getLabel() + node.getLeftEdgeLabel()))
			return this.searchParent(label, node.getLeftChild());
		if (label.startsWith(node.getLabel() + node.getRightEdgeLabel()))
			return this.searchParent(label, node.getRightChild());
		return null;
	}

	/**
	 * Returns all publications stored in this trie.
	 * 
	 * @return
	 */
	public List<Publication> getPublications() {
		if(this.isEmpty())
			return new ArrayList<>();
		List<Publication> erg = new ArrayList<>();
		erg.addAll(getPublications(root));
		return erg;
	}

	private List<Publication> getPublications(TrieNode root) {
		List<Publication> erg = new ArrayList<>();
		if (root.isLeafNode()) {
			erg.add(root.getPublication());
			return erg;
		}
		erg.addAll(getPublications(root.getLeftChild()));
		erg.addAll(getPublications(root.getRightChild()));
		return erg;
	}

	/**
	 * Returns all publications stored in this trie that have the given label prefix.
	 * 
	 * @param prefix
	 * @return
	 */
	public List<Publication> getPublications(String prefix) {
		List<Publication> erg = new ArrayList<>();
		List<Publication> all = this.getPublications();
		for (Publication p : all)
			if (p.getPublicationLabel().startsWith(prefix))
				erg.add(p);
		return erg;
	}
}
