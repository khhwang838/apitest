package keichee.study.dl4j.domain;

import java.util.List;

public class PosNegDto {

	private int numOfWords;
	private List<String> positiveWords;
	private List<String> negativeWords;
	
	public int getNumOfWords() {
		return numOfWords;
	}
	public void setNumOfWords(int numOfWords) {
		this.numOfWords = numOfWords;
	}
	public List<String> getPositiveWords() {
		return positiveWords;
	}
	public void setPositiveWords(List<String> positiveWords) {
		this.positiveWords = positiveWords;
	}
	public List<String> getNegativeWords() {
		return negativeWords;
	}
	public void setNegativeWords(List<String> negativeWords) {
		this.negativeWords = negativeWords;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PosNegDto [numOfWords=");
		builder.append(numOfWords);
		builder.append(", positiveWords=");
		builder.append(positiveWords);
		builder.append(", negativeWords=");
		builder.append(negativeWords);
		builder.append("]");
		return builder.toString();
	}
}
