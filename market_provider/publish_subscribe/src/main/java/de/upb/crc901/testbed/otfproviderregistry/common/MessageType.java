package de.upb.crc901.testbed.otfproviderregistry.common;

/**
 * Defines various types for messages sent by the self-stabilizing protocol for the pub-/sub-system written by A1.
 * 
 * @author Michael
 *
 */
public enum MessageType {
	REPORT_GUI, SUBSCRIBE, UNSUBSCRIBE, RECEIVE_CONFIGURATION, INTRODUCE_CYC, REMOVE_CONNECTION, CHECK, LINEARIZE, INTRODUCE_SHORTCUT, PROBE_SHORTCUT, CHECK_SHORTCUT, ADD_DOMAIN, REMOVE_DOMAIN, PUBLISH, CHECK_TRIE, CHECK_AND_PUBLISH;
}