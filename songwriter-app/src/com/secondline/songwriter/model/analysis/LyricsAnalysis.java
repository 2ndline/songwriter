package com.secondline.songwriter.model.analysis;

public class LyricsAnalysis {
	public enum Balance {
		SYMMETRICAL, ASYMMETRICAL
	}

	public enum Pace {
		CONSTANT, ACCELERATED, DECELERATED
	}

	public enum Flow {
		THROUGHWRITTEN, FRAGMENTED
	}

	public enum Closure {
		CLOSED, OPEN
	}

	public enum ClosureType {
		EXPECTED, UNEXPECTED, DECEPTIVE
	}

	public LyricsAnalysis() {

	}

	private Balance balance;
	private Pace pace;
	private Flow flow;
	private Closure closure;
	private ClosureType cType;

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public Pace getPace() {
		return pace;
	}

	public void setPace(Pace pace) {
		this.pace = pace;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public Closure getClosure() {
		return closure;
	}

	public void setClosure(Closure closure) {
		this.closure = closure;
	}

	public ClosureType getcType() {
		return cType;
	}

	public void setcType(ClosureType cType) {
		this.cType = cType;
	}
}
